package data;

import java.util.ArrayList;
import java.util.Collections;
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
    private final List<Point> data;

    public StatisticsData(Iterable<FormData> inputData) {
        List<Point> tempData = convertToDataPoints(inputData);
        this.keys = getAllKeys(tempData);
        Collections.sort(tempData);
        data = new StatisticsDataExtrapolator().extrapolate(keys, tempData);
    }

    private List<Point> convertToDataPoints(Iterable<FormData> inputData) {
        List<Point> data = new ArrayList<>();
        for (FormData formData : inputData) {
            data.add(new Point(formData.time, formData.inventory));
        }
        return data;
    }

    private Set<String> getAllKeys(List<Point> data) {
        Set<String> keys = new HashSet();
        for (Point dataPoint : data) {
            keys.addAll(dataPoint.inventory.keySet());
        }
        return keys;
    }

    public Set<String> getKeys() {
        return keys;
    }

    public List<Point> getData() {
        return data;
    }
    
    public static class Point implements Comparable<Point> {

        final long timestamp;
        final Map<String, Integer> inventory;

        Point(long timestamp, Map<String, Integer> inventory) {
            this.timestamp = timestamp;
            this.inventory = inventory;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public Map<String, Integer> getInventory() {
            return inventory;
        }

        @Override
        public int compareTo(Point o) {
            if (o.timestamp - timestamp == 0) {
                return 0;
            }
            return o.timestamp - timestamp > 0 ? -1 : 1;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 47 * hash + (int) (this.timestamp ^ (this.timestamp >>> 32));
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
            final Point other = (Point) obj;
            if (this.timestamp != other.timestamp) {
                return false;
            }
            if (!Objects.equals(this.inventory, other.inventory)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "Point{" + "timestamp=" + timestamp + ", inventory=" + inventory + '}';
        }
    }
}
