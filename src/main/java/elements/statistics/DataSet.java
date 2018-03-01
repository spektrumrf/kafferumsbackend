package elements.statistics;

import data.StatisticsData;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class must follow the Json structure of Chart.js dataSets
 * 
 * @author Walter Grönholm
 */
public class DataSet {

    /**
     * Graph line name.
     */
    final String label;
    /**
     * Graph line color.
     */
    final String borderColor;
    /**
     * Graph data points.
     */
    final List<Point> data;
    final boolean hidden;

    DataSet(String label, Color borderColor, List<Point> data, boolean hidden) {
        this.label = label;
        this.borderColor = toHexString(borderColor);
        this.data = data;
        this.hidden = hidden;
    }

    private static String toHexString(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    static List<DataSet> from(StatisticsData statisticsData) {
        List<DataSet> dataSets = new ArrayList<>();
        Map<String, Color> colorMap = createColorMap(statisticsData.getKeys());
        Map<String, List<Point>> pointsMap = convertToPointsMap(statisticsData);
        for (String key : statisticsData.getKeys()) {
            List<Point> data = pointsMap.get(key);
            dataSets.add(new DataSet(
                key,
                colorMap.get(key),
                data,
                data.isEmpty() || data.get(data.size() - 1).y > 0));
        }
        return dataSets;
    }

    private static Map<String, Color> createColorMap(Set<String> keys) {
        Map<String, Color> colorMap = new HashMap<>();
        for (String key : keys) {
            colorMap.put(key, new Color(0, 0, 0));
        }
        return colorMap;
    }

    private static Map<String, List<Point>> convertToPointsMap(StatisticsData data) {
        Map<String, List<Point>> map = new HashMap<>();
        for (String key : data.getKeys()) {
            List<Point> points = new ArrayList<>();
            for (StatisticsData.DataPoint point : data.getData()) {
                if (point.getInventory().get(key) != null) {
                    points.add(new Point(point.getTimestamp(), point.getInventory().get(key)));
                }
            }
            map.put(key, points);
        }
        return map;
    }

    private static class Point {

        private final long x;
        private final int y;

        private Point(long x, int y) {
            this.x = x;
            this.y = y;
        }

    }
}
