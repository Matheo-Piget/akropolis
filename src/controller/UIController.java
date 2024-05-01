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
        int score=currentPlayer.getGrid().calculateScore();
        int starsBuildingPlace = currentPlayer.getGrid().numberOfStars("Building Place");
        int starsMarketPlace = currentPlayer.getGrid().numberOfStars("Market Place");
        int starsBarrackPlace = currentPlayer.getGrid().numberOfStars("Barrack Place");
        int starsTemplePlace = currentPlayer.getGrid().numberOfStars("Temple Place");
        int starsGardenPlace = currentPlayer.getGrid().numberOfStars("Garden Place");
        int buildingScore = currentPlayer.getGrid().calculateMaxBuildingScore();
        int marketScore = currentPlayer.getGrid().calculateMarketScore(); 
        int barrackScore = currentPlayer.getGrid().calculateBarrackScore();
        int templeScore = currentPlayer.getGrid().calculateTempleScore();
        int gardenScore = currentPlayer.getGrid().calculateGardenScore();       
        currentPlayer.setScore(score);
        System.out.println("Le score est " + score);
        boardUI.setScore(currentPlayer.getScore(), starsBuildingPlace, starsMarketPlace, starsBarrackPlace, starsTemplePlace, starsGardenPlace, buildingScore, marketScore, barrackScore, templeScore, gardenScore);}
    /**
     * update the remaining tiles in the view
     */
    public void updateRemainingTilesInfo() {
        BoardUI boardUI = (BoardUI)view;
        int remainingTiles = (((Board)model).getStackTiles()).size();
        boardUI.setRemainingTiles(remainingTiles);
    }
}

