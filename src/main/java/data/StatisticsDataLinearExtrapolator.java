package data;

import data.StatisticsData.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Walter Grönholm
 */
public class StatisticsDataLinearExtrapolator implements DataExtrapolator<StatisticsData.Point> {

    @Override
    public List<StatisticsData.Point> extrapolate(Iterable<String> keys, List<StatisticsData.Point> data) {
        List<StatisticsData.Point> rv = new ArrayList<>();
        for (StatisticsData.Point point : data) {
            rv.add(new Point(point.time, new HashMap(point.inventory)));
        }
        for (String key : keys) {
            long previousPreviousTimestamp = 0l;
            long previousTimestamp = 0l;
            int previousPreviousAmount = 0;
            int previousAmount = 0;
            for (StatisticsData.Point dataPoint : rv) {
                if (!dataPoint.inventory.containsKey(key)) {
                    Integer extrapolated = extrapolate(dataPoint.time, previousTimestamp, previousPreviousTimestamp,
                        previousAmount, previousPreviousAmount);
                    dataPoint.inventory.put(key, extrapolated);
                } else {
                    int amount = dataPoint.inventory.get(key);
                    if (amount > previousAmount) {
                        long timeDiff = previousTimestamp - previousPreviousTimestamp;
                        int amountDiff = previousAmount - previousPreviousAmount;
                        previousPreviousAmount = amount - amountDiff;
                        previousPreviousTimestamp = dataPoint.time - timeDiff;
                        previousAmount = amount;
                        previousTimestamp = dataPoint.time;
                    } else {
                        previousPreviousAmount = previousAmount;
                        previousPreviousTimestamp = previousTimestamp;
                        previousAmount = dataPoint.inventory.get(key);
                        previousTimestamp = dataPoint.time;
                    }

                }
            }
        }
        return rv;
    }

    private Integer extrapolate(long timestamp, long previousTimestamp, long previousPreviousTimestamp, int previousAmount, int previousPreviousAmount) {
        if (previousTimestamp - previousPreviousTimestamp == 0) {
            return previousAmount;
        }
        return Math.max(0, previousAmount + Math.round(((timestamp - previousTimestamp) * (previousAmount - previousPreviousAmount)) / (previousTimestamp - previousPreviousTimestamp)));
    }
}
