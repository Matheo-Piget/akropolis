package view;

import view.main.App;

import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;

/**
 * Panel for displaying the game board.
 * It takes care of diplaying the scrolling board and the site to make the game
 * playable.
 */
public class BoardView extends JPanel implements View {

    private ArrayList<ScrollableGridView> gridViews = new ArrayList<ScrollableGridView>();
    private ScrollableGridView currentGridView;
    private SiteView siteView;
    private BoardUI boardUI;
    private JButton pauseButton;

    /**
     * Constructor for the BoardView.
     * 
     * @param maxHexagons  The maximum number of hexagons to be displayed on the
     *                     board.
     * @param numPlayers   The number of players in the game.
     * 
     * @param siteCapacity The number of tiles that can be stored in the site.
     */
    public BoardView(int maxHexagons, int numPlayers, int siteCapacity) {
        setLayout(new BorderLayout());
        setOpaque(true);
        gridViews = new ArrayList<ScrollableGridView>();
        for (int i = 0; i < numPlayers; i++) {
            gridViews.add(new ScrollableGridView(maxHexagons));
        }
        siteView = new SiteView(siteCapacity);
        add(gridViews.get(0), BorderLayout.CENTER);
        currentGridView = gridViews.get(0);
        add(siteView, BorderLayout.WEST);
        boardUI = new BoardUI();
        add(boardUI, BorderLayout.AFTER_LAST_LINE);
        System.out.println("Adding board view to screen");
        App.getInstance().getScreen().add(this, BorderLayout.CENTER);
        App.getInstance().getScreen().revalidate();

        pauseButton = new JButton("⏸"); // Utilisation d'un symbole de pause
        pauseButton.setPreferredSize(new Dimension(50, 30));
        pauseButton.addActionListener(e -> showPauseMenu());

        this.add(pauseButton, BorderLayout.NORTH);
    }

    public void setSelectedTile(TileView tile) {
        // Convert it to a movable object
        MovableTileView movableTile = new MovableTileView(tile);
        currentGridView.setSelectedTile(movableTile);
    }

    public void nextTurn() {
        getGridView().removeSelectedTile();
        int index = gridViews.indexOf(currentGridView);
        index = (index + 1) % gridViews.size();
        currentGridView = gridViews.get(index);
        remove(0);
        add(currentGridView, BorderLayout.CENTER);
        revalidate();
    }

    public ScrollableGridView getGridView() {
        return currentGridView;
    }

    public SiteView getSiteView() {
        return siteView;
    }

    private void showPauseMenu() {
        JDialog pauseMenu = new JDialog(App.getInstance(), "Pause", true);
        pauseMenu.setLayout(new GridLayout(2, 1));
        pauseMenu.setSize(200, 100);
        pauseMenu.setLocationRelativeTo(this);

        JButton resumeButton = new JButton("Reprendre");
        resumeButton.addActionListener(e -> pauseMenu.dispose());

        JButton quitButton = new JButton("Quitter");
        quitButton.addActionListener(e -> {
            pauseMenu.dispose();
            App.getInstance().exitToMainMenu();
        });

        pauseMenu.add(resumeButton);
        pauseMenu.add(quitButton);

        pauseMenu.setVisible(true);
    }

    // For testing purposes
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BoardView boardView = new BoardView(40, 5, 3);
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
            App.getInstance().getScreen().revalidate();
        });
    }

    public BoardUI getBoardUI() {
        return boardUI;
    }
}
