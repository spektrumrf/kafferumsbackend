package web;

import com.google.gson.reflect.TypeToken;
import data.Access;
import data.LedgerData;
import data.PurchaseData;
import data.UserData;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import meta.Pair;
import json.JsonUtils;
import security.AuthenticationUtils;
import spark.Request;
import spark.Response;
import spark.Route;
import static spark.Spark.halt;
import static spark.Spark.halt;

/**
 * Handles ledger history GETs.
 *
 * @author Walter Grönholm
 */
public class LedgerController {

    static final Type LedgerListType = new TypeToken<List<LedgerData>>() {
    }.getType();

    static final Route getUserLedgers = (Request request, Response response) -> {
        String token = request.queryParams("token");
        String userName = AuthenticationUtils.verifyAndDetokenize(token);
        UserData userData = Access.getInstance().getUserData(userName);
        List<LedgerData> data = Access.getInstance().getLedgers(userData.id);
        return JsonUtils.jsonResponse(data, LedgerListType, response);
    };

    private static class LedgerDataResponse {

        private List<Pair<String, Integer>> rows = new ArrayList<>();

        private static LedgerDataResponse from(LedgerData ledger) {
            List<PurchaseData> purchases = new ArrayList<>(ledger.getPurchases());
            purchases.sort((PurchaseData p1, PurchaseData p2) -> -(p1.timestamp.compareTo(p2.timestamp)));

            LedgerDataResponse response = new LedgerDataResponse();

            int previousDay = -1;
            int dayTotal = 0;
            Timestamp timestamp = null;
            for (PurchaseData purchase : purchases) {
                timestamp = purchase.timestamp;
                int day = getDay(purchase.timestamp);
                if (day != previousDay) {
                    response.rows.add(Pair.of(formatDate(timestamp), dayTotal));
                    previousDay = day;
                    dayTotal = 0;
                }
                dayTotal += purchase.total;
            }
            if (timestamp != null)
                response.rows.add(Pair.of(formatDate(timestamp), dayTotal));
            if (!response.rows.isEmpty())
                response.rows.remove(0);
            return response;
        }

    }

    private static int getDay(Timestamp timestamp) {
        return timestamp.toLocalDateTime().getDayOfYear();
    }

    private static String formatDate(Timestamp timestamp) {
        return DateFormat.getDateInstance().format(timestamp);
    }

    static final Route getLedger = (Request request, Response response) -> {
        String token = request.queryParams("token");
        String userName = AuthenticationUtils.verifyAndDetokenize(token);
        UserData userData = Access.getInstance().getUserData(userName);
        int ledgerId = Integer.parseInt(request.queryParams("id"));
        LedgerData ledger = Access.getInstance().getLedger(ledgerId);
        if (userData.id != ledger.userId && !userData.isAdmin()) {
            halt(401, "Access denied");
        }
        ledger = ledger.populated();
        LedgerDataResponse ledgerResponse = LedgerDataResponse.from(ledger);
        return JsonUtils.jsonResponse(ledgerResponse, LedgerDataResponse.class, response);
    };
}
