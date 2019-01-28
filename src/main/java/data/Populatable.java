package data;

/**
 *
 * @author Walter Grönholm
 */
public interface Populatable {

    public <T extends Populatable> T populated();
}
