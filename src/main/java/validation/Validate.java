package validation;

/**
 *
 * @author Walter Grönholm
 */
public class Validate {
    
    public static void notNull(String fieldName, Object o) {
        if (o == null) {
            throw new IllegalArgumentException(fieldName + " must not be null");
        }
    }
}
