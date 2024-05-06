package view.main.states;

import util.State;
import util.Timeline;
import view.main.App;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameOverState extends State {

    private static final GameOverState INSTANCE = new GameOverState();
    private Timeline timeline;
    private JPanel blackScreen;
    private String winner;

    public static GameOverState getInstance() {
        return INSTANCE;
    }

    @Override
    public void enter() {
        System.out.println("Entering GameOver State");
        blackScreen = new JPanel(new BorderLayout());
        KeyListener keyListener = new KeyListener() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
            }

            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                // Skip the logo animation if a key is pressed
                System.out.println("Key pressed, skipping logo animation");
                // Remove the key listener
                blackScreen.removeKeyListener(this);
                handleExit();
            }

            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
            }
        };
        blackScreen.setFocusable(true);
        blackScreen.setOpaque(true);
        blackScreen.requestFocus();
        blackScreen.requestFocusInWindow();
        blackScreen.addKeyListener(keyListener);
        blackScreen.setBackground(java.awt.Color.BLACK);
        App.getInstance().getScreen().add(blackScreen, java.awt.BorderLayout.CENTER);
        App.getInstance().getScreen().validate();
        JLabel logo = new JLabel("Le Gagnant est : " + winner);
        // White transparent color
        logo.setForeground(new java.awt.Color(255, 255, 255, 0));
        logo.setHorizontalAlignment(JLabel.CENTER);
        logo.setVerticalAlignment(JLabel.CENTER);
        blackScreen.add(logo, java.awt.BorderLayout.CENTER);
        blackScreen.validate();
        logo.setFont(new java.awt.Font("Serif", java.awt.Font.BOLD, 25));
        timeline = new Timeline(0);
        timeline.addKeyFrame(new Timeline.KeyFrame(15, 50, e -> {
            // Increase the label's opacity by a step each time the action is performed
            int alpha = logo.getForeground().getAlpha();
            alpha = Math.min(alpha + 255 / 50, 255);
            logo.setForeground(new java.awt.Color(logo.getForeground().getRed(), logo.getForeground().getGreen(),
                    logo.getForeground().getBlue(), alpha));
        }));
        timeline.addKeyFrame(new Timeline.KeyFrame(1000, 1, e -> {
            // Just wait
        }));
        timeline.addKeyFrame(new Timeline.KeyFrame(15, 50, e -> {
            // Decrease the label's opacity by a step each time the action is performed
            int alpha = logo.getForeground().getAlpha();
            alpha = Math.max(alpha - 255 / 50, 0);
            logo.setForeground(new java.awt.Color(logo.getForeground().getRed(), logo.getForeground().getGreen(),
                    logo.getForeground().getBlue(), alpha));
        }));
        timeline.setOnFinished(e -> handleExit());
        timeline.start();
    }

    @Override
    public void exit() {
        System.out.println("Exiting Game Over State");

        // Remove all components from the screen
        App.getInstance().getScreen().removeAll();
    }

    /**
     * Creates a styled button.
     *
     * @return the styled button
     */
    private JButton createStyledButton() {
        JButton button = new JButton("Retour au menu principal");
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

    /**
     * Handles the exit of the state
     */
    private void handleExit() {
        timeline.stop();
        blackScreen.removeAll();

        blackScreen.setLayout(new GridBagLayout());

        JLabel label = new JLabel("Bravo " + winner + " ! ");
        label.setFont(new Font("Arial", Font.BOLD, 25));
        label.setForeground(Color.WHITE);

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 0;
        gbc2.gridy = 0;
        gbc2.insets = new Insets(10, 10, 10, 10);
        gbc2.anchor = GridBagConstraints.CENTER;

        blackScreen.add(label, gbc2);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 10, 10, 10); //


        JButton button = createStyledButton();
        button.addActionListener(e -> App.getInstance().appState.changeState(StartState.getInstance()));

        // Center the button
        gbc.anchor = GridBagConstraints.CENTER;
        blackScreen.add(button, gbc);

        blackScreen.revalidate();
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
}
