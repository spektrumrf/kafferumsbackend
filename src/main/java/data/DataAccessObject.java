package data;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Walter Grönholm
 */
public abstract class DataAccessObject {

    private static final Logger LOG = LoggerFactory.getLogger(DataAccessObject.class);

    private static DataAccessObject instance;

    public static DataAccessObject getInstance() {
        if (instance == null) {
            throw new IllegalStateException("No DataAccessObject has been initialized");
        }
        return instance;
    }

    public static void init(String url, String user, String pass, int connectionTimeout) {
        instance = new NormDataAccessObject(url, user, pass);
        LOG.info("Initialized DataAccessObject on " + url + "... Testing connection");
        instance.testConnection(connectionTimeout);
        LOG.info("Database connection established");
    }

    abstract void testConnection(int timeout);

    abstract public List<String> getUserNames();

    abstract public String getPassword(String userName);
}
