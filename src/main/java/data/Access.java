package data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import validation.Validate;

/**
 * An abstract class extended by DAO implementations.
 *
 * A static Access instance must be first initialised with {@link init},
 * whereafter specific DAOs can be retrieved by .
 *
 * @author Walter Grönholm
 */
public abstract class Access {

    private static final Logger LOG = LoggerFactory.getLogger(Access.class);

    private static Access databaseAccess;

    public final UserDAO users;
    public final LedgerDAO ledgers;
    public final ItemDAO items;
    public final Populator populator;

    Access(UserDAO userDAO, LedgerDAO ledgerDAO, ItemDAO itemDAO, Populator populator) {
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
        Validate.notNull("databaseAccess", databaseAccess);
        return databaseAccess;
    }

    /**
     * Assigns a new static DAO instance.
     *
     * Uses {@link NormDatabaseAccess} by default.
     */
    public static void init(String url, String user, String pass, int connectionTimeout) {
        init(NormDatabaseAccess.Factory.create(url, user, pass), connectionTimeout);
    }

    static void init(Access databaseAccess, int connectionTimeout) {
        LOG.info("Initialized database access... Testing connection");
        databaseAccess.testConnection(connectionTimeout);
        LOG.info("Database connection established");
        Access.databaseAccess = databaseAccess;
    }

    /**
     * Tests the connection to the database.
     */
    abstract protected void testConnection(int timeout);

}
