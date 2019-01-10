package web;

import config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFileLocation;
import static spark.Spark.get;
import static spark.Spark.post;

/**
 *
 * @author Walter Grönholm
 */
public class KafferumsBackend implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(KafferumsBackend.class);
    
    @Override
    public void run() {
        staticFileLocation("/public");
        port(Configuration.port());
        post(Configuration.inventoryPostPath(), InventoryFormController.handlePost());
        post(Configuration.inventoryPostPath(), InventoryFormController.handlePost());

        LOG.info("Break room listener is listening on path :" + Configuration.port() + Configuration.inventoryPostPath());
    }

}
