package data;

import java.util.List;

/**
 *
 * @author Walter Grönholm
 */
public class StatisticsDataDummyExtrapolator implements DataExtrapolator<String, StatisticsData.DataPoint> {

    @Override
    public List<StatisticsData.DataPoint> extrapolate(Iterable<String> keys, List<StatisticsData.DataPoint> data) {
        return data;
    }

}
