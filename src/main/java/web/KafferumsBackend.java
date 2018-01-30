package web;

import config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

/**
 *
 * @author Walter Grönholm
 */
public class KafferumsBackend implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(KafferumsBackend.class);
    
    @Override
    public void run() {
        port(Configuration.port());
        get(Configuration.statisticsPath(), StatisticsController.handleGet());
        post(Configuration.postPath(), FormController.handlePost());

        LOG.info("Break room listener is listening on path :" + Configuration.port() + Configuration.postPath());
    }

}
