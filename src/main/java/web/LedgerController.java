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
        UserData userData = AuthenticationUtils.verifyAndDetokenize(token);
        List<LedgerData> data = DataAccessObject.getInstance().getLedgers(userData.id);
        String string = "";
        for (LedgerData ledgerData : data) {
            string += ledgerData.id + ", ";
        }
        System.out.println(string);
        return JsonUtils.jsonResponse(data, LedgerListType, response);
    };

    static final Route getLedger = (Request request, Response response) -> {
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
