package files;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import data.InventoryFormData;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Walter Grönholm
 */
public class DataFileLoader {

    private static final Logger LOG = LoggerFactory.getLogger(DataFileLoader.class);
    private final Gson gson;
    private final String fileName;

    public DataFileLoader(String fileName, Gson gson) {
        this.gson = gson;
        this.fileName = fileName;
    }

    public List<InventoryFormData> load() {
        File file = new File(fileName);
        if (file.isFile()) {
            return getFormDataFromFile(file);
        }
        return null;
    }

    private List<InventoryFormData> getFormDataFromFile(File file) {
        try (JsonReader reader = new JsonReader(new FileReader(file));) {
            reader.setLenient(true);
            return gson.fromJson(reader, new TypeToken<List<InventoryFormData>>() {
            }.getType());
        } catch (IOException | JsonSyntaxException ex) {
            LOG.warn("Could not load FormData from file " + file.getName(), ex);
            return null;
        }
    }
}
