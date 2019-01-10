package files;

import com.google.gson.Gson;
import data.InventoryFormData;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Walter Grönholm
 */
public class DataFileSaver {

    private static final Logger LOG = LoggerFactory.getLogger(DataFileSaver.class);
    private final Gson gson;
    private final String fileName;

    public DataFileSaver(String fileName, Gson gson) {
        this.gson = gson;
        this.fileName = fileName;
    }

    public boolean save(List<InventoryFormData> data) {
        File file = new File(fileName);
        String json = gson.toJson(data);
        LOG.info("Appending FormData to file");
        if (!appendStringToFile(json, file)) {
            LOG.warn("Appending FormData to console");
            appendToConsole(json);
            return false;
        }
        return true;
    }

    private boolean appendStringToFile(String data, File file) {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8")) {
            writer.append(data);
            writer.write(System.lineSeparator());
            return true;
        } catch (IOException ex) {
            LOG.error("Exception while appending to file " + file.getName(), ex);
            return false;
        }
    }

    private void appendToConsole(String json) {
        try (PrintWriter systemOutWriter = new PrintWriter(System.out)) {
            systemOutWriter.println(json);
        }
    }
}
