package data;

import java.util.List;

/**
 *
 * @author Walter Grönholm
 */
public interface UserDAO {

    abstract public List<String> getUserNames();

    abstract public String getPassword(String userName);

}
