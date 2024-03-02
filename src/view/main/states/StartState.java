package view.main.states;
import util.State;
import view.MainMenuView;
import view.main.App;

public class StartState extends State{
    
    private static final StartState INSTANCE = new StartState();

    public static StartState getInstance() {
        return INSTANCE;
    }

    private StartState() {
        super();
    }

    @Override
    public void enter() {
        System.out.println("Entering Start State");
        MainMenuView mainMenu = new MainMenuView();
        App.getInstance().getScreen().add(mainMenu, java.awt.BorderLayout.CENTER);
        App.getInstance().getScreen().revalidate();
    }

    @Override
    public void exit() {
        System.out.println("Exiting Start State");
        App.getInstance().getScreen().removeAll();
    }
}
