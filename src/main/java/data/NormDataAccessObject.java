package data;

import com.dieselpoint.norm.Transaction;
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
    public void addPurchase(int ledgerId, PurchaseData purchaseData) {
        Transaction transation = db.startTransaction();
        db.transaction(transation).insert(purchaseData).execute();
        for (PurchaseData.Item item : purchaseData.purchaseItems) {
            db.transaction(transation).sql(
                "INSERT INTO PURCHASEITEM VALUES (?, ?, ?, ?)",
                purchaseData.id,
                item.itemData.id,
                item.amount,
                item.price
            ).execute();
        }
        transation.commit();
    }

}
