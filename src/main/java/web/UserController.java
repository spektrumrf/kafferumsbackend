package web;

import data.DataAccessObject;
import data.UserData;
import java.util.List;
import java.util.Set;
import json.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import security.PasswordUtils;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;
import spark.Spark;

/**
 * Handles User data requests
 *
 * @author Walter Grönholm
 */
class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private static final double INCREMENTAL_TIMEOUT = 1200.0;
    static final String USER = "user";

    static final Route getUserNames = (Request request, Response response) -> {
        List<String> userNames = DataAccessObject.getInstance().getUserNames();
        return JsonUtils.jsonResponse(userNames, List.class, response);
    };

    private static class LoginAttempt {

        public String pin;
        public String userName;
    }

    static final Route verifyPIN = (Request request, Response response) -> {
        LoginAttempt attempt = JsonUtils.getGson().fromJson(request.body(), LoginAttempt.class);

        String pin = attempt.pin;
        String userName = attempt.userName;

        //TODO: validate that the user exists
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

        if (success) {
            UserData userData = DataAccessObject.getInstance().getUserData(userName);
            request.session(true).attribute(USER, userData);
        }

        Thread.sleep((long) Math.floor(INCREMENTAL_TIMEOUT * failedAttempts));
        return JsonUtils.jsonResponse(success, Boolean.class, response);
    };

    static final Route logout = (Request request, Response response) -> {
        removeAllAttributes(request.session(true));
        return null;
    };

    private static void removeAllAttributes(Session session) {
        Set<String> attributes = session.attributes();
        for (String attribute : attributes) {
            session.removeAttribute(attribute);
        }
    }
    
    static final Filter verifyLoggedIn = (Request request, Response response) -> {
        UserData userData = (UserData) request.session(true).attribute(USER);
        if (userData == null) {
            Spark.halt(401, "You are not worthy!");
        }
    };

    static final Filter verifyAdmin = (Request request, Response response) -> {
        UserData userData = (UserData) request.session(true).attribute(USER);
        if (userData == null) {
            Spark.halt(401, "You are not worthy!");
        }
        boolean isAdmin = true; //TODO: check userData.groupId
        if (!isAdmin) {
            Spark.halt(401, "You are not worthy!");
        }
    };

}
