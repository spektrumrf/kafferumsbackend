package data;

import java.util.Map;

/**
 *
 * @author Walter Grönholm
 */
public interface ItemDAO {

    /**
     * @return all items found in database, in an ID --> DATA map.
     */
    Map<Integer, ItemData> getItemIdMap();
}
