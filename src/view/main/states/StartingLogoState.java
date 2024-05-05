package view.main.states;

import util.State;
import view.main.App;
import javax.swing.JPanel;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.event.KeyListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import util.Timeline;

/**
 * The state that shows the starting logo
 */
public class StartingLogoState extends State {

    private static final StartingLogoState INSTANCE = new StartingLogoState();
    private Timeline timeline;
    private JPanel blackScreen;

    public static StartingLogoState getInstance() {
        return INSTANCE;
    }

    @Override
    public void enter() {
        System.out.println("Entering Starting Logo State");
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
        JLabel logoUniv = new JLabel("Université Paris Cité");
        BufferedImage imageUniv = null;
        final ImageIcon[] logoUnivIcon = { null };
        try {
            imageUniv = ImageIO
                    .read(Objects.requireNonNull(getClass().getResource("/menu/universiteParisCite_logo_horizontal_blanc_1000px.png")));
            logoUnivIcon[0] = new ImageIcon(imageUniv);
            logoUniv = new JLabel(logoUnivIcon[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logoUniv.setHorizontalAlignment(JLabel.CENTER);
        logoUniv.setVerticalAlignment(JLabel.CENTER);
        logoUniv.setForeground(new java.awt.Color(255, 255, 255, 0));
        JLabel logo = new JLabel("Un projet réalisé par ED1b");
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
        // #Java sucks
        final JLabel finalLogoUniv = logoUniv;
        final BufferedImage finalImageUniv = imageUniv;
        final float[] alpha = { 0 };
        timeline.addKeyFrame(new Timeline.KeyFrame(15, 50, e -> {
            if (finalLogoUniv.getParent() == null) {
                blackScreen.removeAll();
                blackScreen.add(finalLogoUniv, BorderLayout.CENTER);
                blackScreen.revalidate();
            }
            // Increase the image's opacity by a step each time the action is performed
            alpha[0] = Math.min(alpha[0] + 1f / 50, 1);
            BufferedImage imageWithAlpha = new BufferedImage(finalImageUniv.getWidth(), finalImageUniv.getHeight(),
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = imageWithAlpha.createGraphics();
            g2d.setComposite(AlphaComposite.SrcOver.derive(alpha[0]));
            g2d.drawImage(finalImageUniv, 0, 0, null);
            g2d.dispose();
            finalLogoUniv.setIcon(new ImageIcon(imageWithAlpha));
        }));
        timeline.addKeyFrame(new Timeline.KeyFrame(1000, 1, e -> {
            // Just wait
        }));
        timeline.addKeyFrame(new Timeline.KeyFrame(15, 50, e -> {
            // Decrease the image's opacity by a step each time the action is performed
            alpha[0] = Math.max(alpha[0] - 1f / 50, 0);
            BufferedImage imageWithAlpha = new BufferedImage(finalImageUniv.getWidth(), finalImageUniv.getHeight(),
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = imageWithAlpha.createGraphics();
            g2d.setComposite(AlphaComposite.SrcOver.derive(alpha[0]));
            g2d.drawImage(finalImageUniv, 0, 0, null);
            g2d.dispose();
            finalLogoUniv.setIcon(new ImageIcon(imageWithAlpha));
        }));
        timeline.setOnFinished(e -> handleExit());
        timeline.start();
    }

    /**
     * Handles the exit of the state
     */
    private void handleExit() {
        timeline.stop();
        // Make a smooth transition to the next state by loading the bg image
        try {
            // Load the background image
            BufferedImage backgroundImage = ImageIO
                    .read(Objects.requireNonNull(getClass().getResourceAsStream("/menu/akropolisBG.jpg")));
            // Then we will make a smooth fade in transition to make it look nice
            blackScreen.removeAll();
            JLabel bg = new JLabel(new ImageIcon(backgroundImage));
            bg.setVisible(false);
            blackScreen.add(bg, java.awt.BorderLayout.CENTER);
            timeline = new Timeline(0);
            float[] alpha = { 0 };
            timeline.addKeyFrame(new Timeline.KeyFrame(15, 50, e -> {
                // Increase the image's opacity by a step each time the action is performed
                alpha[0] = Math.min(alpha[0] + 1f / 50, 1);
                BufferedImage imageWithAlpha = new BufferedImage(backgroundImage.getWidth(),
                        backgroundImage.getHeight(),
                        BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = imageWithAlpha.createGraphics();
                g2d.setComposite(AlphaComposite.SrcOver.derive(alpha[0]));
                g2d.drawImage(backgroundImage, 0, 0, null);
                g2d.dispose();
                bg.setIcon(new ImageIcon(imageWithAlpha));
                bg.setVisible(true);
            }));
            timeline.setOnFinished(e -> App.getInstance().appState.changeState(StartState.getInstance()));
            timeline.start();
        } catch (IOException e) {
            e.printStackTrace();
            App.getInstance().appState.changeState(StartState.getInstance());
        }
    }

    @Override
    public void exit() {
        System.out.println("Exiting Starting Logo State");
        App.getInstance().getScreen().removeAll();
    }
}
