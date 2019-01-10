package web;

import config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static spark.Spark.port;
import static spark.Spark.staticFileLocation;
import static spark.Spark.get;
import static spark.Spark.post;

/**
 *
 * @author Walter Grönholm
 */
public class Router implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(Router.class);
    
    @Override
    public void run() {
        staticFileLocation("/public");
        port(Configuration.port());
        post(Configuration.inventoryFormPath(), InventoryFormController.handlePost());
        
        //data
        get("/user/names", UserController.getUserNames());
        post("/user/pin", UserController.verifyPIN());

        LOG.info("Break room listener is listening on :" + Configuration.port());
    }

}
