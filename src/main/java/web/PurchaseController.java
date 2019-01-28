package web;

import config.Configuration;
import data.DataAccessObject;
import data.ItemData;
import data.LedgerData;
import data.PurchaseData;
import data.UserData;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import json.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;
import static web.UserController.USER;

/**
 * Handles purchase POSTs.
 *
 * @author Walter Grönholm
 */
public class PurchaseController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private static class PurchaseJsonData {

        public int ledgerId;
        public List<PurchaseItemJsonData> items;
    }

    private static class PurchaseItemJsonData {

        public int itemId;
        public int amount;
    }

    private static enum PurchaseResult {
        SUCCESS,
        INVALID_LEDGER,
        INSUFFICIENT_BALANCE,
        UNKNOWN_ERROR
    }

    static Route purchase = (Request request, Response response) -> {
        PurchaseJsonData purchaseData = JsonUtils.getGson().fromJson(request.body(), PurchaseJsonData.class);

        PurchaseResult result = tryPurchasing((UserData) request.attribute(USER), purchaseData);
        return JsonUtils.jsonResponse(result, PurchaseResult.class, response);
    };

    private static PurchaseResult tryPurchasing(UserData userData, PurchaseJsonData purchaseJsonData) {
        LedgerData ledger = userData.getLedger(purchaseJsonData.ledgerId);
        if (ledger == null) {
            LOG.error("Ledger did not belong to user");
            return PurchaseResult.INVALID_LEDGER;
        }
        PurchaseData purchaseData = convertPurchaseData(purchaseJsonData);
        try {
            if (ledger.balance() - purchaseData.total > Configuration.ledgerMinimumBalance()) {
                authorizePurchase(ledger, purchaseData);
                LOG.info("Purchase success");
                return PurchaseResult.SUCCESS;
            } else {
                LOG.info("Insufficient balance");
                return PurchaseResult.INSUFFICIENT_BALANCE;
            }
        } catch (Exception ex) {
            LOG.error("Error trying to purchase items", ex);
            return PurchaseResult.UNKNOWN_ERROR;
        }
    }

    private static PurchaseData convertPurchaseData(PurchaseJsonData purchaseJsonData) {
        PurchaseData purchaseData = new PurchaseData();
        purchaseData.ledgerId = purchaseJsonData.ledgerId;
        purchaseData.timestamp = getCurrentTimestamp();
        Map<Integer, ItemData> itemIdMap = null;
        int purchaseTotal = 0;
        for (PurchaseItemJsonData itemJsonData : purchaseJsonData.items) {
            PurchaseData.Item item = new PurchaseData.Item();
            item.itemData = itemIdMap.get(itemJsonData.itemId);
            item.amount = itemJsonData.amount;
            item.price = item.itemData.price;
            purchaseData.addPurchaseItem(item);
            purchaseTotal += item.amount * item.price;
        }
        purchaseData.total = purchaseTotal;
        return purchaseData;
    }

    private static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    private static void authorizePurchase(LedgerData ledgerData, PurchaseData purchaseData) {
        DataAccessObject.getInstance().addPurchase(ledgerData.id, purchaseData);
    }

}
