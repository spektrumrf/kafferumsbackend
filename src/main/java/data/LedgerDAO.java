package data;

/**
 *
 * @author Walter Grönholm
 */
public interface LedgerDAO {
    
    void addPurchase(int ledgerId, PurchaseData purchaseData);
}
