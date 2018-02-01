package elements.statistics;

import com.google.gson.Gson;
import data.StatisticsData;
import elements.Page;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import json.GsonFactory;

/**
 *
 * @author Walter Grönholm
 */
public class StatisticsPage extends Page {

    public StatisticsPage(StatisticsData data) {
        List<DataSet> dataSets = DataSet.from(data);
        add("dataSets", toJson(dataSets));
    }

    private String toJson(List<DataSet> dataSets) {
        Gson gson = GsonFactory.getGson();
        String json = gson.toJson(dataSets);
        return json;
    }

    @Override
    protected String getFilePath() {
        return "statistics.html";
    }

}
