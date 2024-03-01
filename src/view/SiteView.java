package view;

import javax.swing.JPanel;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.awt.Component;
import java.awt.Graphics;
import java.beans.PropertyChangeSupport;

/**
 * Represents the site in the game.
 * The site is where the player can choose tiles to place on the board.
 * They can choose from a selection based on the shop capacity.
 */
public class SiteView extends JPanel implements View {
    private int capacity;
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public SiteView(int capacity) {
        setOpaque(true);
        setLayout(new GridLayout(capacity, 1));
        // It needs to take slighly less space than the grid view which is 1500x844
        // To have a nice padding on the top and bottom
        setPreferredSize(new java.awt.Dimension(200, 800));
        this.capacity = capacity;
    }

    @Override
    public void addPropertyChangeListener(java.beans.PropertyChangeListener pcl) {
        propertyChangeSupport.addPropertyChangeListener(pcl);
    }

    @Override
    public void removePropertyChangeListener(java.beans.PropertyChangeListener pcl) {
        propertyChangeSupport.removePropertyChangeListener(pcl);
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
    public Component add(Component c){
        super.add(c);
        // Then add a mouse click listener to the tile
        c.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // Notify the controller that a tile has been selected to update the site model
                propertyChangeSupport.firePropertyChange("tileSelected", null, c);
                System.out.println("Tile selected");
            }
        });
        return c;
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
