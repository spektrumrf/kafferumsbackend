package web;

import data.DataAccessObject;
import java.util.List;
import java.util.Map;
import json.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import security.PasswordUtils;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Handles User data requests
 * 
 * @author Walter Grönholm
 */
class UserController {
    
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    static Route getUserNames() {        
        return (Request request, Response response) -> {
            LOG.info("getUserNames");
            List<String> userNames = DataAccessObject.getInstance().getUserNames();
            return JsonUtils.jsonResponse(userNames, List.class, response);
        };
    }

    static Route verifyPIN() {        
        return (Request request, Response response) -> {
            LOG.info("verifyPIN");
            Map<String, String[]> map = request.queryMap().toMap();
            String pin = map.get("pin")[0];
            String userName = map.get("userName")[0];
            String storedPin = DataAccessObject.getInstance().getPassword(userName);
            return JsonUtils.jsonResponse(PasswordUtils.verify(pin, storedPin), Boolean.class, response);
        };
    }

}
