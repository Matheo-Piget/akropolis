package view;

import javax.swing.JComponent;
import model.Tile;
import model.Hexagon;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Timer;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents a single tile in the game.
 * A tile is three hexagons that form a triangle.
 */
public class TileView extends JComponent implements View {

    private boolean isHovered = false;
    protected HexagonView hex1, hex2, hex3;
    private float glow = 0.0f;
    private boolean increasing = true;
    private Timer glowTimer;
    private int rotation = 0;
    private BufferedImage image;

    public TileView() {
        // Empty constructor
    }

    public TileView(HexagonView hex1, HexagonView hex2, HexagonView hex3) {
        setOpaque(false);
        setFocusable(true);
        this.hex1 = hex1;
        this.hex2 = hex2;
        this.hex3 = hex3;
        int hexHeight = (int) (Math.sqrt(3) / 2 * HexagonView.size);
        this.setPreferredSize(new Dimension((int) (Math.sqrt(3) * 80), hexHeight));
        add(hex1);
        add(hex2);
        add(hex3);
        setupGlow();
        this.revalidate();
        this.repaint();
    }

    public TileView(Tile tile) {
        setOpaque(false);
        setFocusable(true);
        Hexagon hex1 = tile.getHexagons().get(0);
        Hexagon hex2 = tile.getHexagons().get(1);
        Hexagon hex3 = tile.getHexagons().get(2);
        HexagonView hexView1 = HexagonViewFactory.createHexagonView(hex1);
        HexagonView hexView2 = HexagonViewFactory.createHexagonView(hex2);
        HexagonView hexView3 = HexagonViewFactory.createHexagonView(hex3);
        setHexagons(hexView1, hexView2, hexView3);
    }

    public void rotate() {
        // Rotate the tile by 90 degrees
        rotation = (rotation + 90) % 360;
    }

    public int getRotation() {
        return rotation;
    }

    public void setHovered(boolean hovered) {
        isHovered = hovered;
        repaint();
    }

    private void setupGlow() {
        if(glowTimer != null) {
            glowTimer.stop();
        }
        // Create a Timer that fires every 100 milliseconds
        Timer timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update the glow variable
                if (increasing) {
                    glow += 0.1f;
                    if (glow > 1.0f) {
                        glow = 1.0f;
                        increasing = false;
                    }
                } else {
                    glow -= 0.1f;
                    if (glow < 0.0f) {
                        glow = 0.0f;
                        increasing = true;
                    }
                }

                // Repaint the component
                repaint();
            }
        });
        this.glowTimer = timer;
    }

    public void stopGlow() {
        glowTimer.stop();
        glow = 0.0f;
        repaint();
    }

    @Override
    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);
        getParent().dispatchEvent(e);
    }

    @Override
    protected void processMouseMotionEvent(MouseEvent e) {
        super.processMouseMotionEvent(e);
        getParent().dispatchEvent(e);
    }

    @Override
    public void doLayout() {
        super.doLayout();

        int hexWidth = HexagonView.size;
        int hexHeight = (int) (Math.sqrt(3) / 2 * hexWidth);

        // Calculate the center of the TileView
        int centerX = this.getWidth() / 2;
        int centerY = this.getHeight() / 4;

        // Position the first hexagon at the center y and right most x
        hex1.setLocation(centerX + hexWidth / 4, centerY);

        // Position the second and third hexagons at the left of the first hexagon
        hex2.setLocation(centerX - hexWidth / 2, centerY - hexHeight / 2);
        hex3.setLocation(centerX - hexWidth / 2, centerY + hexHeight / 2);
    }

    public void setHexagons(HexagonView hex1, HexagonView hex2, HexagonView hex3) {
        removeAll();
        this.hex1 = hex1;
        this.hex2 = hex2;
        this.hex3 = hex3;
        add(hex1);
        add(hex2);
        add(hex3);
        setupGlow();
        this.revalidate();
        this.repaint();
    }

    public ArrayList<HexagonView> getHexagons() {
        return new ArrayList<>(Arrays.asList(hex1, hex2, hex3));
    }

    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        // Change the color of the hexagons if the tile is hovered
        if (isHovered) {
            if (!glowTimer.isRunning()) {
                glowTimer.start();
            }
            g2.setStroke(new BasicStroke(1));

            // Draw a dark border around this component which is a rectangle
            g2.setColor(new Color(73, 216, 230));
            g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

            // Draw the glow
            g2.setColor(new Color(73 / 255f, 216 / 255f, 230 / 255f, glow));
            g2.setStroke(new BasicStroke(10));
            g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        } else {
            if (glowTimer.isRunning()) {
                glowTimer.stop();
                // Reset the glow
                glow = 0.0f;
                // Redraw the border
                g2.setColor(new Color(73, 216, 230));
                g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                repaint();
            }
        }
        g2.dispose();
    }
}
