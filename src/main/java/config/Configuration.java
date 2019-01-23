package config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.ini4j.Ini;

/**
 *
 * @author Walter Grönholm
 */
public class Configuration {

    private static Configuration CONFIG;

    private final Ini ini = new Ini();
    private static final List<TypedEntry> CONFIG_ENTRIES = getEntrySet();

    public static void initialize(String filePath) throws IOException {
        initialize(new Configuration(), filePath);
    }

    public static void initialize(Configuration config, String filePath) throws IOException {
        CONFIG = config;
        CONFIG.readFile(new File(filePath));
    }

    private void readFile(File file) throws IOException {
        ini.load(file);
        validateIni(ini, CONFIG_ENTRIES);
    }

    void validateIni(Ini ini, Iterable<TypedEntry> typedEntries) {
        String erronousFields = "";
        for (TypedEntry typedEntry : typedEntries) {
            if (!typedEntry.validate(ini)) {
                if (!erronousFields.isEmpty()) {
                    erronousFields += ", ";
                }
                erronousFields += typedEntry.section + '/' + typedEntry.key;
            }
        }
        if (!erronousFields.isEmpty()) {
            throw new IllegalArgumentException("The following fields are of wrong type or missing: " + erronousFields);
        }
    }
    
    //Sections
    private static final String DATABASE = "database";
    private static final String WEBAPP = "webapp";
    private static final String FORM = "form";
    private static final String LEDGER = "ledger";
    
    public static String dataFilePath() {
        return CONFIG.ini.get(FORM, "file");
    }
    
    public static String databaseUrl() {
        return CONFIG.ini.get(DATABASE, "url", String.class);
    }
    
    public static String databaseUser() {
        return CONFIG.ini.get(DATABASE, "user", String.class);
    }
    
    public static String databasePass() {
        return CONFIG.ini.get(DATABASE, "pass", String.class);
    }

    public static int databaseTimeout() {
        return CONFIG.ini.get(DATABASE, "timeoutMillis", int.class);
    }

    public static int port() {
        return CONFIG.ini.get(WEBAPP, "port", int.class);
    }

    public static String inventoryFormPath() {
        return CONFIG.ini.get(WEBAPP, "inventoryFormPath");
    }
    
    public static String statisticsGraphPath() {
        return CONFIG.ini.get(WEBAPP, "statisticsGraphPath");
    }

    public static int maxFormMapSize() {
        return CONFIG.ini.get(FORM, "maxSize", int.class);
    }

    public static int feedbackTextWarningSize() {
        return CONFIG.ini.get(FORM, "feedbackWarningSize", int.class);
    }

    public static int feedbackTextMaxSize() {
        return CONFIG.ini.get(FORM, "feedbackMaxSize", int.class);
    }
    
    public static int ledgerMinimumBalance() {
        return CONFIG.ini.get(LEDGER, "minimumBalanceCents", int.class);
    }

    private static class TypedEntry<T> {

        private final String section;
        private final String key;
        private final Class<T> valueType;

        private TypedEntry(String section, String key, Class<T> valueType) {
            this.section = section;
            this.key = key;
            this.valueType = valueType;
        }

        private boolean validate(Ini ini) {
            return ini.get(section, key, valueType) != null;
        }

    }

    private static List<TypedEntry> getEntrySet() {
        List<TypedEntry> entrySet = new ArrayList<>();
        entrySet.add(new TypedEntry(FORM, "file", String.class));
        entrySet.add(new TypedEntry(DATABASE, "url", String.class));
        entrySet.add(new TypedEntry(DATABASE, "user", String.class));
        entrySet.add(new TypedEntry(DATABASE, "pass", String.class));
        entrySet.add(new TypedEntry(DATABASE, "timeoutMillis", int.class));
        entrySet.add(new TypedEntry(WEBAPP, "port", int.class));
        entrySet.add(new TypedEntry(WEBAPP, "inventoryFormPath", String.class));
        entrySet.add(new TypedEntry(WEBAPP, "statisticsGraphPath", String.class));
        entrySet.add(new TypedEntry(FORM, "maxSize", int.class));
        entrySet.add(new TypedEntry(FORM, "feedbackWarningSize", int.class));
        entrySet.add(new TypedEntry(FORM, "feedbackMaxSize", int.class));
        entrySet.add(new TypedEntry(LEDGER, "minimumBalanceCents", int.class));
        return entrySet;
    }

}
