package view.main.states;

import util.State;
import view.main.App;
import util.Timeline;

import javax.swing.*;
import java.awt.*;

public class GameOverState extends State {

    private static final GameOverState INSTANCE = new GameOverState();
    private Timeline timeline;
    private JPanel gameOverPanel;

    private String winner;

    public static GameOverState getInstance() {
        return INSTANCE;
    }

    @Override
    public void enter() {
        System.out.println("Entering Game Over State");

        gameOverPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                // Draw a semi-transparent overlay to create a fade-in effect
                g2d.setColor(new Color(0, 0, 0, 150));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        App.getInstance().getScreen().add(gameOverPanel);
        gameOverPanel.setLayout(new BoxLayout(gameOverPanel, BoxLayout.Y_AXIS));

        // Create and add game over components with fade-in effect
        createGameOverComponentsWithFadeIn();

        // Start animation
        startAnimation();
    }

    @Override
    public void exit() {
        System.out.println("Exiting Game Over State");

        // Start exit animation
        startExitAnimation();
    }

    private void createGameOverComponentsWithFadeIn() {
        // Winner label
        JLabel winnerLabel = new JLabel("Jeu Fini ! \n le gagnant est : " + winner);
        winnerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        winnerLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        winnerLabel.setAlignmentY(JLabel.CENTER_ALIGNMENT);
        winnerLabel.setForeground(Color.WHITE);

        // Quit button
        JButton quitButton = createStyledButton();
        quitButton.addActionListener(e -> {
            timeline.stop(); // Stop animation
            App.getInstance().exitToMainMenu();
        });

        // Add components to panel
        gameOverPanel.add(Box.createVerticalGlue());
        gameOverPanel.add(winnerLabel);
        gameOverPanel.add(Box.createVerticalStrut(10));
        gameOverPanel.add(quitButton);
        gameOverPanel.add(Box.createVerticalGlue());
    }

    private JButton createStyledButton() {
        JButton button = new JButton("Quit to Main Menu");
        button.setAlignmentX(JButton.CENTER_ALIGNMENT);
        button.setBackground(new java.awt.Color(255, 215, 0));
        button.setForeground(java.awt.Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new java.awt.Color(255, 215, 0), 2));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new java.awt.Color(255, 235, 59));
                button.setForeground(java.awt.Color.BLACK);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new java.awt.Color(255, 215, 0));
                button.setForeground(java.awt.Color.BLACK);
            }
        });
        return button;
    }

    private void startAnimation() {
        timeline = new Timeline(0);
        timeline.addKeyFrame(new Timeline.KeyFrame(1000, 1, e -> {
            // Initial delay
        }));
        timeline.addKeyFrame(new Timeline.KeyFrame(2000, 50, e -> {
            // Animation steps
            float alpha = (float) e.getWhen() / 2000f;
            applyAlphaToPanel(alpha);
            gameOverPanel.repaint();
        }));
        timeline.setOnFinished(e -> {
            // Animation finished
        });
        timeline.start();
    }

    private void applyAlphaToPanel(float alpha) {
        for (Component component : gameOverPanel.getComponents()) {
            if (component instanceof JComponent) {
                ((JComponent) component).setOpaque(false);
                component.setBackground(new Color(0, 0, 0, 0));
                component.setForeground(new Color(255, 255, 255, (int) (alpha * 100)));
            }
        }
    }

    private void startExitAnimation() {
        // Fade out animation for components
        float startAlpha = 1f;
        float endAlpha = 0f;
        timeline = new Timeline(0);
        timeline.addKeyFrame(new Timeline.KeyFrame(15, 50, e -> {
            for (Component component : gameOverPanel.getComponents()) {
                if (component instanceof JComponent) {
                    float alpha = startAlpha - e.getWhen() * (startAlpha - endAlpha) / 50f;
                    component.setForeground(new Color(255, 255, 255, (int) (alpha * 255)));
                }
            }
        }));
        timeline.setOnFinished(e -> {
            App.getInstance().getScreen().removeAll();
            App.getInstance().getScreen().revalidate();
        });
        timeline.start();
    }

    public void setWinner(String winner) {

        this.winner = winner;

    }
}
