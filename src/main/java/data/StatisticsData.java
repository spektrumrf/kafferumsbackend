package data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Walter Grönholm
 */
public class StatisticsData {

    private final Set<String> keys;
    private final List<DataPoint> data;

    public StatisticsData(Iterable<InventoryFormData> inputData, DataExtrapolator<String, StatisticsData.DataPoint> extrapolator) {
        List<DataPoint> tempData = convertToDataPoints(inputData);
        tempData.add(new DataPoint(normalizeMillis(System.currentTimeMillis()), new HashMap<>()));
        this.keys = getAllKeys(tempData);
        Collections.sort(tempData);
        data = extrapolator.extrapolate(keys, tempData);
    }
    
    //TODO, separate real data from extrapolated data (show it differently)

    private List<DataPoint> convertToDataPoints(Iterable<InventoryFormData> inputData) {
        List<DataPoint> data = new ArrayList<>();
        for (InventoryFormData formData : inputData) {
            data.add(new DataPoint(normalizeMillis(formData.time), formData.inventory));
        } // how to cut/use min? maybe enter interval -> cut
        return data;
    }

    private Set<String> getAllKeys(List<DataPoint> data) {
        Set<String> keys = new HashSet();
        for (DataPoint dataPoint : data) {
            keys.addAll(dataPoint.inventory.keySet());
        }
        return keys;
    }

    public Set<String> getKeys() {
        return keys;
    }

    public List<DataPoint> getData() {
        return data;
    }

    private long normalizeMillis(long time) {
        return (time - 1516355721029l) / (1000 * 60 * 60);
    }

    public static class DataPoint implements Comparable<DataPoint> {

        final long time;
        final Map<String, Integer> inventory;

        DataPoint(long timestamp, Map<String, Integer> inventory) {
            this.time = timestamp;
            this.inventory = inventory;
        }

        public long getTimestamp() {
            return time;
        }

        public Map<String, Integer> getInventory() {
            return inventory;
        }

        @Override
        public int compareTo(DataPoint o) {
            if (o.time - time == 0) {
                return 0;
            }
            return o.time - time > 0 ? -1 : 1;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 47 * hash + (int) (this.time ^ (this.time >>> 32));
            hash = 47 * hash + Objects.hashCode(this.inventory);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final DataPoint other = (DataPoint) obj;
            if (this.time != other.time) {
                return false;
            }
            if (!Objects.equals(this.inventory, other.inventory)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "Point{" + "timestamp=" + time + ", inventory=" + inventory + '}';
        }
    }
}
