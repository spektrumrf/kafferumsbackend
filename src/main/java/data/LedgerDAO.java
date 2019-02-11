package data;

import java.util.List;

/**
 *
 * @author Walter Grönholm
 */
public interface LedgerDAO {
    
    List<LedgerData> ofUser(int userId);

    LedgerData withId(int ledgerId);
    
    List<PurchaseData> purchases(int ledgerId);

    void addPurchase(int ledgerId, PurchaseData purchaseData);
    
    LedgerData latestLedgerOf(String userName);
}
