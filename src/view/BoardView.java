package view;

import javax.swing.JPanel;
import javax.swing.JFrame;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.awt.BorderLayout;
import java.util.ArrayList;

/**
 * Panel for displaying the game board.
 * It takes care of diplaying the scrolling board and the site to make the game
 * playable.
 */
public class BoardView extends JPanel implements View {

    private ScrollableGridView gridView;
    private SiteView siteView;
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    /**
     * Constructor for the BoardView.
     * 
     * @param maxHexagons The maximum number of hexagons to be displayed on the
     *                    board.
     * @param capacity    The capacity of the site.
     */
    public BoardView(int maxHexagons, int capacity) {
        setLayout(new BorderLayout());
        setOpaque(true);
        gridView = new ScrollableGridView(maxHexagons);
        setSize(gridView.getPreferredSize());
        siteView = new SiteView(capacity);
        add(gridView, BorderLayout.CENTER);
        add(siteView, BorderLayout.WEST);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        propertyChangeSupport.addPropertyChangeListener(pcl);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        propertyChangeSupport.removePropertyChangeListener(pcl);
    }

    public ScrollableGridView getGridView() {
        return gridView;
    }

    public SiteView getSiteView() {
        return siteView;
    }

    // For testing purposes
    public static void main(String[] args) {
        BoardView boardView = new BoardView(40, 5);
        ArrayList<TileView> tiles = new ArrayList<TileView>();
        for (int i = 0; i < 3; i++) {
            tiles.add(new TileView(new QuarrieView(0, 0, 1), new QuarrieView(0, 0, 1), new QuarrieView(0, 0, 1)));
        }
        boardView.getSiteView().setTilesInSite(tiles);
        // Add some hexagons to the grid view
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                boardView.getGridView().addHexagon(new QuarrieView(i, j, 1));
            }
        }
        boardView.setVisible(true);
        JFrame frame = new JFrame();
        frame.add(boardView);
        frame.setSize(boardView.getSize());
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
