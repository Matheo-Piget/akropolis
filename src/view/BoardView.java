package view;

import view.main.App;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.List;
import javax.swing.*;
import java.awt.*;

/**
 * Panel for displaying the game board.
 * It takes care of diplaying the scrolling board and the site to make the game
 * playable.
 */
public class BoardView extends JPanel implements View, KeyListener {

    private ScrollableGridView currentGridView;
    private final ArrayList<ScrollableGridView> gridViews = new ArrayList<>();
    private SiteView siteView;
    private BoardUI boardUI;
    private CardLayout cardLayout = new CardLayout();
    private JPanel cardPanel = new JPanel(cardLayout);

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
        setFocusable(true);
        for (int i = 0; i < numPlayers; i++) {
            ScrollableGridView gridView = new ScrollableGridView(maxHexagons);
            gridViews.add(gridView);
            cardPanel.add(gridView, Integer.toString(i));
        }
        siteView = new SiteView(siteCapacity);
        add(cardPanel, BorderLayout.CENTER);
        currentGridView = (ScrollableGridView) cardPanel.getComponent(0);
        add(siteView, BorderLayout.WEST);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false); // Rend le JPanel transparent

        boardUI = new BoardUI();
        bottomPanel.add(boardUI, BorderLayout.CENTER);


        JPanel pausePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pausePanel.setOpaque(false); // Rend le JPanel transparent

        // Ajout du bouton pause au JPanel
        JButton pauseButton = createStyledButton("||");
        pauseButton.setPreferredSize(new Dimension(85, 85));
        pauseButton.addActionListener(e -> showPauseMenu());
        pausePanel.add(pauseButton);

        bottomPanel.add(pausePanel, BorderLayout.EAST);

        // Ajout du JPanel contenant le bouton pause en haut de la fenêtre
        add(bottomPanel, BorderLayout.SOUTH);

        // Create a CountDownLatch with the number of workers in GridView
        CountDownLatch latch = new CountDownLatch(gridViews.size());

        // Create a SwingWorker for each GridView
        for (ScrollableGridView gridView : gridViews) {
            SwingWorker<Void, HexagonOutline> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                    for (int q = -maxHexagons; q <= maxHexagons; q++) {
                        for (int r = -maxHexagons; r <= maxHexagons; r++) {
                            if (Math.abs(q + r) <= maxHexagons) {
                                HexagonOutline hexagon = new HexagonOutline(q, r, 0);
                                publish(hexagon);
                            }
                        }
                    }
                    return null;
                }

                @Override
                protected void process(List<HexagonOutline> chunks) {
                    for (HexagonOutline hexagon : chunks) {
                        gridView.addHexagon(hexagon);
                    }
                }

                @Override
                protected void done() {
                    // Cook pizza
                    // Decrement the latch count
                    latch.countDown();
                }
            };

            // Start the SwingWorker
            worker.execute();
        }

        // Create a SwingWorker to add the BoardView in a background thread
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            private JDialog dialog;

            @Override
            protected Void doInBackground() throws Exception {
                // Show the loading dialog
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

                latch.await();
                // Add the BoardView
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
                // Hide the loading dialog
                SwingUtilities.invokeLater(() -> {
                    dialog.setVisible(false);
                    dialog.dispose();
                });
            }
        };

        // Start the SwingWorker
        worker.execute();
    }

    /**
     * Set the selected tile
     * @param tile
     */
    public void setSelectedTile(TileView tile) {
        // Convert it to a movable object
        currentGridView.setSelectedTile(tile);
    }

    /**
     * show the next GridView, pass turn
     */
    public void nextTurn() {
        getGridView().removeSelectedTile();
        int index = gridViews.indexOf(currentGridView);
        index = (index + 1) % gridViews.size();
        currentGridView = gridViews.get(index);
        // Use CardLayout.show to switch to the next gridView
        cardLayout.show(cardPanel, String.valueOf(index));
        //System.out.println("Next turn");
    }

    public ScrollableGridView getGridView() {
        return currentGridView;
    }

    public SiteView getSiteView() {
        return siteView;
    }

    /**
     * show the pause menu
     */
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
    /**
     * created a styled JButton
     * @param text the text on the button
     * @return a JButton styled
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
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            App.getInstance().appState.getState().getBoardController().nextTurn();
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
