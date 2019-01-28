package data;

import java.util.List;

/**
 *
 * @author Walter Grönholm
 */
public interface UserDAO {

    public List<String> getUserNames();

    public String getPassword(String userName);
    
    public int getLoginAttempts(String userName);

    public void setLoginAttempts(String userName, int failedAttempts);
    
    public UserData getUserData(String userName);

    public UserData getUserData(int userId);

}
