package web;

import config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Filter;
import spark.Route;
import static spark.Spark.path;
import static spark.Spark.staticFileLocation;
import static spark.Spark.before;
import static spark.Spark.port;
import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.post;
import static spark.Spark.before;
import static spark.Spark.port;
import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.post;
import static spark.Spark.before;
import static spark.Spark.port;
import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.post;
import static spark.Spark.before;
import static spark.Spark.port;
import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.post;

/**
 *
 * @author Walter Grönholm
 */
public class Router implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(Router.class);

    private static final Route approveJsonPost = (request, response) -> {
        String origin = request.headers("ORIGIN");
        response.header("Access-Control-Allow-Origin", origin);
        response.header("Access-Control-Allow-Methods", "POST");
        response.header("Access-Control-Allow-Headers", "Accept, Content-Type");
        response.header("Access-Control-Max-Age", "1728000");
        return "true";
    };
    
    private static final Filter logRequest = (request, response) -> {
        LOG.info("Received " + request.requestMethod() + " call to " + request.uri());
    };

    @Override
    public void run() {
        staticFileLocation("/public");
        port(Configuration.port());
        post(Configuration.inventoryFormPath(), InventoryFormController.handlePost());

        before("/*", logRequest);
        path("/api", () -> {
            before("/user/*", UserController.verifyLoggedIn);
            path("/user", () -> {
                post("/purchase", PurchaseController.purchase);
                get("/ledgers", LedgerController.getUserLedgers);
                get("/ledger", LedgerController.getLedger);
            });
            before("/admin/*", UserController.verifyAdmin);
            path("/admin", () -> {
                //TODO add stuff here
            });
            get("/names", UserController.getUserNames);
            post("/pin", UserController.verifyPIN);
        });
        options("/*", approveJsonPost);

        LOG.info("Break room listener is listening on :" + Configuration.port());
    }

}
