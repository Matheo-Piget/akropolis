package controller;

import model.Board;
import model.Player;
import view.ui.BoardUI;
import java.beans.PropertyChangeEvent;

public class UIController extends Controller {

    public UIController(Board model, BoardUI boardUI) {
        super(model, boardUI);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if (propertyName.equals("playerUpdated")) {
            updatePlayerInfo();
        } else if (propertyName.equals("tilesRemainingUpdated")) {
            updateRemainingTilesInfo();
        }
    }

    /**
     * Update the player infos in the view
     */
    public void updatePlayerInfo() {
        Player currentPlayer = ((Board)model).getCurrentPlayer();
        BoardUI boardUI = (BoardUI)view;
        boardUI.setPlayer(currentPlayer.getName());
        boardUI.setRock(currentPlayer.getResources());
        int score=currentPlayer.getGrid().calculateScore();
        currentPlayer.setScore(score);
        System.out.println("Le score est "+score);
        boardUI.setscore(currentPlayer.getScore());
    }

    /**
     * update the remaining tiles in the view
     */
    public void updateRemainingTilesInfo() {
        BoardUI boardUI = (BoardUI)view;
        int remainingTiles = (((Board)model).getStackTiles()).size();
        boardUI.setRemainingTiles(remainingTiles);
    }
}

