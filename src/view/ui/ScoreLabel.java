package view.ui;

import javax.swing.JLabel;
import javax.swing.Timer;
import java.awt.Color;

class ScoreLabel extends JLabel {
    private Timer timer;
    private boolean isBlinking = false;
    int nbEtoileHabitation;int nbEtoileMarché ; int nbEtoileCaserne ;
    int nbEtoileTemples; int nbEtoilesJardins;

    public ScoreLabel(int score) {
        super("Score : " + score);
        setOpaque(false);
        setForeground(java.awt.Color.WHITE);
    }

    public void setScore(int score, int starsBuildingPlace, int starsMarketPlace, int starsBarrackPlace, int starsTemplePlace, int starsGardenPlace, int buildingScore, int marketScore, int barrackScore, int templeScore, int gardenScore) {
        int res1 = starsBuildingPlace * buildingScore;
        int res2 = starsMarketPlace * marketScore;
        int res3 = starsBarrackPlace * barrackScore;
        int res4 = starsTemplePlace * templeScore;
        int res5 = starsGardenPlace * gardenScore;
    
        //int finalScore = res1 + res2 + res3 + res4 + res5;
        int finalScore = score;
        this.setText("<html>Final score : " + finalScore + "<br>" + starsBuildingPlace + " × " + buildingScore + " = " + res1 + "        " + starsMarketPlace + " × " + marketScore + " = " + res2 + "        " + starsBarrackPlace + " × " + barrackScore + " = " + res3 + "<br>" + starsTemplePlace + " × " + templeScore + " = " + res4 + "        " + starsGardenPlace + " × " + gardenScore + " = " + res5 + "</html>");        
        validate();
        startBlinking();
        repaint();
    }
    

    /**
     * Starts the blinking animation.
     */
    private void startBlinking() {
        if (timer != null && timer.isRunning()) {
            return;
        }

        timer = new Timer(900, e -> {
            isBlinking = !isBlinking;
            setForeground(isBlinking ? Color.RED : Color.WHITE);
            if (!isBlinking) {
                ((Timer) e.getSource()).stop();
            }
        });
        timer.start();
    }
}
