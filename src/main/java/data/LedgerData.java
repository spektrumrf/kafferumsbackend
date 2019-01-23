package data;

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
@Table(name = "LEDGER")
public class LedgerData {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    public int id;
    @Column(name = "ID_USER")
    public int userId;
    
    @Transient
    public List<PurchaseData> purchases;
    
    public int balance() {
        return purchases.stream().map((purchase) -> purchase.total).reduce(0, Integer::sum);
    }
}
