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
        // Do nothing
    }

    /**
     * Update the player infos in the view
     */
    public void updatePlayerInfo() {
        Player currentPlayer = ((Board)model).getCurrentPlayer();
        BoardUI boardUI = (BoardUI)view;
        boardUI.setPlayer(currentPlayer.getName());
        boardUI.setRock(currentPlayer.getResources());
        int [] mult = new int[5];
        mult[0] = currentPlayer.getGrid().numberOfStars("Building Place");
        mult[1] = currentPlayer.getGrid().numberOfStars("Market Place");
        mult[2] = currentPlayer.getGrid().numberOfStars("Temple Place");
        mult[3] = currentPlayer.getGrid().numberOfStars("Barrack Place");
        mult[4] = currentPlayer.getGrid().numberOfStars("Garden Place");
        int [] scores = new int[5];
        scores[0] = currentPlayer.getGrid().calculateBuildingScoreNoPlaces();
        scores[1] = currentPlayer.getGrid().calculateMarketScoreNoPlaces();
        scores[2] = currentPlayer.getGrid().calculateTempleScoreNoPlaces();
        scores[3] = currentPlayer.getGrid().calculateBarrackScoreNoPlaces();
        scores[4] = currentPlayer.getGrid().calculateGardenScoreNoPlaces();
        int score = currentPlayer.getGrid().calculateScore();
        boardUI.setScore(score, scores, mult);
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

