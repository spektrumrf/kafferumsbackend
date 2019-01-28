package data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author Walter Grönholm
 */
@Table(name = "PURCHASE")
public class PurchaseData {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    public int id;
    @Column(name = "ID_LEDGER")
    public int ledgerId;
    @Column(name = "TIMESTAMP")
    public Timestamp timestamp;
    @Column(name = "TOTAL")
    public int total;
    
    @Transient
    private List<PurchaseItemData> purchaseItems = new ArrayList<>();

    Iterable<PurchaseItemData> getPurchaseItems() {
        return purchaseItems;
    }

    public void addPurchaseItem(PurchaseItemData item) {
        purchaseItems.add(item);
    }
}
