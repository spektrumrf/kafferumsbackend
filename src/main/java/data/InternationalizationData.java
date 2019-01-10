package data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Walter Grönholm
 */
@Table(name = "I18N")
public class InternationalizationData {

    private static final String SEPARATOR = "::";

    @Id
    @GeneratedValue
    @Column(name = "ID")
    public int id;
    @Column(name = "FINNISH")
    public String finnish;
    @Column(name = "SWEDISH")
    public String swedish;
    @Column(name = "ENGLISH")
    public String english;
    @Column(name = "KEY")
    public String key;

    @Override
    public String toString() {
        return key + SEPARATOR
            + finnish + SEPARATOR
            + swedish + SEPARATOR
            + english;
    }

}
