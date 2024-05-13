package view;

import model.Player;
import util.SoundManager;
import view.main.App;
import view.main.states.GameOverState;
import view.ui.BoardUI;
import view.ui.TriangleButton;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import util.Pair;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * Panel for displaying the game board.
 * It takes care of displaying the scrolling board and the site to make the game
 * playable.
 */
public class BoardView extends JPanel implements View {
    // DONT USE KEYLISTENER, USE KEYBINDINGS INSTEAD BECAUSE KEYLISTNER IS NOT
    // RELIABLE AND CAN BE OVERRIDEN BY OTHER COMPONENTS, THEY CAN LOSE FOCUS

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
    private TriangleButton infoButton;
    private final CardLayout switchGrids = new CardLayout();
    private final JPanel cardPanel = new JPanel(switchGrids);
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
        setupKeyBindings();
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
     * Add hexagon outlines to the grid views in the background.
     *
     * @param maxHexagons The maximum number of hexagons to be displayed on the
     *                    board.
     * @return A CountDownLatch to wait for the workers to finish.
     */
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
                                HexagonOutline hexagon = new HexagonOutline(q, r, 0, GridView.hexagonSize,
                                        playerColors.get(gridViews.indexOf(gridView)));
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

    /**
     * Setup the key bindings to listen for key events.
     */
    private void setupKeyBindings(){
        Action escapeAction = new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                showPauseMenu();
            }
        };
        Action rotateAction = new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                currentGridView.rotateSelectedTile();
            }
        };
        Action centerAction = new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                currentGridView.centerView();
            }
        };
        ActionMap actionMap = getActionMap();
        actionMap.put("escape", escapeAction);
        actionMap.put("rotate", rotateAction);
        actionMap.put("center", centerAction);

        InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escape");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "rotate");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, 0), "center");
    }

    /**
     * Setup the view.
     */
    private void setupView() {
        setLayout(gameSwitch);
        this.setPreferredSize(App.getInstance().getScreen().getPreferredSize());
        gamePanel.setOpaque(false);
        scoreDetails = new ScoreDetails();
        // Fuck you Swing bugged piece of software when I CALL FUCKIN INTEGER IT WORKS
        // BUT NOT WITH AN INT ???
        gamePanel.add(scoreDetails, Integer.valueOf(1));
        scoreDetails.setLocation(App.getInstance().getWidth() - App.getInstance().getScreen().getWidth() / 5, 0);
        gamePanel.add(borderGamePanel, Integer.valueOf(0));
        borderGamePanel.setBounds(0, 0, App.getInstance().getScreen().getWidth(),
                App.getInstance().getScreen().getHeight());
        borderGamePanel.setOpaque(false);
        this.add(gamePanel, "game");
        gamePanel.setBounds(0, 0, App.getInstance().getScreen().getWidth(), App.getInstance().getScreen().getHeight());
        this.add(new PausePanel(this), "pause");
        gameSwitch.show(this, "game");
        setOpaque(true);
        setFocusable(true);
        gamePanel.moveToFront(scoreDetails);
        gamePanel.moveToBack(borderGamePanel);
    }

    /**
     * initialize the grisviews
     * 
     * @param maxHexagons max hexagons in the gridview
     * @param numPlayers  number of players
     */
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

    /**
     * Setup the layered pane.
     */
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

    /**
     * Setup the bottom panel.
     *
     * @param numPlayers The number of players.
     */
    private void setupBottomPanel(int numPlayers) {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        boardUI = new BoardUI(numPlayers, scoreDetails);
        bottomPanel.add(boardUI, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        infoButton = new TriangleButton("Score");
        infoButton.setBackground(new Color(255, 215, 0));
        infoButton.setSize(new Dimension(100, 150));
        infoButton.setFont(new Font("Arial", Font.BOLD, 22));
        // Change the shape of the button to be a triangle
        infoButton.addActionListener(e -> {
            SoundManager.playSound("gameButton2");
            scoreDetails.setSize(App.getInstance().getScreen().getWidth() / 5,
                    App.getInstance().getScreen().getHeight() - bottomPanel.getHeight());
            scoreDetails.validate();
            scoreDetails.repaint();
        });
        gamePanel.add(infoButton, Integer.valueOf(1));
        infoButton.setLocation(App.getInstance().getWidth() - infoButton.getWidth(),
                App.getInstance().getHeight() / 2 - infoButton.getHeight());
        infoButton.repaint();
        pauseButton = createStyledButton("||");
        pauseButton.setPreferredSize(new Dimension(85, 85));
        pauseButton.addActionListener(e -> {
            SoundManager.playSound("gameButton2");
            showPauseMenu();
        });
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

    public void resumeGame() {
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

    /**
     * Freeze the game.
     */
    public void freeze() {
        currentGridView.disableListeners();
        siteView.disableListeners();
        pauseButton.setEnabled(false);
        infoButton.setEnabled(false);
        this.setEnabled(false);
    }

    /**
     * Unfreeze the game.
     */
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

    public void showGameOver(List<Pair<String,Integer>> players) {
        GameOverState.getInstance().setRanking(players);
        App.getInstance().appState.changeState(GameOverState.getInstance());
    }
}
