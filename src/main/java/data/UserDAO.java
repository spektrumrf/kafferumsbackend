package data;

import java.util.List;

/**
 *
 * @author Walter Grönholm
 */
public interface UserDAO {

    public List<String> names();

    public String password(String userName);
    
    public int loginAttempts(String userName);

    public void loginAttempts(String userName, int failedAttempts);
    
    public UserData withName(String userName);

    public UserData withId(int userId);

}
