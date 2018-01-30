package config;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.ini4j.Ini;

/**
 *
 * @author Walter Grönholm
 */
public class Configuration {

    private static Configuration CONFIG;

    private final Ini ini = new Ini();
    private static final Set<TypedEntry> CONFIG_ENTRIES = getEntrySet();

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

    void validateIni(Ini ini, Set<TypedEntry> typedEntries) {
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
    
    public static String dataFilePath() {
        return CONFIG.ini.get("data", "file");
    }

    public static int port() {
        return CONFIG.ini.get("webapp", "port", int.class);
    }

    public static String postPath() {
        return CONFIG.ini.get("webapp", "postPath");
    }
    
    public static String statisticsPath() {
        return CONFIG.ini.get("webapp", "statisticsPath");
    }

    public static int maxFormMapSize() {
        return CONFIG.ini.get("form", "maxSize", int.class);
    }

    public static int feedbackTextWarningSize() {
        return CONFIG.ini.get("form", "feedbackWarningSize", int.class);
    }

    public static int feedbackTextMaxSize() {
        return CONFIG.ini.get("form", "feedbackMaxSize", int.class);
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

    private static Set<TypedEntry> getEntrySet() {
        Set<TypedEntry> entrySet = new HashSet<>();
        entrySet.add(new TypedEntry("data", "file", String.class));
        entrySet.add(new TypedEntry("webapp", "port", int.class));
        entrySet.add(new TypedEntry("webapp", "postPath", String.class));
        entrySet.add(new TypedEntry("webapp", "statisticsPath", String.class));
        entrySet.add(new TypedEntry("form", "maxSize", int.class));
        entrySet.add(new TypedEntry("form", "feedbackWarningSize", int.class));
        entrySet.add(new TypedEntry("form", "feedbackMaxSize", int.class));
        return entrySet;
    }

}
