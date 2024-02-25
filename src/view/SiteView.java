package view;

import javax.swing.JPanel;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Graphics;

/**
 * Represents the site in the game.
 * The site is where the player can choose tiles to place on the board.
 * They can choose from a selection based on the shop capacity.
 */
public class SiteView extends JPanel {
    private int capacity;
    private BufferedImage background;

    public SiteView(int capacity) {
        setLayout(new GridLayout(capacity, 1));
        // It needs to take slighly less space than the grid view which is 1500x844
        // To have a nice padding on the top and bottom
        setPreferredSize(new java.awt.Dimension(HexagonView.WIDTH * 2, 800));
        setOpaque(false);
        this.capacity = capacity;
        // Load the background image
        try {
            background = ImageIO.read(getClass().getResourceAsStream("/siteBackground.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setTilesInSite(ArrayList<TileView> tiles) {
        removeAll();
        // Don't add more tiles than the capacity
        for (int i = 0; i < capacity; i++) {
            if (i < tiles.size()) {
                add(tiles.get(i));
                System.out.println("Added tile to site");
            }
        }
        validate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        double componentRatio = (double) getWidth() / getHeight();
        double imageRatio = (double) background.getWidth() / background.getHeight();
        int newWidth, newHeight;

        if (componentRatio > imageRatio) {
            newHeight = getHeight();
            newWidth = (int) (newHeight * imageRatio);
        } else {
            newWidth = getWidth();
            newHeight = (int) (newWidth / imageRatio);
        }

        int x = (getWidth() - newWidth) / 2;
        int y = (getHeight() - newHeight) / 2;

        g.drawImage(background, x, y, newWidth, newHeight, this);
    }
}
