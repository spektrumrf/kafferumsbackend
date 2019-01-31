package web;

import config.Configuration;
import data.InventoryFormData;
import files.DataFileLoader;
import files.DataFileSaver;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import json.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;
import static spark.Spark.halt;

/**
 * Handles Inventory update forms
 * 
 * @author Walter Grönholm
 */
class InventoryFormController {
    
    private static final Logger LOG = LoggerFactory.getLogger(InventoryFormController.class);

    static Route handlePost() {        
        return (Request request, Response response) -> {
            LOG.info("Recieved new message");
            Map<String, String[]> map = request.queryMap().toMap();
            InventoryFormData data = new InventoryFormData(map);
            List<InventoryFormData> allData = new DataFileLoader(Configuration.dataFilePath(), JsonUtils.GSON).load();
            if (allData == null) {
                allData = new ArrayList<>();
                allData.add(data);
                new DataFileSaver(Configuration.dataFilePath()+'.'+System.currentTimeMillis(), JsonUtils.GSON).save(allData);
            } else {
                allData.add(data);
                new DataFileSaver(Configuration.dataFilePath(), JsonUtils.GSON).save(allData);
            }
            response.redirect("http://www.spektrum.fi/kafferummet");
            halt();
            return "";
        };
    }

}
