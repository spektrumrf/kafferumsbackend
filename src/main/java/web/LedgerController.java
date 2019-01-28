package web;

import data.DataAccessObject;
import data.LedgerData;
import data.UserData;
import json.JsonUtils;
import security.AuthenticationUtils;
import spark.Request;
import spark.Response;
import spark.Route;
import static spark.Spark.halt;

/**
 * Handles ledger history GETs.
 *
 * @author Walter Grönholm
 */
public class LedgerController {

    static final Route get = (Request request, Response response) -> {
        String token = request.queryParams("token");
        UserData userData = AuthenticationUtils.verifyAndDetokenize(token);
        int ledgerId = Integer.parseInt(request.queryParams("id"));
        LedgerData data = DataAccessObject.getInstance().getLedger(ledgerId);
        if (userData.id != data.userId && !userData.isAdmin()) {
            halt(401, "Access denied");
        }
        data = data.populated();
        return JsonUtils.jsonResponse(data, LedgerData.class, response);
    };
}
