package data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Walter Grönholm
 */
public class StatisticsDataLinearExtrapolatorTest {
    
    public StatisticsDataLinearExtrapolatorTest() {
    }
    
    @Test
    public void testExtrapolate() {
        System.out.println("testExtrapolate");
        
        Iterable<String> keys = Arrays.asList(
            new String[]{"some_thing", "testing", "reduce 1 per 1000", "reduce 1 per 1000 with increase", "new", "no_data"});
        List<StatisticsData.DataPoint> data = new ArrayList<>();
        data.add(new StatisticsData.DataPoint(1000, testExtrapolate_firstPointMap()));
        data.add(new StatisticsData.DataPoint(2000, testExtrapolate_secondPointMap()));
        data.add(new StatisticsData.DataPoint(3000, testExtrapolate_thirdPointMap()));
        data.add(new StatisticsData.DataPoint(10000, testExtrapolate_lastPointMap()));
        StatisticsDataLinearExtrapolator instance = new StatisticsDataLinearExtrapolator();
        List<StatisticsData.DataPoint> expectedResult = new ArrayList<>();
        expectedResult.add(new StatisticsData.DataPoint(1000, testExtrapolate_expected_firstPointMap()));
        expectedResult.add(new StatisticsData.DataPoint(2000, testExtrapolate_expected_secondPointMap()));
        expectedResult.add(new StatisticsData.DataPoint(3000, testExtrapolate_expected_thirdPointMap()));
        expectedResult.add(new StatisticsData.DataPoint(10000, testExtrapolate_expected_lastPointMap()));
        List<StatisticsData.DataPoint> result = instance.extrapolate(keys, data);
        StringBuilder errorMessage = new StringBuilder();
        assertEquals("The result size is different", expectedResult.size(), result.size());
        for (int i = 0; i < expectedResult.size(); i++) {
            StatisticsData.DataPoint expectedPoint = expectedResult.get(i);
            StatisticsData.DataPoint resultedPoint = result.get(i);
            if (!expectedPoint.equals(resultedPoint)) {
                errorMessage.append("\n").append(resultedPoint).append("\n does not equal expected point\n").append(expectedPoint);
            }
        }
        String errorMessageString = errorMessage.toString();
        if (!errorMessageString.isEmpty()) {
            fail(errorMessageString);
        }
    }

    private Map<String, Integer> testExtrapolate_firstPointMap() {
        Map<String, Integer> rv = new HashMap<>();
        rv.put("some_thing", 12);
        rv.put("ignored", 0);
        rv.put("testing", 1);
        rv.put("reduce 1 per 1000", 100);
        rv.put("reduce 1 per 1000 with increase", 100);
        return rv;
    }

    private Map<String, Integer> testExtrapolate_secondPointMap() {
        Map<String, Integer> rv = new HashMap<>();
        rv.put("new", 1);
        rv.put("reduce 1 per 1000", 99);
        rv.put("reduce 1 per 1000 with increase", 99);
        return rv;
    }

    private Map<String, Integer> testExtrapolate_thirdPointMap() {
        Map<String, Integer> rv = new HashMap<>();
        rv.put("some_thing", 6);
        rv.put("new", 10);
        rv.put("reduce 1 per 1000 with increase", 198);
        return rv;
    }

    private Map<String, Integer> testExtrapolate_lastPointMap() {
        Map<String, Integer> rv = new HashMap<>();
        rv.put("testing", 0);
        return rv;
    }
    
    private Map<String, Integer> testExtrapolate_expected_firstPointMap() {
        Map<String, Integer> rv = new HashMap<>();
        rv.put("some_thing", 12);
        rv.put("ignored", 0);
        rv.put("no_data", 0);
        rv.put("testing", 1);
        rv.put("reduce 1 per 1000", 100);
        rv.put("reduce 1 per 1000 with increase", 100);
        rv.put("new", 0);
        return rv;
    }

    private Map<String, Integer> testExtrapolate_expected_secondPointMap() {
        Map<String, Integer> rv = new HashMap<>();
        rv.put("some_thing", 12);
        rv.put("no_data", 0);
        rv.put("testing", 1);
        rv.put("reduce 1 per 1000", 99);
        rv.put("reduce 1 per 1000 with increase", 99);
        rv.put("new", 1);
        return rv;
    }

    private Map<String, Integer> testExtrapolate_expected_thirdPointMap() {
        Map<String, Integer> rv = new HashMap<>();
        rv.put("some_thing", 6);
        rv.put("no_data", 0);
        rv.put("testing", 1);
        rv.put("reduce 1 per 1000", 98);
        rv.put("reduce 1 per 1000 with increase", 198);
        rv.put("new", 10);
        return rv;
    }

    private Map<String, Integer> testExtrapolate_expected_lastPointMap() {
        Map<String, Integer> rv = new HashMap<>();
        rv.put("some_thing", 0);
        rv.put("no_data", 0);
        rv.put("testing", 0);
        rv.put("reduce 1 per 1000", 91);
        rv.put("reduce 1 per 1000 with increase", 191);
        rv.put("new", 10);
        return rv;
    }
}
