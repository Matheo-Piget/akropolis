package view;

import javax.swing.JPanel;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.awt.Graphics;

/**
 * Represents the site in the game.
 * The site is where the player can choose tiles to place on the board.
 * They can choose from a selection based on the shop capacity.
 */
public class SiteView extends JPanel implements View {
    private final int capacity;

    public SiteView(int capacity) {
        setOpaque(true);
        setLayout(new GridLayout(capacity, 1));
        // It needs to take slightly less space than the grid view which is 1500x844
        // To have a nice padding on the top and bottom
        setPreferredSize(new java.awt.Dimension(200, 800));
        this.capacity = capacity;
    }

    public TileView getTileClicked(int x, int y) {
        for (TileView tileView : getTiles()) {
            if (tileView.contains(x, y)) {
                return tileView;
            }
        }
        return null;
    }

    public int getTileSize(){
        // Calculate the size of the tiles
        return getPreferredSize().height / capacity;
    }

    public ArrayList<TileView> getTiles() {
        ArrayList<TileView> tiles = new ArrayList<>();
        for (java.awt.Component component : getComponents()) {
            tiles.add((TileView) component);
        }
        return tiles;
    }


    public void update(ArrayList<TileView> tiles) { 
        setTilesInSite(tiles);
        System.out.println("SiteView updated");
    }

    public void setTilesInSite(ArrayList<TileView> tiles) {
        // Stop all animations
        for (TileView tile : getTiles()) {
            tile.stopGlow();
        }
        removeAll();
        // Don't add more tiles than the capacity
        for (int i = 0; i < capacity; i++) {
            if (i < tiles.size()) {
                tiles.get(i).setSize(new java.awt.Dimension(200, 800 / capacity));
                add(tiles.get(i));
            }
        }
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Fill the panel with a gray color
        g.setColor(new java.awt.Color(0, 0, 0, 100));
        g.fillRect(0, 0, getWidth(), getHeight());
        // Then draw the border
        g.setColor(new java.awt.Color(255, 255, 255, 100));

        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    }
}
