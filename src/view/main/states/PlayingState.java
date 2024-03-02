package view.main.states;

import controller.BoardController;
import model.Board;
import util.State;
import view.BoardView;
import view.main.App;
import model.Player;
import java.util.ArrayList;

public class PlayingState extends State {
    // The players of the game by default there are 1 player
    private ArrayList<Player> players = new ArrayList<Player>() {{
        add(new Player("Player 1"));
    }};
    private BoardController boardController;
    private static final PlayingState INSTANCE = new PlayingState();

    public static PlayingState getInstance() {
        return INSTANCE;
    }

    private PlayingState() {
        super();
    }

    @Override
    public void enter() {
        System.out.println("Entering Playing State");
        // Then we create the board
        Board board = new Board(players);
        // And we create the board view
        BoardView boardView = new BoardView(board.getStackTiles().size() * 2, board.switchSizePlayers());
        // Then we create the board controller
        boardController = new BoardController(board, boardView);
    }

    @Override
    public void exit() {
        System.out.println("Exiting Playing State");
        // Remove everything from the screen
        App.getInstance().getScreen().removeAll();
    }
}
