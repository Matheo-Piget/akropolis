package view.main.states;
import util.State;
import view.main.App;
import view.panel.MainMenuView;

/**
 * Represents the state of the game when it is starting.
 * This state is responsible for displaying the main menu.
 */
public class StartState extends State{
    
    private static final StartState INSTANCE = new StartState();

    public static StartState getInstance() {
        return INSTANCE;
    }

    @Override
    public void enter() {
        MainMenuView mainMenu = new MainMenuView();
        App.getInstance().getScreen().add(mainMenu, java.awt.BorderLayout.CENTER);
        App.getInstance().getScreen().revalidate();
        App.getInstance().getScreen().repaint();
    }

    @Override
    public void exit() {
        App.getInstance().getScreen().removeAll();
    }
}
