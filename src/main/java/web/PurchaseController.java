package web;

import config.Configuration;
import data.Access;
import data.ItemData;
import data.LedgerData;
import data.PurchaseData;
import data.PurchaseItemData;
import data.UserData;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import json.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import security.AuthenticationUtils;
import spark.Request;
import spark.Response;
import spark.Route;
import validation.Validate;

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
        PurchaseJsonData purchaseData = JsonUtils.GSON.fromJson(request.body(), PurchaseJsonData.class);
        String token = request.queryParams("token");
        String userName = AuthenticationUtils.verifyAndDetokenize(token);
        UserData userData = Access.getInstance().getUserData(userName);
        
        PurchaseResult result = tryPurchasing(userData, purchaseData);
        return JsonUtils.jsonResponse(result, PurchaseResult.class, response);
    };

    private static PurchaseResult tryPurchasing(UserData userData, PurchaseJsonData purchaseJsonData) {
        Validate.notNull("userData", userData);
        Validate.notNull("purchaseJsonData", purchaseJsonData);
        LedgerData ledger = Access.getInstance().getLedger(purchaseJsonData.ledgerId);
        if (ledger == null) {
            LOG.error("Could not find ledger with id " + purchaseJsonData.ledgerId);
            return PurchaseResult.INVALID_LEDGER;
        }
        if (ledger.userId != userData.id) {
            LOG.error("Ledger did not belong to the user");
            return PurchaseResult.INVALID_LEDGER;
        }
        PurchaseData purchaseData = convertPurchaseData(purchaseJsonData);
        try {
            ledger = ledger.populated();
            if (- (ledger.balance() + purchaseData.total) > Configuration.ledgerMinimumBalance()) {
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
        Map<Integer, ItemData> itemIdMap = Access.getInstance().getItemIdMap(); //call from db
        int purchaseTotal = 0;
        for (PurchaseItemJsonData itemJsonData : purchaseJsonData.items) {
            PurchaseItemData item = new PurchaseItemData();
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
        Access.getInstance().addPurchase(ledgerData.id, purchaseData);
    }

}
