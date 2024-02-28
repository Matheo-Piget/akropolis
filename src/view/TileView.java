package view;

import javax.swing.JComponent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Timer;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Dimension;

/**
 * Represents a single tile in the game.
 * A tile is three hexagons that form a triangle.
 */
public class TileView extends JComponent {

    private boolean isHovered = false;
    private HexagonView hex1, hex2, hex3;
    private boolean isClicked = false;
    private float glow = 0.0f;
    private boolean increasing = true;
    private Timer glowTimer;

    public TileView(HexagonView hex1, HexagonView hex2, HexagonView hex3) {
        setOpaque(false);
        this.hex1 = hex1;
        this.hex2 = hex2;
        this.hex3 = hex3;
        int hexWidth = HexagonView.size;
        int hexHeight = (int) (Math.sqrt(3) / 2 * hexWidth);
        this.setPreferredSize(new Dimension((int) (Math.sqrt(3) * hexWidth), hexHeight));
        add(hex1);
        add(hex2);
        add(hex3);
        // Remove the mouse listener from the hexagons
        hex1.removeMouseListener(hex1.getMouseListeners()[0]);
        hex2.removeMouseListener(hex2.getMouseListeners()[0]);
        hex3.removeMouseListener(hex3.getMouseListeners()[0]);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
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
        this.revalidate();
        this.repaint();
    }

    @Override
    public void doLayout() {
        super.doLayout();

        int hexWidth = HexagonView.size;
        int hexHeight = (int) (Math.sqrt(3) / 2 * hexWidth);

        // Calculate the total width and height of the hexagons
        int totalWidth = 3 * hexWidth;
        int totalHeight = hexHeight * 2;

        // Calculate the center of the TileView
        int centerX = this.getWidth() / 2;
        int centerY = this.getHeight() / 2;

        // Calculate the top left corner of the area where the hexagons should be placed
        int startX = centerX - totalWidth / 2;
        int startY = centerY - totalHeight / 2;

        // Position the first hexagon at the top
        hex1.setLocation(startX + hexWidth, startY);

        // Position the second and third hexagons at the bottom left and right corners
        // of the first hexagon
        hex2.setLocation(startX + (int) (hexWidth * 2 - Math.sqrt(3) * hexWidth), startY + hexHeight / 2);
        hex3.setLocation(startX + (int) (Math.sqrt(3) * hexWidth), startY + hexHeight / 2);
    }

    public void setHexagons(HexagonView hex1, HexagonView hex2, HexagonView hex3) {
        removeAll();
        add(hex1);
        add(hex2);
        add(hex3);
    }

    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        // Change the color of the hexagons if the tile is hovered
        if (isHovered && !isClicked) {
            if (!glowTimer.isRunning()) {
                glowTimer.start();
            }
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setStroke(new BasicStroke(1));

            // Draw a dark border around this component which is a rectangle
            g2.setColor(new Color(73, 216, 230));
            g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

            // Draw the glow
            g2.setColor(new Color(73 / 255f, 216 / 255f, 230 / 255f, glow));
            g2.setStroke(new BasicStroke(10));
            g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            g2.dispose();

        } else {
            if (glowTimer.isRunning()) {
                glowTimer.stop();
            }
        }
    }
}
