package data;

import java.util.List;

/**
 *
 * @author Walter Grönholm
 */
interface DataExtrapolator<K, V> {

    /**
     * Extrapolates the given data list to a new, fuller data list.
     * @param keys The keys of the data items which should be extrapolated.
     * @param data §
     * @return A new list with more or as much data as given
     */
    List<V> extrapolate(Iterable<K> keys, List<V> data);

}
