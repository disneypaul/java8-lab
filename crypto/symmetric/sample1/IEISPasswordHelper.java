

import ServiceConfiguration;                    // Configurations helper
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Component
@Qualifier("IEISPasswordHelper")
public class IEISPasswordHelper {
    private static Log log = LogFactory.getLog(IEISPasswordHelper.class);
    private static String DEFAULT_ENCODING = "UTF-8";
    private static String PKDF_Alg = "PBKDF2WithHmacSHA1";
    private static int PKDF_Iteration = 1000;
    private static int PKDF_KeySize = 24;
    //protected static ConfigurationManager configurationManager = null;

    @Autowired
    private ServiceConfiguration configurationManager;

    public ServiceConfiguration getConfigurationManager() {
        return configurationManager;
    }

    public void setConfigurationManager(ServiceConfiguration configurationManager) {
        this.configurationManager = configurationManager;
    }

    public static byte[] clearPassToUserPassword(String clearpass, HashAlg alg, byte[] salt) {
        if (alg == null) {
            throw new IllegalArgumentException("Invalid hash argorithm.");
        }
        try {
            MessageDigest digester = null;
            StringBuilder resultInText = new StringBuilder();

            switch (alg.ordinal()) {
                case 0:
                    resultInText.append("{MD5}");
                    digester = MessageDigest.getInstance("MD5");
                    break;
                case 1:
                    resultInText.append("{SHA}");
                    digester = MessageDigest.getInstance("SHA");
                    break;
                case 2:
                    resultInText.append("{SMD5}");
                    digester = MessageDigest.getInstance("MD5");
                    break;
                case 3:
                    resultInText.append("{SSHA}");
                    digester = MessageDigest.getInstance("SHA");
                    break;
                case 4:
                    resultInText.append("{SSHA512}");
                    digester = MessageDigest.getInstance("SHA-512");
            }

            digester.reset();
            digester.update(clearpass.getBytes(DEFAULT_ENCODING));
            byte[] hash = null;
            if ((salt != null) && (((alg == HashAlg.SMD5) || (alg == HashAlg.SSHA) || (alg == HashAlg.SSHA512)))) {
                digester.update(salt);
                hash = ArrayUtils.addAll(digester.digest(), salt);
            } else {
                hash = digester.digest();
            }
            resultInText.append(new String(Base64.encodeBase64(hash), DEFAULT_ENCODING));

            return resultInText.toString().getBytes(DEFAULT_ENCODING);
        } catch (UnsupportedEncodingException uee) {
            log.warn("Error occurred while hashing password ", uee);
            return new byte[0];
        } catch (NoSuchAlgorithmException nse) {
            log.warn("Error occurred while hashing password ", nse);
        }
        return new byte[0];
    }

    public static byte[] clearPassToMD5UserPassword(String clearpass) {
        return clearPassToUserPassword(clearpass, HashAlg.MD5, null);
    }

    public static byte[] clearPassToSHAUserPassword(String clearpass) {
        return clearPassToUserPassword(clearpass, HashAlg.SHA, null);
    }

    public static byte[] clearPassToSHA512UserPassword(String clearpass) {
        return clearPassToUserPassword(clearpass, HashAlg.SSHA512, null);
    }

    public static boolean verifyPassword(String clearpass, byte[] userPassword) {
        try {
            String hashFromClearText = null;
            String userPasswordInText = new String(userPassword, DEFAULT_ENCODING);

            if (userPasswordInText.startsWith("{MD5}")) {
                hashFromClearText = new String(clearPassToUserPassword(clearpass, HashAlg.MD5, null));
            } else if (userPasswordInText.startsWith("{SMD5}")) {
                byte[] hashPlusSalt = Base64.decodeBase64(userPasswordInText.substring(6).getBytes(DEFAULT_ENCODING));

                byte[] salt = ArrayUtils.subarray(hashPlusSalt, 16, hashPlusSalt.length);
                hashFromClearText = new String(clearPassToUserPassword(clearpass, HashAlg.SMD5, salt));
            } else if (userPasswordInText.startsWith("{SHA}")) {
                hashFromClearText = new String(clearPassToUserPassword(clearpass, HashAlg.SHA, null));
            } else if (userPasswordInText.startsWith("{SSHA}")) {
                byte[] hashPlusSalt = Base64.decodeBase64(userPasswordInText.substring(6).getBytes(DEFAULT_ENCODING));

                byte[] salt = ArrayUtils.subarray(hashPlusSalt, 20, hashPlusSalt.length);
                hashFromClearText = new String(clearPassToUserPassword(clearpass, HashAlg.SSHA, salt));
            } else if (userPasswordInText.startsWith("{SSHA512}")) {
                byte[] hashPlusSalt = Base64.decodeBase64(userPasswordInText.substring(9).getBytes(DEFAULT_ENCODING));

                byte[] salt = ArrayUtils.subarray(hashPlusSalt, 64, hashPlusSalt.length);
                hashFromClearText = new String(clearPassToUserPassword(clearpass, HashAlg.SSHA512, salt));
            } else {
                hashFromClearText = clearpass;
            }
            return hashFromClearText.equals(userPasswordInText);
        } catch (UnsupportedEncodingException uee) {
            log.warn("Error occurred while verifying password", uee);
        }
        return false;
    }

    //Changes related to 16.03.01 #PBKDF2 : hashing
    public String generatePBKDF2Hash(String message) {
        String finalHash = "";
        //Start - Sprint 16.4.1 - US660360, US660364, US660363 ,US660361 - trimming secret answer while changing to PBKDF2

        PKDF_Iteration = Integer.parseInt(getConfigurationManager().getPropertyValue("PKDF_Iteration"));
        PKDF_KeySize = Integer.parseInt(getConfigurationManager().getPropertyValue("PKDF_KeySize"));
        if (message != null) {
            //Start - Sprint 16.12.2 US946298 - replace multiple spaces/tabs(\t)/new line(\n)/carriage return(\r),form feed(\f),etc. between words with a single space character (ASCII 32)
            message = message.replaceAll("\\s+", " ");
            //End - Sprint 16.12.2 US946298 - replace multiple spaces/tabs(\t)/new line(\n)/carriage return(\r),form feed(\f),etc. between words with a single space character (ASCII 32)
            message = message.trim().replaceAll("[\u0000-\u001f]", "").toLowerCase();
        }
        //End - Sprint 16.4.1 - US660360, US660364, US660363 ,US660361 - trimming secret answer while changing to PBKDF2
        try {
            /*
             * Start - DS Sprint 17.13.1 DS-80/DS-96: Change SecureRandom to Random to allow hashing and
             * mitigate performance issue faced in using SecureRandom
             */
            Random random = new Random();
            /*
             * Start - DS Sprint 17.13.1 DS-80/DS-96: Change SecureRandom to Random to allow hashing and
             * mitigate performance issue faced in using SecureRandom
             */
            byte[] salt = new byte[PKDF_KeySize];
            random.nextBytes(salt);
            String saltEncoded = new String(Base64.encodeBase64(salt), DEFAULT_ENCODING);

            PBEKeySpec spec = new PBEKeySpec(message.toCharArray(), salt, PKDF_Iteration, PKDF_KeySize * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(PKDF_Alg);
            byte[] pseudoValue = skf.generateSecret(spec).getEncoded();
            String pseudoEncoded = new String(Base64.encodeBase64(pseudoValue), DEFAULT_ENCODING);

            finalHash = "{PBKDF2}" + PKDF_Iteration + "$" + saltEncoded + "$" + pseudoEncoded;


        } catch (Exception ex) {
            log.error("Exception while generatePBKDF2Hash for message [" + message + "]  - " + ex.getMessage());
        }

        return finalHash;
    }

    public String getPBKDF2HashWithSalt(int iteration, String salt, String message) {
        String finalHash = "";
        //Start - Sprint 16.4.1 - US660360, US660364, US660363 ,US660361 - trimming secret answer while changing to PBKDF2

        PKDF_KeySize = Integer.parseInt(getConfigurationManager().getPropertyValue("PKDF_KeySize"));
        if (message != null) {
            //Start - Sprint 16.12.2 US946298 - replace multiple spaces/tabs(\t)/new line(\n)/carriage return(\r),form feed(\f),etc. between words with a single space character (ASCII 32)
            message = message.replaceAll("\\s+", " ");
            //End - Sprint 16.12.2 US946298 - replace multiple spaces/tabs(\t)/new line(\n)/carriage return(\r),form feed(\f),etc. between words with a single space character (ASCII 32)
            message = message.trim().replaceAll("[\u0000-\u001f]", "").toLowerCase();
        }
        //End - Sprint 16.4.1 - US660360, US660364, US660363 ,US660361 - trimming secret answer while changing to PBKDF2
        try {
            byte[] saltDecoded = Base64.decodeBase64(salt.getBytes(DEFAULT_ENCODING));

            PBEKeySpec spec = new PBEKeySpec(message.toCharArray(), saltDecoded, iteration, PKDF_KeySize * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(PKDF_Alg);
            byte[] pseudoValue = skf.generateSecret(spec).getEncoded();
            String pseudoEncoded = new String(Base64.encodeBase64(pseudoValue), DEFAULT_ENCODING);
            finalHash = "{PBKDF2}" + iteration + "$" + salt + "$" + pseudoEncoded;
        } catch (Exception ex) {
            log.error("Exception while getPBKDF2HashWithSalt for message [" + message + "] salt [" + salt + "] iteration [" + iteration + "] - " + ex.getMessage());
        }

        return finalHash;
    }
    //Completed 16.03.01 changes

    public static enum HashAlg {
        MD5, SHA, SMD5, SSHA, SSHA512;
    }
}

