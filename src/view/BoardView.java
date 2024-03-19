package view;

import view.main.App;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;

/**
 * Panel for displaying the game board.
 * It takes care of diplaying the scrolling board and the site to make the game
 * playable.
 */
public class BoardView extends JPanel implements View, KeyListener {

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

        JPanel pausePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pausePanel.setOpaque(false); // Rend le JPanel transparent

        // Ajout du bouton pause au JPanel
        pauseButton = createStyledButton("||");
        pauseButton.setPreferredSize(new Dimension(50, 50));
        pauseButton.addActionListener(e -> showPauseMenu());
        pausePanel.add(pauseButton);

        // Ajout du JPanel contenant le bouton pause en haut de la fenêtre
        add(pausePanel, BorderLayout.NORTH);
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
        // Création du dialogue de pause
        JDialog pauseMenu = new JDialog(App.getInstance(), "Pause", true);
        pauseMenu.setLayout(new GridLayout(2, 1));
        pauseMenu.setSize(200, 100);
        pauseMenu.setLocationRelativeTo(this);

        // Bouton pour reprendre le jeu
        JButton resumeButton = createStyledButton("Reprendre");
        resumeButton.addActionListener(e -> pauseMenu.dispose());

        // Bouton pour quitter le jeu
        JButton quitButton = createStyledButton("Quitter");
        quitButton.addActionListener(e -> {
            pauseMenu.dispose();
            App.getInstance().exitToMainMenu();
        });

        // Ajout des boutons au dialogue
        pauseMenu.add(resumeButton);
        pauseMenu.add(quitButton);

        // Affichage du dialogue de pause
        pauseMenu.setVisible(true);
    }


    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 50));
        button.setBackground(new Color(255, 215, 0));
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 2));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 235, 59));
                button.setForeground(Color.BLACK);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 215, 0));
                button.setForeground(Color.BLACK);
            }
        });
        return button;
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

    @Override
    public void keyPressed(KeyEvent e) {
        // Check if the pressed key is Escape (keyCode 27)
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            // Pause the game
            showPauseMenu();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not needed, but must be implemented due to KeyListener interface
    }

    /**
     * Invoked when a key has been typed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key typed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }
}
