package data;

import java.util.List;

/**
 *
 * @author Walter Grönholm
 */
public interface LedgerDAO {
    
    List<LedgerData> ledgers(int userId);

    LedgerData ledger(int ledgerId);
    
    List<PurchaseData> purchases(int ledgerId);

    void addPurchase(int ledgerId, PurchaseData purchaseData);
    
    LedgerData latestLedger(String userName);
}
