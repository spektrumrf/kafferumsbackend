package data;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Walter Grönholm
 */
public class NormDataAccessObject extends DataAccessObject {

    private final com.dieselpoint.norm.Database db;

    public NormDataAccessObject(String url, String user, String pass) {
        db = new com.dieselpoint.norm.Database();
        db.setJdbcUrl(url);
        db.setUser(user);
        db.setPassword(pass);
        db.setDriverClassName("org.h2.Driver");
    }

    @Override
    void testConnection(int timeout) {
        try {
            if (!db.getConnection().isValid(timeout))
                throw new IllegalStateException("Connection to database is either closed or invalid");
        } catch (SQLException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
    
    @Override
    public List<String> getUserNames() {
        return db.sql("SELECT NAME FROM USER").results(String.class);
    }

    @Override
    public String getPassword(String userName) {
        return db.sql("SELECT LOGIN.DATA FROM LOGIN JOIN USER ON LOGIN.ID = USER.ID_LOGIN WHERE USER.NAME = ?", userName).first(String.class);
    }
}
