package test;

import model.*;
import util.BetterAssert;

// This class will be used to test the code
// TODO : Ask the teacher if we should use JUnit or not
public class Test {
    // Future tests to test the model and the controller will be written here
    // Maybe we will use JUnit to test the code
    // We could break the tests into different classes to make it more readable
    public static void main(String[] args) {
        Board board = new Board();
        // We verify that the starting tile is placed at the center of the grid
        BetterAssert.assertIsTrue(board.getTile(0, 0) != null);
        BetterAssert.assertIsTrue(board.getTile(1, 1) != null);
        BetterAssert.assertIsTrue(board.getTile(-1, 1) != null);
        // Then we will add a tile
        Quarrie tile = new Quarrie(1, 0);
        board.addTile(tile);
        // We verify that the tile is placed at the right position
        BetterAssert.assertIsTrue(board.getTile(1, 0) != null);
        // We will try to add an invalid tile
        Quarrie invalidTile1 = new Quarrie(0, 0);
        BetterAssert.assertIsTrue(!board.addTile(invalidTile1));
        // We will try the case where the tile is not placed next to another tile
        Quarrie invalidTile2 = new Quarrie(5, 5);
        BetterAssert.assertIsTrue(!board.addTile(invalidTile2));
        // Display the existing tiles
        board.display();
    }
}
