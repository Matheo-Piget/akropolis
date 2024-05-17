package view.main;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import util.SettingsParser;
import util.SoundManager;
import view.main.states.AppState;
import view.main.states.StartState;

/** 
 * Represents the main class of the game.
 * This class is a singleton and is used to create the main window of the game. 
 */
public class App extends JFrame{

    private static final ImageIcon appIcon = new ImageIcon("res/menu/akropolis_logo2.png");
    private final JPanel screen = new JPanel();
    private static final App INSTANCE = new App();

    public AppState appState;
    
    public App() {
        screen.setLayout(new java.awt.BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Akropolis");
        setResizable(false);
        
        getContentPane().setBackground(java.awt.Color.BLACK);
        screen.setBackground(java.awt.Color.WHITE);
        int WIDTH = SettingsParser.getResolutionWidth();
        int HEIGHT = SettingsParser.getResolutionHeight();
        screen.setPreferredSize(new java.awt.Dimension(WIDTH, HEIGHT));
        add(screen);
        pack();

        SoundManager.loadSound("menu", "/sound/Akropolis.wav");
        SoundManager.loadSound("gameButton", "/sound/ButtonSound.wav");
        SoundManager.loadSound("gameButton2", "/sound/GameButton.wav");
        SoundManager.loadSound("tilePlaced", "/sound/tilePlaceSound.wav");

        setIconImage(appIcon.getImage());

        setVisible(true);
    }

    public static App getInstance() {
        return INSTANCE;
    }

    public JPanel getScreen() {
        return screen;
    }

    public void run() {
        appState = AppState.LOGO;
        appState.getState().enter();
    }
    public void exitToMainMenu() {
        // Logique pour revenir au menu principal
        appState.changeState(StartState.getInstance());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            App app = App.getInstance();
            app.run();
        });
    }
}