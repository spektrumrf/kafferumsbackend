package data;

import java.util.List;

/**
 *
 * @author Walter Grönholm
 */
interface DataExtrapolator<T> {

    List<T> extrapolate(Iterable<String> keys, List<T> data);

}
