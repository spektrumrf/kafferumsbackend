package data;

/**
 *
 * @author Walter Grönholm
 */
abstract public class Populatable {
    
    abstract public <T extends Populatable> T populated();

    protected Populator populator() {
        return DataAccessObject.getInstance();
    }
}
