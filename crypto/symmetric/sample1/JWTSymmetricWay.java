import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtDecoder {

    public static void main(String[] args) {
        // A sample token you would receive from a client's Authorization header
        // This token was created with the secret "my-super-secret-key"
        String token = "sdfdssdfsdfssdfheggfgergfhjhjgxcxvcxcxvwerw";

        // The secret key MUST be the same one used to sign the token
        String secret = "my-super-secret-key";

        try {
            // 1. Create the Algorithm instance using the secret key
            Algorithm algorithm = Algorithm.HMAC256(secret);

            // 2. Build the verifier
            // We can also add checks for claims like issuer and audience here
            JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("auth-server") // Optional: verify the 'iss' claim
                .build(); 

            // 3. Verify the token. This is the crucial step.
            // If the token is invalid (bad signature, expired, etc.), it will throw JWTVerificationException
            DecodedJWT decodedJWT = verifier.verify(token);

            // 4. If verification is successful, you can extract the claims
            String subject = decodedJWT.getSubject();
            String name = decodedJWT.getClaim("name").asString();
            String role = decodedJWT.getClaim("role").asString();
            String issuer = decodedJWT.getIssuer();

            System.out.println("Token Verified Successfully!");
            System.out.println("Subject: " + subject);
            System.out.println("Name: " + name);
            System.out.println("Role: " + role);
            System.out.println("Issuer: " + issuer);

        } catch (JWTVerificationException exception){
            // Invalid signature/claims
            System.err.println("Token verification failed: " + exception.getMessage());
        }
    }
}
