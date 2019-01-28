package data;

/**
 *
 * @author Walter Grönholm
 */
public interface Populator {
    
    <T extends Populatable> T populate(Populatable other);

    LedgerData populate(LedgerData ledger);
}
