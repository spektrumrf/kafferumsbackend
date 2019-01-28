package data;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author Walter Grönholm
 */
@Table(name = "PURCHASEITEM")
public class PurchaseItemData {
    @Column(name = "ID_PURCHASE")
    public int purchaseId;
    @Column(name = "ID_ITEM")
    public int itemId;
    @Column(name = "AMOUNT")
    public int amount;
    @Column(name = "PRICE")
    public int price;
    
    @Transient
    public ItemData itemData;
}
