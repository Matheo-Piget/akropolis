package view.panel;

import javax.swing.JPanel;

import view.View;
import view.component.TileView;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.awt.Graphics;

/**
 * Represents the site in the game.
 * The site is where the player can choose tiles to place on the board.
 * They can choose from a selection based on the shop capacity.
 */
public class SiteView extends JPanel implements View {
    private final int capacity;
    private MouseAdapter ma;

    public SiteView(int capacity) {
        setOpaque(true);
        setLayout(new GridLayout(capacity, 1));
        setPreferredSize(new java.awt.Dimension(200, 800));
        this.capacity = capacity;
    }

    /**
     * Get the tiles in the site
     * @return The tiles in the site
     */
    public ArrayList<TileView> getTiles() {
        ArrayList<TileView> tiles = new ArrayList<>();
        for (java.awt.Component component : getComponents()) {
            tiles.add((TileView) component);
        }
        return tiles;
    }


    /**
     * Update the site view
     * @param tiles The tiles to update the site with
     */
    public void update(ArrayList<TileView> tiles) { 
        setTilesInSite(tiles);
    }

    /**
     * Set the tiles in the site
     * @param tiles The tiles to set in the site
     */
    public void setTilesInSite(ArrayList<TileView> tiles) {
        // Stop all animations
        for (TileView tile : getTiles()) {
            tile.stopGlow();
        }
        removeAll();
        // Don't add more tiles than the capacity
        for (int i = 0; i < capacity; i++) {
            if (i < tiles.size()) {
                tiles.get(i).setCost(i);
                add(tiles.get(i));
            }
        }
        revalidate();
        repaint();
    }

    /**
     * Disable the listeners
     */
    public void disableListeners(){
        if(ma == null){
            // Get the mouse adapter
            ma = (MouseAdapter) getMouseListeners()[0];
        }
        removeMouseListener(ma);
        removeMouseMotionListener(ma);
        setEnabled(false);
    }

    /**
     * Enable the listeners
     */
    public void enableListeners(){
        if(ma != null){
            addMouseListener(ma);
            addMouseMotionListener(ma);
            setEnabled(true);
        }
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
