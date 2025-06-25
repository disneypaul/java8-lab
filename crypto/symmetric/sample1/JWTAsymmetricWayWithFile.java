import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class JwtRsaDecoderFromFile {

    public static void main(String[] args) throws Exception {
        // IMPORTANT: Replace with the actual path to your key files
        String publicKeyPath = "path/to/your/public_key.pem";
        String privateKeyPath = "path/to/your/private_key.pem";

        // Load keys from files
        RSAPublicKey publicKey = loadPublicKey(publicKeyPath);
        RSAPrivateKey privateKey = loadPrivateKey(privateKeyPath);
        
        System.out.println("Keys loaded successfully from files.");

        // --- Step 1: Create a sample token with the Private Key ---
        Algorithm creationAlgorithm = Algorithm.RSA256(publicKey, privateKey);
        String token = JWT.create()
                .withIssuer("my-auth-service")
                .withSubject("user123")
                .withClaim("scope", "read:data")
                .sign(creationAlgorithm);

        System.out.println("Generated Token: " + token);
        System.out.println("--- Verifying Token ---");

        // --- Step 2: Verify the token with the Public Key ---
        try {
            // Create the Algorithm instance using ONLY the public key
            Algorithm verificationAlgorithm = Algorithm.RSA256(publicKey, null);

            JWTVerifier verifier = JWT.require(verificationAlgorithm)
                .withIssuer("my-auth-service")
                .build();

            DecodedJWT decodedJWT = verifier.verify(token);

            System.out.println("Token Verified Successfully!");
            System.out.println("Subject: " + decodedJWT.getSubject());
            System.out.println("Scope: " + decodedJWT.getClaim("scope").asString());

        } catch (JWTVerificationException exception){
            System.err.println("Token verification failed: " + exception.getMessage());
        }
    }

    /**
     * Loads a public key from a PEM file.
     * @param filePath The path to the PEM file.
     * @return RSAPublicKey object.
     */
    public static RSAPublicKey loadPublicKey(String filePath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Files.readAllBytes(Paths.get(filePath));
        String keyString = new String(keyBytes);

        // Remove the PEM header and footer
        String publicKeyPEM = keyString
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PUBLIC KEY-----", "");

        // Base64 decode the key
        byte[] decoded = Base64.getDecoder().decode(publicKeyPEM);

        // Create the key spec
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);

        // Get the key factory and generate the public key
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) kf.generatePublic(spec);
    }

    /**
     * Loads a private key from a PEM file.
     * @param filePath The path to the PEM file.
     * @return RSAPrivateKey object.
     */
    public static RSAPrivateKey loadPrivateKey(String filePath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Files.readAllBytes(Paths.get(filePath));
        String keyString = new String(keyBytes);

        // Remove the PEM header and footer
        String privateKeyPEM = keyString
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PRIVATE KEY-----", "");
        
        // Base64 decode the key
        byte[] decoded = Base64.getDecoder().decode(privateKeyPEM);

        // Create the key spec for a PKCS#8 encoded key
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);

        // Get the key factory and generate the private key
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) kf.generatePrivate(spec);
    }
}
