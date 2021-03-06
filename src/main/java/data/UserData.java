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

    @Transient
    public boolean isAdmin() {
        return groupId == 2;
    }

    @Override
    public String toString() {
        return "UserData{" + "id=" + id + ", name=" + name + ", groupId=" + groupId + ", loginId=" + loginId + '}';
    }
}
