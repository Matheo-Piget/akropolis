package test;

import model.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BoardTest {
    @Test
    public void testAddTile() {
        Board board = new Board();
        Quarrie tile = new Quarrie(1, 0);
        board.addTile(tile);
        assertNotNull(board.getTile(1, 0));
    }

    @Test
    public void testAddInvalidTile() {
        Board board = new Board();
        Quarrie invalidTile1 = new Quarrie(0, 0);
        assertFalse(board.addTile(invalidTile1));
        Quarrie invalidTile2 = new Quarrie(5, 5);
        assertFalse(board.addTile(invalidTile2));
    }
}
