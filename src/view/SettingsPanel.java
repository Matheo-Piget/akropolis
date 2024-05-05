package view;

import javax.swing.JPanel;
import java.util.Arrays;
import javax.swing.JSpinner;
import util.SettingsParser;

/**
 * In this panel, the user can change the settings of the game.
 * We can change the window size here.
 */
public class SettingsPanel extends JPanel {
    private JSpinner spinner = new JSpinner();
    private String [] WindowSizes = {"800x600", "1024x768", "1500x700", "1280x1024", "1600x900", "1920x1080"};

    /**
     * Constructor for the SettingsPanel class.
     * Initializes the spinner.
     */
    public SettingsPanel() {
        int userWindowWidth = SettingsParser.getResolutionWidth();
        int userWindowHeight = SettingsParser.getResolutionHeight();
        // Handle the case where the user has a custom window size which is not in the array we add it to the array
        String userWindowSize = userWindowWidth + "x" + userWindowHeight;
        if (!Arrays.asList(WindowSizes).contains(userWindowSize)) {
            String[] newWindowSizes = new String[WindowSizes.length + 1];
            System.arraycopy(WindowSizes, 0, newWindowSizes, 0, WindowSizes.length);
            newWindowSizes[WindowSizes.length] = userWindowSize;
            WindowSizes = newWindowSizes;
        }
        spinner.setModel(new javax.swing.SpinnerListModel(WindowSizes));
        spinner.setValue(userWindowSize);
        add(spinner);
    }
}
