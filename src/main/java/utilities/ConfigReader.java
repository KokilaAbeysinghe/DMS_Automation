package utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads key/value pairs from config.properties on the classpath
 * (src/test/resources/config.properties gets copied there by Maven).
 * Classpath loading works regardless of the IDE/run configuration's
 * working directory, unlike a relative file path.
 */
public class ConfigReader {

    private static Properties properties;
    private static final String CONFIG_FILE = "config.properties";

    private static void loadProperties() {
        if (properties == null) {
            properties = new Properties();
            try (InputStream is = ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
                if (is == null) {
                    throw new RuntimeException(
                            "Unable to find " + CONFIG_FILE + " on the classpath. " +
                                    "Confirm it exists at src/test/resources/" + CONFIG_FILE +
                                    " and that the project has been rebuilt."
                    );
                }
                properties.load(is);
            } catch (IOException e) {
                throw new RuntimeException("Unable to load " + CONFIG_FILE, e);
            }
        }
    }

    public static String get(String key) {
        loadProperties();
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Property '" + key + "' not found in " + CONFIG_FILE);
        }
        return value;
    }

    public static String get(String key, String defaultValue) {
        loadProperties();
        return properties.getProperty(key, defaultValue);
    }
}