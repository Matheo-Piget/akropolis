package test;

import model.*;
import util.Point3D;
import org.junit.Test;
import static org.junit.Assert.*;

public class BoardTest {
    @Test
    public void testAddTile() {
        Board board = new Board();
        Quarrie validTile1 = new Quarrie(new Point3D(1, 1, 1));
        Quarrie validTile2 = new Quarrie(new Point3D(0, 2, 1));
        Quarrie validTile3 = new Quarrie(new Point3D(3, 2, 1));
        validTile1.setTileTrio(new TileTrio(validTile1, validTile2, validTile3));
        // We verify that we can add a tile to the board
        assertTrue(board.addTile(validTile1));
        // And that we can get it back
        assertEquals(validTile1, board.getTile(1, 1));
        assertEquals(validTile2, board.getTile(0, 2));
        assertEquals(validTile3, board.getTile(3, 2));
    }

    @Test
    public void testAddInvalidTile() {
        Board board = new Board();
        Quarrie invalidTile1 = new Quarrie(new Point3D(0, 1, 1));
        Quarrie invalidTile2 = new Quarrie(new Point3D(0, 2, 1));
        Quarrie invalidTile3 = new Quarrie(new Point3D(3, 2, 1));
        invalidTile1.setTileTrio(new TileTrio(invalidTile1, invalidTile2, invalidTile3));
        // We verify that we can't add a tile to the board if it's not next to another tile
        assertFalse(board.addTile(invalidTile1));
        // And that we can't get it back
        assertNotEquals(invalidTile1, board.getTile(0, 1));
        assertNotEquals(invalidTile2, board.getTile(0, 2));
        assertNotEquals(invalidTile3, board.getTile(3, 2));
    }

    @Test
    public void testOverlapTile(){
        Board board = new Board();
        District validTile1 = new District(new Point3D(0, 0, 1), DistrictColor.RED);
        District validTile2 = new District(new Point3D(-1, -1, 1), DistrictColor.BLUE);
        District validTile3 = new District(new Point3D(1, -1, 1), DistrictColor.GREEN);
        validTile1.setTileTrio(new TileTrio(validTile1, validTile2, validTile3));
        // We verify that we can add a tile to the board
        assertTrue(board.addTile(validTile1));
        // We verify that their height has been updated
        assertEquals(2, validTile1.getElevation());
        assertEquals(2, validTile2.getElevation());
        assertEquals(2, validTile3.getElevation());
        // And that we can get it back
        assertEquals(validTile1, board.getTile(0, 0));
        assertEquals(validTile2, board.getTile(-1, -1));
        assertEquals(validTile3, board.getTile(1, -1));
    }
}
