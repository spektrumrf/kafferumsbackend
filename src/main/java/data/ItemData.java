package data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Walter Grönholm
 */
@Table(name = "ITEM")
public class ItemData {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    public int id;
    @Column(name = "ID_I18N")
    public int i18nId;
    @Column(name = "PRICE")
    public int price;
    @Column(name = "GTIN13")
    public String gtin13;
}
