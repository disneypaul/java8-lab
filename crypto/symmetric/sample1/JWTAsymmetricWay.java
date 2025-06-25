import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class JwtRsaDecoder {

    public static void main(String[] args) throws Exception {
        // In a real app, you would generate/load these keys properly
        KeyPair keyPair = generateRsaKeys();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

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
            // 1. Create the Algorithm instance using ONLY the public key
            Algorithm verificationAlgorithm = Algorithm.RSA256(publicKey, null); // Private key is not needed for verification

            // 2. Build the verifier
            JWTVerifier verifier = JWT.require(verificationAlgorithm)
                .withIssuer("my-auth-service")
                .build();

            // 3. Verify the token
            DecodedJWT decodedJWT = verifier.verify(token);

            // 4. Extract claims if successful
            System.out.println("Token Verified Successfully!");
            System.out.println("Subject: " + decodedJWT.getSubject());
            System.out.println("Scope: " + decodedJWT.getClaim("scope").asString());

        } catch (JWTVerificationException exception){
            System.err.println("Token verification failed: " + exception.getMessage());
        }
    }
    
    // Helper method to generate keys for this example
    public static KeyPair generateRsaKeys() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }
}
