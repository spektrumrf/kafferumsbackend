package web;

import config.Configuration;
import data.InventoryFormData;
import data.StatisticsData;
import data.StatisticsDataLinearExtrapolator;
import files.DataFileLoader;
import java.util.List;
import json.JsonUtils;
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
            List<InventoryFormData> inputData = new DataFileLoader(Configuration.dataFilePath(), JsonUtils.GSON).load();
            if (inputData == null) {
                throw new NullPointerException("inputData is null");
            }
            StatisticsData statisticsData = new StatisticsData(inputData, new StatisticsDataLinearExtrapolator());
            return JsonUtils.jsonResponse(statisticsData, StatisticsData.class, response);
        };
    }
    
}
