package data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An abstract class extended by DAO implementations. A static DAO instance must be first initialised with {@link init}, whereafter it can be retrieved by {@link getInstance}.
 *
 * @author Walter Grönholm
 */
public abstract class Access implements UserDAO, LedgerDAO, ItemDAO, Populator {

    private static final Logger LOG = LoggerFactory.getLogger(Access.class);

    private static Access instance;

    /**
     * @return the static DAO instance
     */
    public static Access getInstance() {
        if (instance == null) {
            throw new IllegalStateException("No DataAccessObject has been initialized");
        }
        return instance;
    }

    /**
     * Assigns a new static DAO instance.
     */
    public static void init(String url, String user, String pass, int connectionTimeout) {
        instance = new NormDataAccessObject(url, user, pass);
        LOG.info("Initialized DataAccessObject on " + url + "... Testing connection");
        instance.testConnection(connectionTimeout);
        LOG.info("Database connection established");
    }

    /**
     * Tests the connection to the database.
     */
    abstract protected void testConnection(int timeout);

}
