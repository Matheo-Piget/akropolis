package view.main.states;

import util.State;
import view.main.App;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameOverState extends State {

    private static final GameOverState INSTANCE = new GameOverState();
    private JPanel gameOverPanel;

    private String winner;

    public static GameOverState getInstance() {
        return INSTANCE;
    }

    @Override
    public void enter() {
        System.out.println("Entering Game Over State");

        gameOverPanel = new JPanel(new BorderLayout());
        gameOverPanel.setBackground(new Color(50, 50, 50));
        App.getInstance().getScreen().add(gameOverPanel);

        // Create and add game over components
        createGameOverComponents();

        // Revalidate screen
        App.getInstance().getScreen().revalidate();
    }

    @Override
    public void exit() {
        System.out.println("Exiting Game Over State");

        // Remove all components from the screen
        App.getInstance().getScreen().removeAll();
    }

    /**
     * Creates the game over components.
     */
    private void createGameOverComponents() {
        // Winner label
        JLabel winnerLabel = new JLabel("Game Over! Winner: " + winner);
        winnerLabel.setFont(new Font("Arial", Font.BOLD, 40));
        winnerLabel.setHorizontalAlignment(JLabel.CENTER);
        winnerLabel.setForeground(Color.WHITE);
        winnerLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        gameOverPanel.add(winnerLabel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);

        // Quit to Main Menu button
        JButton quitButton = createStyledButton("Quit to Main Menu");
        quitButton.addActionListener(e -> App.getInstance().exitToMainMenu());
        buttonPanel.add(quitButton);

        gameOverPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Creates a styled button.
     *
     * @param text the text of the button
     * @return the styled button
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(255, 215, 0));
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 215, 0), 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.setPreferredSize(new Dimension(200, 40));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(255, 235, 59));
                button.setForeground(Color.BLACK);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(255, 215, 0));
                button.setForeground(Color.BLACK);
            }
        });
        return button;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
}
