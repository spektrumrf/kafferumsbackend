package data;

import java.util.List;

/**
 *
 * @author Walter Grönholm
 */
public interface UserDAO {

    abstract public List<String> getUserNames();

    abstract public String getPassword(String userName);
    
    abstract public int getLoginAttempts(String userName);

    abstract public void setLoginAttempts(String userName, int failedAttempts);

}
