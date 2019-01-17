
package data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Markus Andersson
 */
@Table(name = "LOGIN")
public class LoginData {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    public int id;
    @Column(name = "TYPE")
    public int type;
    @Column(name = "ATTEMPTS")
    public int attempts;
    @Column(name = "DATA")
    public String data;
    
    
}
