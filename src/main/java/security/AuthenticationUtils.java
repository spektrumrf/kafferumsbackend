package security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import config.Configuration;
import data.DataAccessObject;
import data.UserData;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

/**
 *
 * @author Walter Grönholm
 */
public class AuthenticationUtils {

    private static final String ISSUER = "auth0";
    private static final int SECRET_LENGTH = 48;
    private static final String SECRET = generateSecret();
    
    private static String generateSecret() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[SECRET_LENGTH];
        secureRandom.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String tokenize(String userName) throws JWTCreationException {
        return AuthenticationUtils.tokenize(userName, getAlgorithm());
    }

    private static Algorithm getAlgorithm() throws IllegalArgumentException {
        return Algorithm.HMAC256(SECRET);
    }

    private static String tokenize(String userName, Algorithm algorithm) throws JWTCreationException, IllegalArgumentException {
        Date currentDate = new Date();
        Date expiryDate = new Date(currentDate.getTime() + Configuration.tokenExpirySeconds() * 1000);
        String token = JWT.create()
            .withIssuer(ISSUER)
            .withIssuedAt(currentDate)
            .withExpiresAt(expiryDate)
            .withSubject(userName)
            .sign(algorithm);
        return token;
    }

    /**
     * First decodes the token, then verifies its signature, lastly the user
     * data is fetched.
     */
    public static UserData verifyAndDetokenize(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(getAlgorithm())
            .withIssuer(ISSUER)
            .build();
        DecodedJWT jwt = verifier.verify(token);
        String userName = jwt.getSubject();
        return DataAccessObject.getInstance().getUserData(userName);
    }

}
