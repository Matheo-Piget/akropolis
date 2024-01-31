package states;
import util.State;

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
    }

    @Override
    public void exit() {
        System.out.println("Exiting Start State");
    }
}
