package data;

import com.dieselpoint.norm.Database;
import com.dieselpoint.norm.Transaction;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Essentially a big collection of database queries.
 *
 * @author Walter Grönholm
 */
public class NormDatabaseAccess extends Access {

    static class Factory {
        
        static NormDatabaseAccess create(String url, String user, String pass) {
            Database db = new Database();
            db.setJdbcUrl(url);
            db.setUser(user);
            db.setPassword(pass);
            db.setDriverClassName("org.h2.Driver");
            
            return new NormDatabaseAccess(
                db,
                getUserDAO(db),
                getLedgerDAO(db),
                getItemDAO(db),
                getPopulator(db)
            );
        }

        static UserDAO getUserDAO(Database db) {
            return new UserDAO() {

                @Override
                public List<String> names() {
                    return db.sql("SELECT NAME FROM USER").results(String.class);
                }

                @Override
                public String password(String userName) {
                    return db.sql("SELECT LOGIN.DATA FROM LOGIN JOIN USER ON LOGIN.ID = USER.ID_LOGIN WHERE USER.NAME = ?", userName).first(String.class);
                }

                @Override
                public int loginAttempts(String userName) {
                    return db.sql("SELECT LOGIN.ATTEMPTS FROM LOGIN JOIN USER ON LOGIN.ID = USER.ID_LOGIN WHERE USER.NAME = ?", userName).first(int.class);
                }

                @Override
                public void loginAttempts(String userName, int failedAttempts) {
                    db.sql("UPDATE LOGIN SET ATTEMPTS = ? WHERE ID = (SELECT ID_LOGIN FROM USER WHERE NAME = ?)", failedAttempts, userName).execute();
                }

                @Override
                public UserData withName(String userName) {
                    return db.where("name=?", userName).first(UserData.class);
                }

                @Override
                public UserData withId(int userId) {
                    return db.where("id=?", userId).first(UserData.class);
                }

            };
        }

        static LedgerDAO getLedgerDAO(Database db) {
            return new LedgerDAO() {

                @Override
                public void addPurchase(int ledgerId, PurchaseData purchaseData) {
                    Transaction transaction = db.startTransaction();
                    db.transaction(transaction).insert(purchaseData);
                    for (PurchaseItemData item : purchaseData.getPurchaseItems()) {
                        item.purchaseId = purchaseData.id;
                        item.itemId = item.itemData.id;
                        db.transaction(transaction).insert(item);
                    }
                    transaction.commit();
                }

                @Override
                public LedgerData withId(int ledgerId) {
                    return db.where("id=?", ledgerId).first(LedgerData.class);
                }

                @Override
                public List<LedgerData> ofUser(int userId) {
                    return db.where("id_user=?", userId).results(LedgerData.class);
                }

                @Override
                public LedgerData latestLedgerOf(String userName) {
                    UserData user = Access.data().users.withName(userName);
                    return getLatestLedger(user.id);
                }

                private LedgerData getLatestLedger(int userId) {
                    LedgerData ledger = db.sql(
                        "SELECT LEDGER.* FROM LEDGER "
                        + "JOIN PURCHASE ON LEDGER.ID = PURCHASE.ID_LEDGER "
                        + "WHERE LEDGER.ID_USER = ? ORDER BY PURCHASE.TIMESTAMP DESC",
                        userId
                    ).first(LedgerData.class);
                    if (ledger != null) {
                        return ledger;
                    }
                    return db.sql("SELECT * FROM LEDGER WHERE ID_USER = ?", userId)
                        .first(LedgerData.class);
                }

                @Override
                public List<PurchaseData> purchases(int ledgerId) {
                    return db.where("id_ledger=?", ledgerId).results(PurchaseData.class);
                }
            };
        }

        static ItemDAO getItemDAO(Database db) {
            return new ItemDAO() {
                @Override
                public Map<Integer, ItemData> itemMap() {
                    List<ItemData> items = db.results(ItemData.class);
                    Map<Integer, ItemData> map = new HashMap<>();
                    items.stream().forEach((item) -> {
                        map.put(item.id, item);
                    });
                    return map;
                }
            };
        }

        static Populator getPopulator(Database db) {
            return new Populator() {

                @Override
                public <T extends Populatable> T populate(T other) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public LedgerData populate(LedgerData ledger) {
                    List<PurchaseData> purchases = Access.data().ledgers.purchases(ledger.id);
                    ledger.getPurchases().addAll(purchases);
                    return ledger;
                }
            };
        }
    }
    
    private final Database db;

    public NormDatabaseAccess(Database db, UserDAO userDAO, LedgerDAO ledgerDAO, ItemDAO itemDAO, Populator populator) {
        super(userDAO, ledgerDAO, itemDAO, populator);
        this.db = db;
    }

    @Override
    protected void testConnection(int timeout) {
        try {
            if (!db.getConnection().isValid(timeout)) {
                throw new IllegalStateException("Connection to database is either closed or invalid");
            }
        } catch (SQLException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

}
