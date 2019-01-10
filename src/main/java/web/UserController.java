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

    private static final double INCREMENTAL_TIMEOUT = 800.0;
    
    static Route getUserNames() {        
        return (Request request, Response response) -> {
            LOG.info("getUserNames");
            List<String> userNames = DataAccessObject.getInstance().getUserNames();
            return JsonUtils.jsonResponse(userNames, List.class, response);
        };
    }
    
    static Route verifyPIN() {        
        return (Request request, Response response) -> {
            Map<String, String[]> map = request.queryMap().toMap();
            String pin = map.get("pin")[0];
            String userName = map.get("userName")[0];
            
            LOG.info("verifyPIN for " + userName + " coming from " + request.ip());
            String storedPin = DataAccessObject.getInstance().getPassword(userName);
            int failedAttempts = DataAccessObject.getInstance().getLoginAttempts(userName); //todo minimize DB calls
            
            Boolean success = PasswordUtils.verify(pin, storedPin);
            if (success) {
                failedAttempts = 0;
            } else {
                failedAttempts++;
            }

            DataAccessObject.getInstance().setLoginAttempts(userName, failedAttempts);

            Thread.sleep((long) Math.floor(INCREMENTAL_TIMEOUT * failedAttempts));
            return JsonUtils.jsonResponse(success, Boolean.class, response);
        };
    }

}
