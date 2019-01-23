package web;

import config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Route;
import spark.Spark;
import static spark.Spark.path;
import static spark.Spark.before;
import static spark.Spark.port;
import static spark.Spark.staticFileLocation;
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
        response.header("Access-Control-Allow-Headers", "accept, content-type");
        response.header("Access-Control-Max-Age", "1728000");
        return "true";
    };

    @Override
    public void run() {
        staticFileLocation("/public");
        port(Configuration.port());
        post(Configuration.inventoryFormPath(), InventoryFormController.handlePost());

        before("/*", (req, res) -> LOG.info("Received " + req.requestMethod() + " call to " + req.uri()));
        path("/api", () -> {
            before("/user/*", UserController.verifyLoggedIn);
            path("/user", () -> {
                get("/logout", UserController.logout);
                options("/purchase", approveJsonPost);
                post("/purchase", PurchaseController.purchase);
                //get history
            });
            before("/admin/*", UserController.verifyAdmin);
            path("/admin", () -> {
                //TODO add stuff here
            });
            get("/names", UserController.getUserNames);
            options("/pin", approveJsonPost);
            post("/pin", UserController.verifyPIN);
        });

        LOG.info("Break room listener is listening on :" + Configuration.port());
    }

}
