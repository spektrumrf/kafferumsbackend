package web;

import data.Access;
import data.LedgerData;
import data.UserData;
import java.util.List;
import json.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import security.AuthenticationUtils;
import security.PasswordUtils;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Route;
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
        List<String> userNames = Access.getInstance().getUserNames();
        return JsonUtils.jsonResponse(userNames, List.class, response);
    };

    private static class LoginRequest {

        public String pin;
        public String userName;
    }

    private static class LoginResponse {

        private LoginResponse(boolean success, String token, Integer ledgerId) {
            this.success = success;
            this.token = token;
            this.ledgerId = ledgerId;
        }

        public boolean success;
        public String token;
        public Integer ledgerId;
    }

    static final Route verifyPIN = (Request request, Response response) -> {
        LoginRequest attempt = JsonUtils.GSON.fromJson(request.body(), LoginRequest.class);

        String pin = attempt.pin;
        String userName = attempt.userName;

        //TODO: validate that the user exists
        LOG.info("Verifying PIN for " + userName + " coming from " + request.ip());
        String storedPin = Access.getInstance().getPassword(userName);
        int failedAttempts = Access.getInstance().getLoginAttempts(userName); //todo minimize DB calls

        boolean success = PasswordUtils.verify(pin, storedPin);
        if (success) {
            failedAttempts = 0;
        } else {
            failedAttempts++;
        }

        Access.getInstance().setLoginAttempts(userName, failedAttempts);
        
        String token = success ? AuthenticationUtils.tokenize(userName) : null;
        LedgerData latestLedger = success ? Access.getInstance().getLatestLedger(userName) : null;
        Integer latestLedgerId = latestLedger != null ? latestLedger.id : null;

        Thread.sleep((long) Math.floor(INCREMENTAL_TIMEOUT * failedAttempts));
        
        LoginResponse loginResponse = new LoginResponse(success, token, latestLedgerId);
        return JsonUtils.jsonResponse(loginResponse, LoginResponse.class, response);
    };

    static final Filter verifyLoggedIn = (Request request, Response response) -> {
        String token = request.queryParams("token");
        AuthenticationUtils.verifyAndDetokenize(token);
    };

    static final Filter verifyAdmin = (Request request, Response response) -> {
        String token = request.queryParams("token");
        String userName = AuthenticationUtils.verifyAndDetokenize(token);
        UserData userData = Access.getInstance().getUserData(userName);
        if (!userData.isAdmin()) {
            LOG.info(request.ip() + " tried to access admin page without proper authorization");
            Spark.halt(401, "You are not worthy!");
        }
    };

}
