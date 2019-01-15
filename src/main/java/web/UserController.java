package web;

import data.DataAccessObject;
import java.util.List;
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

    private static final double INCREMENTAL_TIMEOUT = 1200.0;

    static Route getUserNames = (Request request, Response response) -> {
        List<String> userNames = DataAccessObject.getInstance().getUserNames();
        return JsonUtils.jsonResponse(userNames, List.class, response);
    };
    
    private static class LoginAttempt {
        public String pin;
        public String userName;
    }

    static Route verifyPIN = (Request request, Response response) -> {
        LoginAttempt attempt = JsonUtils.getGson().fromJson(request.body(), LoginAttempt.class);

        String pin = attempt.pin;
        String userName = attempt.userName;
        
        //TODO: validate user name

        LOG.info("Verifying PIN for " + userName + " coming from " + request.ip());
        String storedPin = DataAccessObject.getInstance().getPassword(userName);
        int failedAttempts = DataAccessObject.getInstance().getLoginAttempts(userName); //todo minimize DB calls

        boolean success = PasswordUtils.verify(pin, storedPin);
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
