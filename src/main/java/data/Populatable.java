package data;

/**
 * Helper class to be extended by all populatable data classes.
 *
 * @author Walter Grönholm
 */
abstract public class Populatable {
    
    abstract public <T extends Populatable> T populated();

    protected Populator populator() {
        return DataAccessObject.getInstance();
    }
}
