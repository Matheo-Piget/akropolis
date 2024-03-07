package controller;

import model.Board;
import model.Player;
import view.BoardUI;
import java.beans.PropertyChangeEvent;

public class UIController extends Controller {
    private BoardUI boardUI;

    public UIController(Board model, BoardUI boardUI) {
        super(model, boardUI);
        this.boardUI = boardUI;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if (propertyName.equals("playerUpdated ")) {
            updatePlayerInfo();
        } else if (propertyName.equals("tilesRemainingUpdated")) {
            updateRemainingTilesInfo();
        }
    }

    private void updatePlayerInfo() {
        Player currentPlayer = ((Board)model).getCurrentPlayer();
        boardUI.setPlayer(currentPlayer.getName());
        boardUI.setRock(currentPlayer.getResources());
    }

    private void updateRemainingTilesInfo() {
        int remainingTiles = (((Board)model).getStackTiles()).size();
        boardUI.setRemainingTiles(remainingTiles+1);
    }
}

