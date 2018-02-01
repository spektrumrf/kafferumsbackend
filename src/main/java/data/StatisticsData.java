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
    private final List<Point> data;

    public StatisticsData(Iterable<FormData> inputData, DataExtrapolator<StatisticsData.Point> extrapolator) {
        List<Point> tempData = convertToDataPoints(inputData);
        tempData.add(new Point(normalizeMillis(System.currentTimeMillis()), new HashMap<>()));
        this.keys = getAllKeys(tempData);
        Collections.sort(tempData);
        data = extrapolator.extrapolate(keys, tempData);
    }

    private List<Point> convertToDataPoints(Iterable<FormData> inputData) {
        List<Point> data = new ArrayList<>();
        for (FormData formData : inputData) {
            data.add(new Point(normalizeMillis(formData.time), formData.inventory));
        } // how to cut/use min? maybe enter interval -> cut
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

    private int normalizeMillis(long time) {
        return (int) ((time - 1500000000000l) / 60000 - 272595);
    }

    public static class Point implements Comparable<Point> {

        final int time;
        final Map<String, Integer> inventory;

        Point(int timestamp, Map<String, Integer> inventory) {
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
        public int compareTo(Point o) {
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
            final Point other = (Point) obj;
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
