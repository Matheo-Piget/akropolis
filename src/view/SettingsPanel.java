package view;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import model.LeaderBoard;
import javax.swing.ImageIcon;
import java.util.Arrays;
import java.util.Objects;
import util.SettingsParser;
import util.SoundManager;
import view.main.App;
import view.ui.UIFactory;

/**
 * In this panel, the user can change the settings of the game.
 * We can change the window size here.
 */
public class SettingsPanel extends JLayeredPane {
    private JComboBox<String> windowSize = new JComboBox<>();
    private final JLabel backGroundLabel = new JLabel();

    /**
     * Constructor for the SettingsPanel class.
     * Initializes the spinner.
     */
    public SettingsPanel(CardLayout switcher) {
        loadBackgroundImage();
        add(backGroundLabel, Integer.valueOf(0));
        int userWindowWidth = SettingsParser.getResolutionWidth();
        int userWindowHeight = SettingsParser.getResolutionHeight();
        backGroundLabel.setBounds(0, 0, userWindowWidth, userWindowHeight);
        // Handle the case where the user has a custom window size which is not in the
        // array we add it to the array
        String userWindowSize = userWindowWidth + "x" + userWindowHeight;
        String[] windowSizes = {"1200x560", "1350x630", "1500x700", "1650x770", "1800x840"};
        if (!Arrays.asList(windowSizes).contains(userWindowSize)) {
            String[] newWindowSizes = new String[windowSizes.length + 1];
            System.arraycopy(windowSizes, 0, newWindowSizes, 0, windowSizes.length);
            newWindowSizes[windowSizes.length] = userWindowSize;
            windowSizes = newWindowSizes;
        }
        JPanel leaderBoardPanel = new JPanel();
        // It will have an arrow to display each leaderboard category
        leaderBoardPanel.setLayout(null);
        leaderBoardPanel.setBounds(0, 0, userWindowWidth, userWindowHeight);
        windowSize = new JComboBox<>(windowSizes);
        windowSize.setBounds(50, 100, 200, 30);
        windowSize.setSelectedItem(userWindowSize);
        windowSize.addActionListener(e -> {
            String[] size = Objects.requireNonNull(windowSize.getSelectedItem()).toString().split("x");
            SettingsParser.changeResolution(Integer.parseInt(size[0]), Integer.parseInt(size[1]));
            App.getInstance().getScreen()
                    .setPreferredSize(new Dimension(Integer.parseInt(size[0]), Integer.parseInt(size[1])));
            App.getInstance().pack();
            App.getInstance().revalidate();
            // Recreate the background image
            loadBackgroundImage();
        });
        JLabel windowSizeLabel = new JLabel("Résolution de l'écran:");
        windowSizeLabel.setForeground(java.awt.Color.WHITE);
        windowSizeLabel.setBounds(50, 50, 200, 30);
        JButton leaderBoardResetButton = UIFactory.createStyledButton("Réinitialiser les scores",
                e -> {
                    LeaderBoard leaderBoard = new LeaderBoard();
                    leaderBoard.resetLeaderBoard();
                });
        leaderBoardResetButton.setBounds(50, 200, 300, 30);
        JButton backButton = UIFactory.createStyledButton("Retour", e -> switcher.show(getParent(), "choicePanel"));
        backButton.setBounds(50, 250, 300, 30);
        backButton.addActionListener(e -> switcher.show(getParent(), "choicePanel"));
        JButton soundToggleButton = UIFactory.createStyledButton("", e -> toggleSound());
        String soundButtonText = SoundManager.isSoundEnabled() ? "Désactiver le son" : "Activer le son";
        soundToggleButton.setText(soundButtonText);
        soundToggleButton.setBounds(50, 150, 300, 30);
        add(soundToggleButton, Integer.valueOf(1));
        add(windowSizeLabel, Integer.valueOf(1));
        add(windowSize, Integer.valueOf(1));
        add(leaderBoardResetButton, Integer.valueOf(1));
        add(backButton, Integer.valueOf(1));
    }

    /**
     * Loads the background image of the settings panel.
     */
    private void loadBackgroundImage() {
        try {
            ImageIcon imageIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/menu/settingsbg.gif")));
            Image image = imageIcon.getImage();
            Dimension screenSize = App.getInstance().getScreen().getPreferredSize();
            Image scaledImage = image.getScaledInstance(screenSize.width, screenSize.height, Image.SCALE_DEFAULT);
            backGroundLabel.setIcon(new ImageIcon(scaledImage));
            backGroundLabel.setSize(screenSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the appearance of the sound button based on the sound state.
     */
    private void updateSoundButtonAppearance() {
        Component[] components = getComponents();
        for (Component component : components) {
            if (component instanceof JButton button) {
                if (SoundManager.isSoundEnabled() && button.getText().equals("Activer le son")){
                    button.setText("Désactiver le son");
                    break;
                }
                else if (!SoundManager.isSoundEnabled() && button.getText().equals("Désactiver le son")){
                    button.setText("Activer le son");
                }
            }
        }
    }

    /**
     * Toggles the sound on and off.
     */
    private void toggleSound() {
        SoundManager.invertSoundEnabled();
        if (SoundManager.isSoundEnabled()) {
            SoundManager.loopSound("menu");

        } else {
            SoundManager.stopAllSounds();
        }
        // Mettez à jour l'apparence du bouton en fonction de l'état du son
        updateSoundButtonAppearance();
    }
}
