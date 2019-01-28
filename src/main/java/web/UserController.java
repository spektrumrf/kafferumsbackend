package web;

import data.DataAccessObject;
import data.UserData;
import java.util.List;
import java.util.Set;
import json.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import security.AuthenticationUtils;
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

    static final Route getUserNames = (Request request, Response response) -> {
        List<String> userNames = DataAccessObject.getInstance().getUserNames();
        return JsonUtils.jsonResponse(userNames, List.class, response);
    };

    private static class LoginRequest {

        public String pin;
        public String userName;
    }

    private static class LoginResponse {

        private LoginResponse(boolean success, String token) {
            this.success = success;
            this.token = token;
        }

        public boolean success;
        public String token;
    }

    static final Route verifyPIN = (Request request, Response response) -> {
        LoginRequest attempt = JsonUtils.getGson().fromJson(request.body(), LoginRequest.class);

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

        String token = null;
        if (success) {
            token = AuthenticationUtils.tokenize(userName);
        } else {
            Thread.sleep((long) Math.floor(INCREMENTAL_TIMEOUT * failedAttempts));
        }
        LoginResponse loginResponse = new LoginResponse(success, token);
        return JsonUtils.jsonResponse(loginResponse, LoginResponse.class, response);
    };

    static final Route logout = (Request request, Response response) -> {
        removeAllAttributes(request.session());
        return null;
    };

    private static void removeAllAttributes(Session session) {
        Set<String> attributes = session.attributes();
        for (String attribute : attributes) {
            session.removeAttribute(attribute);
        }
    }

    static final Filter verifyLoggedIn = (Request request, Response response) -> {
        String token = request.queryParams("token");
        AuthenticationUtils.verifyAndDetokenize(token);
    };

    static final Filter verifyAdmin = (Request request, Response response) -> {
        String token = request.queryParams("token");
        UserData userData = AuthenticationUtils.verifyAndDetokenize(token);
        if (!userData.isAdmin()) {
            LOG.info(request.ip() + " tried to access admin page without proper authorization");
            Spark.halt(401, "You are not worthy!");
        }
    };

}
