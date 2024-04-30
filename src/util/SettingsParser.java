package util;

import java.io.IOException;
import java.util.Properties;
import java.io.InputStream;

/**
 * This class is used to parse the settings file
 */
public class SettingsParser {
    private static Properties properties;

    static {
        properties = new Properties();
        try {
            InputStream in = SettingsParser.class.getResourceAsStream("/save/settings.ini");
            properties.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getResolutionWidth() {
        return Integer.parseInt(properties.getProperty("RESOLUTION_WIDTH"));
    }

    public static int getResolutionHeight() {
        return Integer.parseInt(properties.getProperty("RESOLUTION_HEIGHT"));
    }
}
