package data;

import java.util.Map;

/**
 *
 * @author Walter Grönholm
 */
public interface ItemDAO {

    Map<Integer, ItemData> getItemIdMap();
}
