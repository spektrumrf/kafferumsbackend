package web;

import config.Configuration;
import data.FormData;
import data.StatisticsData;
import data.StatisticsData.DataPoint;
import data.StatisticsDataLinearExtrapolator;
import elements.statistics.StatisticsPage;
import files.DataFileLoader;
import java.util.List;
import java.util.Map;
import json.GsonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;
import static spark.template.water.WaterTemplateEngine.waterEngine;
import static spark.template.water.WaterTemplateEngine.render;

/**
 *
 * @author Walter Grönholm
 */
class StatisticsController {

    private static final Logger LOG = LoggerFactory.getLogger(StatisticsController.class);

    static Route handleGet() {
        return (Request request, Response response) -> {
            LOG.debug("Returning statistics");
            List<FormData> inputData = new DataFileLoader(Configuration.dataFilePath(), GsonFactory.getGson()).load();
            if (inputData == null) {
                throw new NullPointerException("inputData is null");
            }
            StatisticsData statisticsData = new StatisticsData(inputData, new StatisticsDataLinearExtrapolator());
            return waterEngine().render(render(new StatisticsPage(statisticsData), request));
        };
    }

    private static String tempReturn(StatisticsData statisticsData) {
        StringBuilder sb = new StringBuilder();
        sb.append("<ul>");
        for (DataPoint point : statisticsData.getData()) {
            sb.append("<li> Time: ").append(point.getTimestamp()).append("</li><ul>");
            for (Map.Entry<String, Integer> entry : point.getInventory().entrySet()) {
                sb.append("<li>").append(entry.getKey()).append(": ").append(entry.getValue())
                    .append("</li>");
            }
            sb.append("</ul>");
        }
        sb.append("</ul>");
        return sb.toString();
    }
}
