
package data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Markus Andersson
 */
@Table(name = "USERGROUP")
public class UserGroupData {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    public int id;
    @Column(name = "ID_I18N")
    public int i18nId;
}
