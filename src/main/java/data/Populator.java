package data;

/**
 * An interface for instances or classes, which are able to fill missing
 * <b>fields</b>
 * from a data object. This class should not be used to fill gaps in data, or do
 * any inter- or extrapolation.
 *
 * @author Walter Grönholm
 */
public interface Populator {

    /**
     * Attempts to populate a generic {@link Populatable} object.
     *
     * @throws UnsupportedOperationException if generic {@link Populatable}
     * objects can not be filled.
     */
    <T extends Populatable> T populate(T other);

    /**
     * Adds {@link PurchaseData} to the given ledger.
     * 
     * @return the given ledger for easy chaining
     */
    LedgerData populate(LedgerData ledger);
}
