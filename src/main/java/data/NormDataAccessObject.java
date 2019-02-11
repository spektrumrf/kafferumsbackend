package data;

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
public class NormDataAccessObject extends Access {

    private final com.dieselpoint.norm.Database db;

    public NormDataAccessObject(String url, String user, String pass) {
        db = new com.dieselpoint.norm.Database();
        db.setJdbcUrl(url);
        db.setUser(user);
        db.setPassword(pass);
        db.setDriverClassName("org.h2.Driver");
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

    @Override
    public List<String> getUserNames() {
        return db.sql("SELECT NAME FROM USER").results(String.class);
    }

    @Override
    public String getPassword(String userName) {
        return db.sql("SELECT LOGIN.DATA FROM LOGIN JOIN USER ON LOGIN.ID = USER.ID_LOGIN WHERE USER.NAME = ?", userName).first(String.class);
    }

    @Override
    public int getLoginAttempts(String userName) {
        return db.sql("SELECT LOGIN.ATTEMPTS FROM LOGIN JOIN USER ON LOGIN.ID = USER.ID_LOGIN WHERE USER.NAME = ?", userName).first(int.class);
    }

    @Override
    public void setLoginAttempts(String userName, int failedAttempts) {
        db.sql("UPDATE LOGIN SET ATTEMPTS = ? WHERE ID = (SELECT ID_LOGIN FROM USER WHERE NAME = ?)", failedAttempts, userName).execute();
    }

    @Override
    public UserData getUserData(String userName) {
        return db.where("name=?", userName).first(UserData.class);
    }

    @Override
    public UserData getUserData(int userId) {
        return db.where("id=?", userId).first(UserData.class);
    }

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
    public LedgerData getLedger(int ledgerId) {
        return db.where("id=?", ledgerId).first(LedgerData.class);
    }

    @Override
    public List<LedgerData> getLedgers(int userId) {
        return db.where("id_user=?", userId).results(LedgerData.class);
    }
    
    @Override
    public LedgerData getLatestLedger(String userName) {
        UserData user = getUserData(userName);
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
    public List<PurchaseData> getPurchases(int ledgerId) {
        return db.where("id_ledger=?", ledgerId).results(PurchaseData.class);
    }

    @Override
    public <T extends Populatable> T populate(T other) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public LedgerData populate(LedgerData ledger) {
        List<PurchaseData> purchases = Access.getInstance().getPurchases(ledger.id);
        ledger.getPurchases().addAll(purchases);
        return ledger;
    }

    @Override
    public Map<Integer, ItemData> getItemIdMap() {
        List<ItemData> items = db.results(ItemData.class);
        Map<Integer, ItemData> map = new HashMap<>();
        items.stream().forEach((item) -> {
            map.put(item.id, item);
        });
        return map;
    }
}
