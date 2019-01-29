package data;

import java.util.List;

/**
 *
 * @author Walter Grönholm
 */
public interface LedgerDAO {
    
    List<LedgerData> getLedgers(int userId);

    LedgerData getLedger(int ledgerId);
    
    List<PurchaseData> getPurchases(int ledgerId);

    void addPurchase(int ledgerId, PurchaseData purchaseData);
    
    LedgerData getLatestLedger(String userName);
}
