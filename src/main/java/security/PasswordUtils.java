package security;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PasswordUtils {
    
    private static final Logger LOG = LoggerFactory.getLogger(PasswordUtils.class);
    
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 224;
    private static final int SALT_LENGTH = 256 - KEY_LENGTH - 2;

    public static String hashify(String password) throws Exception {
        byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(SALT_LENGTH);
        return Base64.getEncoder().encodeToString(salt) + "$" + hash(password, salt);
    }

    public static boolean verify(String password, String stored) throws Exception {
        String[] split = stored.split("\\$");
        switch (split.length) {
            case 1:
                LOG.warn("User has an unencrypted password stored in the database!");
                return stored.equals(password);
            case 2:
                String salt = split[0];
                String hash = split[1];
                return hash.equals(hash(password, Base64.getDecoder().decode(salt)));
            default:
                throw new IllegalStateException(
                    "The stored password must have the form 'salt$hash'");
        }
    }

    private static String hash(String password, byte[] salt) throws Exception {
        if (password == null || password.length() == 0)
            throw new IllegalArgumentException("Empty passwords are not supported.");
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey key = f.generateSecret(new PBEKeySpec(
            password.toCharArray(), salt, ITERATIONS, KEY_LENGTH));
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}
