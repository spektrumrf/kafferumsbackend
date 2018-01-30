package elements;

import data.StatisticsData;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.watertemplate.Template;

/**
 *
 * @author Walter Grönholm
 */
public class StatisticsGraph extends Template {

    public StatisticsGraph(StatisticsData data) {
        Map<String, Color> colorMap = createColorMap(data.getKeys());
        List<Point> points = convertToPoints(data);
        addCollection("points", points);
    }

    private Map<String, Color> createColorMap(Set<String> keys) {
        Map<String, Color> colorMap = new HashMap<>();
        for (String key : keys) {
            colorMap.put(key, new Color(0, 0, 0));
        }
        return colorMap;
    }

    private List<Point> convertToPoints(StatisticsData data) {
        List<Point> points = new ArrayList<>();
        for (StatisticsData.Point point : data.getData()) {
            for (String key : data.getKeys()) {
                points.add(new Point(point.getTimestamp(), point.getInventory().get(key), key));
            }
        }
        return points;
    }

    @Override
    protected String getFilePath() {
        return "statistics-graph";
    }

    private static class Point {

        private final long x;
        private final int y;
        private final String key;

        public Point(long x, int y, String key) {
            this.x = x;
            this.y = y;
            this.key = key;
        }

    }

}
