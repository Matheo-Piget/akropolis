package util;

import java.io.IOException;
import java.util.Properties;
import java.io.InputStream;
import java.io.FileOutputStream;

/**
 * This class is used to parse the settings file
 */
public class SettingsParser {
    private static final Properties properties;

    static {
        properties = new Properties();
        try {
            InputStream in = SettingsParser.class.getResourceAsStream("/save/settings.ini");
            properties.load(in);
            assert in != null;
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to get the resolution width
     * @return the resolution width
     */
    public static int getResolutionWidth() {
        return Integer.parseInt(properties.getProperty("RESOLUTION_WIDTH"));
    }

    /**
     * This method is used to get the resolution height
     * @return the resolution height
     */
    public static int getResolutionHeight() {
        return Integer.parseInt(properties.getProperty("RESOLUTION_HEIGHT"));
    }

    /**
     * This method is used to get the resolution
     * @return the resolution
     */
    public static int[] getResolution() {
        return new int[]{getResolutionWidth(), getResolutionHeight()};
    }

    /**
     * This method is used to get the volume
     * @return the volume
     */
    private static int changeResolutionWidth(int width) {
        properties.setProperty("RESOLUTION_WIDTH", Integer.toString(width));
        return width;
    }

    /**
     * This method is used to get the volume
     * @return the volume
     */
    private static int changeResolutionHeight(int height) {
        properties.setProperty("RESOLUTION_HEIGHT", Integer.toString(height));
        return height;
    }

    /**
     * This method is used to get the volume
     */
    public static void changeResolution(int width, int height) {
        changeResolutionWidth(width);
        changeResolutionHeight(height);
        save(); // Save the new resolution
    }

    public static boolean soundEnabled() {
        return Boolean.parseBoolean(properties.getProperty("SOUND_ENABLED"));
    }

    public static void setSoundEnabled(boolean enabled) {
        properties.setProperty("SOUND_ENABLED", Boolean.toString(enabled));
        save();
    }

    /**
     * This method is used to get the volume
     */
    private static void save() {
        try {
            properties.store(new FileOutputStream("./res/save/settings.ini"), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
