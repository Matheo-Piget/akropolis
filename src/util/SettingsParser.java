package util;

import java.io.IOException;
import java.util.Properties;
import java.io.InputStream;
import java.io.FileOutputStream;

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

    public static int[] getResolution() {
        return new int[]{getResolutionWidth(), getResolutionHeight()};
    }

    private static int changeResolutionWidth(int width) {
        properties.setProperty("RESOLUTION_WIDTH", Integer.toString(width));
        return width;
    }

    private static int changeResolutionHeight(int height) {
        properties.setProperty("RESOLUTION_HEIGHT", Integer.toString(height));
        return height;
    }

    public void changeResolution(int width, int height) {
        changeResolutionWidth(width);
        changeResolutionHeight(height);
        save(); // Save the new resolution
    }

    private static void save() {
        try {
            properties.store(new FileOutputStream("./res/save/settings.ini"), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
