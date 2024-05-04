package view;

import model.Player;
import view.main.App;
import view.main.states.GameOverState;
import view.ui.BoardUI;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * Panel for displaying the game board.
 * It takes care of displaying the scrolling board and the site to make the game
 * playable.
 */
public class BoardView extends JPanel implements View, KeyListener {

    // Fields
    private ScrollableGridView currentGridView;
    private EndTurnPlayerLabel endTurnPlayerLabel;
    private final ArrayList<ScrollableGridView> gridViews = new ArrayList<>();
    private SiteView siteView;
    private JLayeredPane gamePanel = new JLayeredPane();
    private JPanel borderGamePanel = new JPanel(new BorderLayout());
    private ScoreDetails scoreDetails;
    private final CardLayout gameSwitch = new CardLayout();
    private BoardUI boardUI;
    private JButton pauseButton;
    private JButton infoButton;
    private final CardLayout switchGrids = new CardLayout();
    private final JPanel cardPanel = new JPanel(switchGrids);
    private SoundEffect pauseButtonClickSound;
    private String[] playerNames;

    /**
     * Constructor for the BoardView.
     *
     * @param maxHexagons  The maximum number of hexagons to be displayed on the
     *                     board.
     * @param siteCapacity The number of tiles that can be stored in the site.
     */
    public BoardView(int maxHexagons, ArrayList<Player> players, int siteCapacity) {
        setupView();
        int numPlayers = players.size();
        initializeGridViews(maxHexagons, numPlayers);
        initializeSiteView(siteCapacity);
        setupLayeredPane();
        setupBottomPanel(numPlayers);
        playerNames = new String[players.size()];
        for (Player player : players) {
            playerNames[players.indexOf(player)] = player.getName();
        }

        // Create a CountDownLatch with the number of workers in GridView
        CountDownLatch latch = addHexagonOulineInBackground(maxHexagons);

        // Display loading dialog while workers are running
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            private JDialog dialog;

            @Override
            protected Void doInBackground() throws Exception {
                // Show loading dialog
                SwingUtilities.invokeLater(() -> {
                    dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(App.getInstance().getScreen()),
                            "Loading", true);
                    dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                    dialog.setLayout(new FlowLayout());
                    dialog.add(new JLabel("Loading..."));
                    dialog.setModal(true);
                    dialog.pack();
                    dialog.setLocationRelativeTo(App.getInstance().getScreen());
                    dialog.setVisible(true);
                });

                latch.await(); // Wait for workers to finish
                // Add BoardView to screen
                SwingUtilities.invokeLater(() -> {
                    App.getInstance().getScreen().add(BoardView.this, BorderLayout.CENTER);
                    App.getInstance().getScreen().revalidate();
                    BoardView.this.requestFocusInWindow();
                    BoardView.this.addKeyListener(BoardView.this);
                });

                return null;
            }

            @Override
            protected void done() {
                // Hide loading dialog
                SwingUtilities.invokeLater(() -> {
                    dialog.setVisible(false);
                    dialog.dispose();
                });
            }
        };
        worker.execute(); // Start the SwingWorker
    }

    private CountDownLatch addHexagonOulineInBackground(int maxHexagons) {
        CountDownLatch latch = new CountDownLatch(gridViews.size());
        ArrayList<Color> playerColors = new ArrayList<>();
        playerColors.add(Color.BLUE);
        playerColors.add(Color.RED);
        playerColors.add(Color.GREEN.darker());
        playerColors.add(Color.MAGENTA);
        // Create a SwingWorker for each GridView
        for (ScrollableGridView gridView : gridViews) {
            SwingWorker<Void, HexagonOutline> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                    // Generate hexagons
                    for (int q = -maxHexagons; q <= maxHexagons; q++) {
                        for (int r = -maxHexagons; r <= maxHexagons; r++) {
                            if (Math.abs(q + r) <= maxHexagons) {
                                HexagonOutline hexagon = new HexagonOutline(q, r, 0, GridView.hexagonSize, playerColors.get(gridViews.indexOf(gridView)));
                                publish(hexagon);
                            }
                        }
                    }
                    return null;
                }

                @Override
                protected void process(List<HexagonOutline> chunks) {
                    // Add generated hexagons to the gridView
                    for (HexagonOutline hexagon : chunks) {
                        gridView.addHexagon(hexagon);
                    }
                }

                @Override
                protected void done() {
                    latch.countDown(); // Decrement the latch count
                }
            };
            worker.execute(); // Start the SwingWorker
        }
        return latch;
    }

    @SuppressWarnings("removal")
    private void setupView() {
        setLayout(gameSwitch);
        this.setPreferredSize(App.getInstance().getScreen().getPreferredSize());
        gamePanel.setOpaque(false);
        scoreDetails = new ScoreDetails();
        // Fuck you Swing bugged piece of software when I CALL FUCKIN INTEGER IT WORKS BUT NOT WITH AN INT ???
        gamePanel.add(scoreDetails, new Integer(1));
        scoreDetails.setLocation(App.getInstance().getWidth() - App.getInstance().getScreen().getWidth() / 5, 0);
        gamePanel.add(borderGamePanel, new Integer(0));
        borderGamePanel.setBounds(0, 0, App.getInstance().getScreen().getWidth(), App.getInstance().getScreen().getHeight());
        borderGamePanel.setOpaque(false);
        this.add(gamePanel, "game");
        gamePanel.setBounds(0, 0, App.getInstance().getScreen().getWidth(), App.getInstance().getScreen().getHeight());
        this.add(new PausePanel(this), "pause");
        gameSwitch.show(this, "game");
        setOpaque(true);
        setFocusable(true);
        pauseButtonClickSound = new SoundEffect("/GameButton.wav");
        gamePanel.moveToFront(scoreDetails);
        gamePanel.moveToBack(borderGamePanel);
    }

    private void initializeGridViews(int maxHexagons, int numPlayers) {
        for (int i = 0; i < numPlayers; i++) {
            ScrollableGridView gridView = new ScrollableGridView(maxHexagons);
            gridViews.add(gridView);
            cardPanel.add(gridView, Integer.toString(i));
        }
        currentGridView = gridViews.get(0); // Set initial gridView
    }

    private void initializeSiteView(int siteCapacity) {
        siteView = new SiteView(siteCapacity);
        borderGamePanel.add(siteView, BorderLayout.WEST);
    }

    private void setupLayeredPane() {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.add(cardPanel, 0);
        layeredPane.setSize(cardPanel.getPreferredSize());
        cardPanel.setBounds(0, 0, cardPanel.getPreferredSize().width, cardPanel.getPreferredSize().height);
        endTurnPlayerLabel = new EndTurnPlayerLabel();
        layeredPane.add(endTurnPlayerLabel, 1);
        layeredPane.moveToFront(endTurnPlayerLabel);
        borderGamePanel.add(layeredPane, BorderLayout.CENTER);
    }

    private void setupBottomPanel(int numPlayers) {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        boardUI = new BoardUI(numPlayers);
        bottomPanel.add(boardUI, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        infoButton = createStyledButton("?");
        infoButton.setPreferredSize(new Dimension(85, 85));
        infoButton.addActionListener(e -> {
            pauseButtonClickSound.play();
            scoreDetails.setSize(App.getInstance().getScreen().getWidth() / 5, App.getInstance().getScreen().getHeight());
            scoreDetails.validate();
            scoreDetails.repaint();
        });
        pauseButton = createStyledButton("||");
        pauseButton.setPreferredSize(new Dimension(85, 85));
        pauseButton.addActionListener(e -> {
            pauseButtonClickSound.play();
            showPauseMenu();
        });
        buttonPanel.add(infoButton);
        buttonPanel.add(pauseButton);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);
        borderGamePanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    // Methods

    /**
     * Set the selected tile.
     *
     * @param tile The selected tile.
     */
    public void setSelectedTile(TileView tile) {
        currentGridView.setSelectedTile(tile);
    }

    public void resumeGame(){
        gameSwitch.show(this, "game");
    }

    /**
     * Show the next GridView and pass turn.
     */
    public void nextTurn() {
        int index = gridViews.indexOf(currentGridView);
        // Play the end turn animation
        getGridView().removeSelectedTile();
        freeze();
        endTurnPlayerLabel.play(playerNames[(index + 1) % gridViews.size()]);
    }

    public void freeze() {
        currentGridView.disableListeners();
        siteView.disableListeners();
        pauseButton.setEnabled(false);
        infoButton.setEnabled(false);
        this.setEnabled(false);
    }

    public void unfreeze() {
        currentGridView.enableListeners();
        siteView.enableListeners();
        pauseButton.setEnabled(true);
        infoButton.setEnabled(true);
        this.setEnabled(true);
        this.requestFocusInWindow();
        int index = gridViews.indexOf(currentGridView);
        index = (index + 1) % gridViews.size();
        currentGridView = gridViews.get(index);
    }

    public void displayNextBoard() {
        switchGrids.show(cardPanel, String.valueOf(gridViews.indexOf(currentGridView)));
        getGridView().removeSelectedTile();
    }

    /**
     * Get the filled hexagons in the current grid view.
     *
     * @return An array of filled hexagons.
     */
    public HexagonView[] getFilledHexagons() {
        HexagonView[] filledHexagons = currentGridView.getFilledHexagons();
        // Check for null values
        for (HexagonView hexagon : filledHexagons) {
            if (hexagon == null) {
                return null;
            }
        }
        return filledHexagons;
    }

    // Getter methods
    public ScrollableGridView getGridView() {
        return currentGridView;
    }

    public SiteView getSiteView() {
        return siteView;
    }

    public BoardUI getBoardUI() {
        return boardUI;
    }

    public ArrayList<ScrollableGridView> getGridViews() {
        return gridViews;
    }

    /**
     * Show the pause menu.
     */
    private void showPauseMenu() {
        System.out.println("Showing pause menu");
        SwingUtilities.invokeLater(() -> {
            gameSwitch.show(this, "pause");
        });
    }

    /**
     * Create a styled JButton.
     *
     * @param text The text on the button.
     * @return A styled JButton.
     */
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

    // KeyListener methods
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            showPauseMenu();
        }
        if (e.getKeyCode() == KeyEvent.VK_R) {
            currentGridView.rotateSelectedTile();
        }
        if (e.getKeyCode() == KeyEvent.VK_C) {
            currentGridView.centerView();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not needed, but must be implemented due to KeyListener interface
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not needed, but must be implemented due to KeyListener interface
    }

    public void showGameOver(String winner) {
        GameOverState.getInstance().setWinner(winner);
        App.getInstance().appState.changeState(GameOverState.getInstance());
    }
}
