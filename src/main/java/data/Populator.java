package data;

/**
 *
 * @author Walter Grönholm
 */
public interface Populator {
    
    <T extends Populatable> T populate(T other);

    LedgerData populate(LedgerData ledger);
}
