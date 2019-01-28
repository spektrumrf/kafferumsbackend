package data;

import java.util.List;

/**
 *
 * @author Walter Grönholm
 */
public interface LedgerDAO {

    LedgerData getLedger(int ledgerId);
    
    List<PurchaseData> getPurchases(int ledgerId);

    void addPurchase(int ledgerId, PurchaseData purchaseData);
}
