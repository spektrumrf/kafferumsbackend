package web;

import config.Configuration;
import data.FormData;
import data.StatisticsData;
import data.StatisticsData.Point;
import files.DataFileLoader;
import java.util.List;
import java.util.Map;
import json.GsonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

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
            StatisticsData statisticsData = new StatisticsData(inputData);
//            StatisticsGraph graph = new StatisticsGraph(statisticsData);
            StringBuilder sb = new StringBuilder();
            sb.append("<ul>");
            for (Point point : statisticsData.getData()) {
                sb.append("<li> Time: ").append(point.getTimestamp()).append("</li><li><ul>");
                for (Map.Entry<String, Integer> entry : point.getInventory().entrySet()) {
                    sb.append("<li>").append(entry.getKey()).append(": ").append(entry.getValue())
                        .append("</li>");
                }
                sb.append("</ul></li>");
            }
            sb.append("</ul>");
            return sb.toString();
//            return waterEngine().render(render(new StatisticsPage(graph), request));
        };
    }
}
