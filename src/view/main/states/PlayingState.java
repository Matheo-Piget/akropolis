package view.main.states;

import controller.BoardController;
import model.Board;
import util.State;
import view.main.App;
import view.panel.BoardView;
import model.Player;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the state of the game when it is being played.
 * This state is responsible for creating the board and the board controller.
 */
public class PlayingState extends State {
    // The players of the game by default there are 1 player
    private final ArrayList<Player> players = new ArrayList<>();
    // Just in case we need to access the board controller
    private BoardController boardController;
    private static final PlayingState INSTANCE = new PlayingState();

    public ArrayList<Player> getPlayers() {
        return players;
    }

    // Setter for the players
    public void setPlayers(List<String> playerNames) {
        players.clear(); // empty the list
        for (String name : playerNames) {
            players.add(new Player(name)); // create a new player
        }
    }

    public static PlayingState getInstance() {
        return INSTANCE;
    }

    @Override
    public void enter() {
        // Then we create the board
        Board board = new Board(players);
        // And we create the board view
        BoardView boardView = new BoardView(board.getStackTiles().size(), players, board.switchSizePlayers());
        // Then we create the board controller
        boardController = new BoardController(board, boardView);
    }

    @Override
    public void exit() {
        // Remove everything from the screen
        App.getInstance().getScreen().removeAll();
    }
}
