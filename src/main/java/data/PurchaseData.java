package data;

import java.sql.Timestamp;
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
    @Column(name = "PRICE")
    public int total;
    
    @Transient
    public List<PurchaseData.Item> purchaseItems;
    
    public static class Item {
        public ItemData itemData;
        public int amount;
        public int price;
    }
}
