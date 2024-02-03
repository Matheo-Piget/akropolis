package model;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final List<Player> players;
    private Player currentPlayer;

    public Game() {
        players = new ArrayList<>();
    }

    public void startGame() {
        currentPlayer = players.get(0); // Start with the first player
        startTurn(currentPlayer);
    }

    public void startTurn(Player player) {
        // Logique pour commencer un tour
    }

    public void endTurn(Player player) {
        // Logique pour la fin du tour
        currentPlayer = getNextPlayer();
        startTurn(currentPlayer);
    }

    private Player getNextPlayer() {
        // retourne le prochain joueur
        return players.get(0);
    }
}
