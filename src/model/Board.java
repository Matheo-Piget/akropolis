package model;

import java.util.ArrayList;
import java.util.List;
import java.beans.PropertyChangeListener;

/**
 * Represents the game board and manages the game.
 */
public class Board extends Model {
    private final ArrayList<Player> playerList; // List of players in the game
    private final StackTiles stackTiles; // The stack of tiles in the game
    private final Site site; // The tiles on the table
    private Player currentPlayer; // The current player
    private int manche = 0;

    /**
     * Constructs a new board and initializes the game.
     * @param players The list of players in the game.
     */
    public Board(List<Player> players) {
        playerList = new ArrayList<>();
        int nb_rocks = 1;
        for (Player player : players) {
            player.setResources(nb_rocks);
            player.setGrid(new Grid(player));
            playerList.add(player);
            nb_rocks++;
        }
        stackTiles = new StackTiles(60); // Assuming 60 tiles in the stack
        site = new Site(switchSizePlayers());
        currentPlayer = playerList.get(0);
        stackTiles.shuffle(); // Shuffle the stack of tiles
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        propertyChangeSupport.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        propertyChangeSupport.removePropertyChangeListener(pcl);
    }

    public boolean setSelectedTile(Tile tile) {
        if(canChooseTile(tile)) {;
            playerList.get(manche % playerList.size()).setSelectedTile(tile);
            return true;
        }
        return false;
    }

    /**
     * Starts the game by starting the first turn.
     */
    public void startGame() {
        startTurn(currentPlayer);
    }

    /**
     * Starts a turn for the given player.
     * @param player The player for whom the turn is starting.
     */
    public void startTurn(Player player) {
        firePropertyChange("nextTurn", null, player);

        currentPlayer.getGrid().display();
        System.out.println("Player " + player.getName() + "'s turn");
        // Add tiles to the table if the manche is over
        if (manche % getNumberOfPlayers() == 0) {
            site.updateSite(stackTiles);
        }
    }

    /**
     * Adds a tile to the grid of the current player.
     */
    public void addTileToGrid() {
        Tile tile = currentPlayer.getSelectedTile();
        if(tile == null) return; // No tile selected
        if (addTile(tile)) {
            currentPlayer.setResources(currentPlayer.getResources() - site.calculateCost(tile));
            // Remove the tile from the site
            site.removeTile(tile);
            firePropertyChange("addTile", null, null);
            endTurn();
        }
    }

    /**
     * Ends the turn for the given player, switches to the next player, and starts their turn.
     */
    public void endTurn() {
        System.out.println("End of turn : "+ currentPlayer.getName());


        // Logic to end a turn
        manche++;
        currentPlayer = getNextPlayer(); // Switch to the next player
        currentPlayer.setSelectedTile(null); // Reset the selected tile

        System.out.println("Next turn : "+ getNextPlayer().getName());
        firePropertyChange("nextTurn", null, currentPlayer);

        startTurn(currentPlayer); // Start the next player's turn
    }

    /**
     * Retrieves the next player in the list.
     * @return The next player in the list.
     */
    private Player getNextPlayer() {
        // Trouver l'index du joueur actuel dans la liste des joueurs
        return playerList.get((manche) % playerList.size());
    }

    public ArrayList<Grid> getGrids() {
        ArrayList<Grid> grids = new ArrayList<>();
        for (Player player : playerList) {
            grids.add(player.getGrid());
        }
        return grids;
    }

    public Site getSite() {
        return site;
    }

    public StackTiles getStackTiles() {
        return stackTiles;
    }

    public int getManche() {
        return manche;
    }

    /**
     * Returns the number of players in the game.
     * @return The number of players in the game.
     */
    private int getNumberOfPlayers() {
        return playerList.size();
    }

    /**
     * Returns the number of tiles to put on the table according to the number of players.
     * @return The number of tiles to put on the table.
     */
    public int switchSizePlayers() {
        System.out.println(getNumberOfPlayers());
        return switch (getNumberOfPlayers()) {
            case 1, 2 -> 3;
            case 3 -> 4;
            default -> 5;
        };
    }

    /**
     * For debug only
     * @param tile
     * @return
     */
    public boolean addTile(Tile tile) {
        System.out.println("Adding tile to grid to player "+currentPlayer.getName());
        return currentPlayer.getGrid().addTile(tile);
    }

    public boolean addTile(Tile tile, Player player) {
        return player.getGrid().addTile(tile);
    }

    public Hexagon getHexagon(int x, int y) {
        return currentPlayer.getGrid().getHexagon(x, y);
    }

    public int getScore(Player player) {
        return player.getGrid().calculateScore();
    }

    /**
     * Returns the current player.
     * @return The current player.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }


    /**
     * Checks if the game is over.
     * @return true if the game is over, false otherwise.
     */
    public boolean isGameOver() {
        return stackTiles.isEmpty() && site.isEmpty();
    }

    /**
     * Returns the winner of the game.
     * @return The winner of the game.
     */
    public Player getWinner() {
        if (isGameOver()) {
            Player winner = playerList.get(0);
            for (Player p : playerList) {
                if (getScore(p) > getScore(winner)) {
                    winner = p;
                }
            }
            return winner;
        }
        return null;
    }

    /**
     * Checks if the player can choose a tile from the provided list based on their resources.
     * @param chosen The tile chosen by the player.
     * @return True if the player can choose the tile, false otherwise.
     */
    public boolean canChooseTile(Tile chosen) {
        int price = 0;
        for (Tile tile : site.getTiles()) {
            if (tile != chosen) {
                price++;
            } else {
                break;
            }
        }
        System.out.println("Price : "+price);
        return currentPlayer.getResources() >= price;
    }
}
