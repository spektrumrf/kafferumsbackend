
package data;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Markus Andersson
 */
@Table(name = "USER")
public class UserData {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    public int id;
    @Column(name = "NAME")
    public String name;
    @Column(name = "ID_USERGROUP")
    public int groupId;
    @Column(name = "ID_LOGIN")
    public int loginId;

    @Override
    public String toString() {
        return "UserData{" + "id=" + id + ", name=" + name + ", groupId=" + groupId + ", loginId=" + loginId + '}';
    }
    
}
