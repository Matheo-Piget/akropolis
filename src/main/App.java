package main;

import javax.swing.JFrame;
import javax.swing.JPanel;
import states.AppState;

public class App extends JFrame{
    private final int WIDTH = 700;
    private final int HEIGHT = 700;

    private JPanel screen = new JPanel();
    private static final App INSTANCE = new App();

    public AppState appState;
    
    public App() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setResizable(false);
        
        getContentPane().setBackground(java.awt.Color.BLACK);
        screen.setBackground(java.awt.Color.WHITE);
        screen.setPreferredSize(new java.awt.Dimension(WIDTH, HEIGHT));
        add(screen);
        pack();

        setVisible(true);
    }

    public static App getInstance() {
        return INSTANCE;
    }

    public void run() {
        appState = AppState.START;
        appState.getState().enter();
    }

    public static void main(String[] args) {
        App app = App.getInstance();
        app.run();
    }
}