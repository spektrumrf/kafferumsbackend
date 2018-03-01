package web;


import config.Configuration;
import data.FormData;
import files.DataFileLoader;
import files.DataFileSaver;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import json.GsonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;
import static spark.Spark.halt;

/**
 *
 * @author Walter Grönholm
 */
class FormController {
    
    private static final Logger LOG = LoggerFactory.getLogger(FormController.class);

    static Route handlePost() {        
        return (Request request, Response response) -> {
            LOG.info("Recieved new message");
            Map<String, String[]> map = request.queryMap().toMap();
            FormData data = new FormData(map);
            List<FormData> allData = new DataFileLoader(Configuration.dataFilePath(), GsonFactory.getGson()).load();
            if (allData == null) {
                allData = new ArrayList<>();
                allData.add(data);
                new DataFileSaver(Configuration.dataFilePath()+'.'+System.currentTimeMillis(), GsonFactory.getGson()).save(allData);
            } else {
                allData.add(data);
                new DataFileSaver(Configuration.dataFilePath(), GsonFactory.getGson()).save(allData);
            }
            response.redirect("http://www.spektrum.fi/kafferummet");
            halt();
            return "";
        };
    }

}
