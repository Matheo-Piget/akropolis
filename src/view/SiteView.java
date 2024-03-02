package view;

import javax.swing.JPanel;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.awt.Graphics;
import model.Hexagon;
import model.Tile;

/**
 * Represents the site in the game.
 * The site is where the player can choose tiles to place on the board.
 * They can choose from a selection based on the shop capacity.
 */
public class SiteView extends JPanel implements View {
    private int capacity;

    public SiteView(int capacity) {
        setOpaque(true);
        setLayout(new GridLayout(capacity, 1));
        // It needs to take slighly less space than the grid view which is 1500x844
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

    public ArrayList<TileView> getTiles() {
        ArrayList<TileView> tiles = new ArrayList<TileView>();
        for (java.awt.Component component : getComponents()) {
            tiles.add((TileView) component);
        }
        return tiles;
    }


    public void update(ArrayList<Tile> tiles) {
        ArrayList<TileView> tileViews = new ArrayList<TileView>();
        for (Tile tile : tiles) {
            tileViews.add(obtainCorrespondingView(tile));
        }
        setTilesInSite(tileViews);
    }

    public TileView obtainCorrespondingView(Tile tile){
        TileView tileView;
        HexagonView [] hexs = new HexagonView[3];
        int i = 0;
        System.out.println("Tile: " + tile);
        for(Hexagon h : tile.getHexagons()){
            // Then we convert the hexagon to a hexagon view
            if(h instanceof model.Quarrie){
                hexs[i] = new QuarrieView(h.getX(), h.getY(), h.getZ());
            }
            else if (h instanceof model.Place){
                hexs[i] = new PlaceView(h.getX(), h.getY(), h.getZ(), (model.Place) h);
            }
            else{
                hexs[i] = new DistrictView(h.getX(), h.getY(), h.getZ(), (model.District) h);
            }
            i++;
        }
        tileView = new TileView(hexs[0], hexs[1], hexs[2]);
        return tileView;
    }

    public void setTilesInSite(ArrayList<TileView> tiles) {
        removeAll();
        // Don't add more tiles than the capacity
        for (int i = 0; i < capacity; i++) {
            if (i < tiles.size()) {
                add(tiles.get(i));
            }
        }
        validate();
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
