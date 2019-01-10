package data;

import config.Configuration;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Walter Grönholm
 */
public class InventoryFormData implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(InventoryFormData.class);

    final long time;
    final String feedback;
    final Map<String, Integer> inventory;

    public InventoryFormData(Map<String, String[]> formData) {
        this.time = System.currentTimeMillis();
        String[] get = formData.get("text_feedback");
        this.feedback = get == null || get.length == 0
            ? ""
            : trim(get[0]);
        this.inventory = transform(formData);
    }

    private String trim(String get) {
        get = get.trim();
        get = get.replace('"', '\'').replace('|', '/');
        get = get.replaceAll("\n(\n)+", "\n");
        if (get.length() >= Configuration.feedbackTextWarningSize()) {
            LOG.warn("Long feedback string of {} characters!", get.length());
        }
        if (get.length() > Configuration.feedbackTextMaxSize()) {
            LOG.warn("Cut to {} characters", 300);
            return get.substring(0, 300 - 1) + "...";
        }
        return get;
    }

    private Map<String, Integer> transform(Map<String, String[]> formData) {
        Map<String, Integer> map = new TreeMap<>();
        if (formData.size() >= Configuration.maxFormMapSize()) {
            LOG.warn("Form data map is too large! Map size: {}", formData.size());
            return map;
        }
        for (Map.Entry<String, String[]> entry : formData.entrySet()) {
            if (entry.getKey() == null || entry.getKey().startsWith("text_") || !entry.getKey().contains("_")) {
                continue;
            }
            if (entry.getValue() != null && entry.getValue().length > 0) {
                String value = entry.getValue()[0];
                if (value != null && value.length() > 0) {
                    try {
                        map.put(entry.getKey(), Integer.parseInt(value));
                    } catch (NumberFormatException ex) {
                        LOG.warn("NumberFormatException for value {}", value);
                    }
                }
            }
        }
        return map;
    }

}
