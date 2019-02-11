package data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import validation.Validate;

/**
 * An abstract class extended by DAO implementations. A static DAO instance must be first initialised with {@link init}, whereafter it can be retrieved by {@link getInstance}.
 *
 * @author Walter Grönholm
 */
public abstract class Access {

    private static final Logger LOG = LoggerFactory.getLogger(Access.class);
    
    private static Access data;

    public final UserDAO users;
    public final LedgerDAO ledgers;
    public final ItemDAO items;
    public final Populator populator;

    private Access(UserDAO userDAO, LedgerDAO ledgerDAO, ItemDAO itemDAO, Populator populator) {
        Validate.notNull("userDAO", userDAO);
        Validate.notNull("ledgerDAO", ledgerDAO);
        Validate.notNull("itemDAO", itemDAO);
        Validate.notNull("populator", populator);
        this.users = userDAO;
        this.ledgers = ledgerDAO;
        this.items = itemDAO;
        this.populator = populator;
    }

    public static Access data() {
        return data;
    }

    /**
     * Assigns a new static DAO instance.
     */
    public static void init(String url, String user, String pass, int connectionTimeout) {
        final NormDataAccessObject instance = new NormDataAccessObject(url, user, pass);
        LOG.info("Initialized DataAccessObject on " + url + "... Testing connection");
        instance.testConnection(connectionTimeout);
        LOG.info("Database connection established");
        data = new Access(instance, instance, instance, instance) {
            @Override
            protected void testConnection(int timeout) {
                instance.testConnection(timeout);
            }
        };
    }

    /**
     * Tests the connection to the database.
     */
    abstract protected void testConnection(int timeout);

}
