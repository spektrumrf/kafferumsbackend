package data;

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
@Table(name = "LEDGER")
public class LedgerData extends Populatable {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    public int id;
    @Column(name = "ID_USER")
    public int userId;
    
    private final List<PurchaseData> purchases = new ArrayList<>();

    @Transient
    public List<PurchaseData> getPurchases() {
        return purchases;
    }
    
    public int balance() {
        return getPurchases().stream().map((purchase) -> purchase.total).reduce(0, Integer::sum);
    }

    @Override
    public LedgerData populated() {
        return populator().populate(this);
    }
    
}
