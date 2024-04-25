package view.main;

import javax.swing.*;

import view.main.states.AppState;
import view.main.states.StartState;

public class App extends JFrame{

    private static final ImageIcon appIcon = new ImageIcon("res/akropolis_logo2.png");
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
        int WIDTH = 1500;
        int HEIGHT = 700;
        screen.setPreferredSize(new java.awt.Dimension(WIDTH, HEIGHT));
        add(screen);
        pack();

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