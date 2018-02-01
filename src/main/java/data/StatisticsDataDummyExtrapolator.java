package data;

import java.util.List;

/**
 *
 * @author Walter Grönholm
 */
public class StatisticsDataDummyExtrapolator implements DataExtrapolator<StatisticsData.Point> {

    @Override
    public List<StatisticsData.Point> extrapolate(Iterable<String> keys, List<StatisticsData.Point> data) {
        return data;
    }

}
