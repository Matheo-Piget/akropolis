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

    public void setScore(int score , int arg1,int arg2 , int arg3 ,int arg4,int arg5, int arg11,int arg22 , int arg33 ,int arg44,int arg55) {
        int res1 = arg1 * arg11;
        int res2 = arg2 * arg22;
        int res3 = arg3 * arg33;
        int res4 = arg4 * arg44;
        int res5 = arg5 * arg55;
    
        //int finalScore = res1 + res2 + res3 + res4 + res5;
        int finalScore = score;
        this.setText("<html>Score final : " + finalScore + "<br>" + arg1 + " × " + arg11 + " = " + res1 + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + arg2 + " × " + arg22 + " = " + res2 + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + arg3 + " × " + arg33 + " = " + res3 + "<br>" + arg4 + " × " + arg44 + " = " + res4 + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + arg5 + " × " + arg55 + " = " + res5 + "</html>");        validate();
        startBlinking();
        repaint();
    }

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
