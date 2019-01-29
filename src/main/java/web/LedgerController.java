package web;

import com.google.gson.reflect.TypeToken;
import data.DataAccessObject;
import data.LedgerData;
import data.UserData;
import java.lang.reflect.Type;
import java.util.List;
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
    static final Type LedgerListType = new TypeToken<List<LedgerData>>() {}.getType();
    
    static final Route getUserLedgers = (Request request, Response response) -> {
        String token = request.queryParams("token");
        String userName = AuthenticationUtils.verifyAndDetokenize(token);
        UserData userData = DataAccessObject.getInstance().getUserData(userName);
        List<LedgerData> data = DataAccessObject.getInstance().getLedgers(userData.id);
        return JsonUtils.jsonResponse(data, LedgerListType, response);
    };

    static final Route getLedger = (Request request, Response response) -> {
        String token = request.queryParams("token");
        String userName = AuthenticationUtils.verifyAndDetokenize(token);
        UserData userData = DataAccessObject.getInstance().getUserData(userName);
        int ledgerId = Integer.parseInt(request.queryParams("id"));
        LedgerData ledger = DataAccessObject.getInstance().getLedger(ledgerId);
        if (userData.id != ledger.userId && !userData.isAdmin()) {
            halt(401, "Access denied");
        }
        ledger = ledger.populated();
        return JsonUtils.jsonResponse(ledger, LedgerData.class, response);
    };
}
