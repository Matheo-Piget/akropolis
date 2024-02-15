package test;

import model.*;
import util.Point3D;
import org.junit.Test;
import static org.junit.Assert.*;

public class BoardTest {
    @Test
    public void testAddTile() {
        Board board = new Board();
        Quarrie validTile1 = new Quarrie(new Point3D(0, 2, 1));
        Quarrie validTile2 = new Quarrie(new Point3D(-1, 3, 1));
        Quarrie validTile3 = new Quarrie(new Point3D(1, 2, 1));
        TileTrio tileTrio1 = new TileTrio(validTile1, validTile2, validTile3);
        // We verify that we can add a tile to the board
        assertTrue(board.addTile(tileTrio1));
        // And that we can get it back
        assertEquals(validTile1, board.getTile(0, 2));
        assertEquals(validTile2, board.getTile(-1, 3));
        assertEquals(validTile3, board.getTile(1, 2));
    }

    @Test
    public void testAddInvalidTile() {
        Board board = new Board();
        Quarrie invalidTile1 = new Quarrie(new Point3D(0, 1, 1));
        Quarrie invalidTile2 = new Quarrie(new Point3D(-1, 2, 1));
        Quarrie invalidTile3 = new Quarrie(new Point3D(1, 1, 1));
        TileTrio tileTrio1 = new TileTrio(invalidTile1, invalidTile2, invalidTile3);
        // We verify that we can't add a tile to the board if it's not correctly overlapping
        assertFalse(board.addTile(tileTrio1));
        // And that we can't get it back
        assertNotEquals(invalidTile1, board.getTile(0, 1));
        assertNotEquals(invalidTile2, board.getTile(0, 2));
        assertNotEquals(invalidTile3, board.getTile(3, 2));

        // Then we will verify we can't add a tile that has no neighbors
        Quarrie invalidTile4 = new Quarrie(new Point3D(0, 3, 1));
        Quarrie invalidTile5 = new Quarrie(new Point3D(-1, 3, 1));
        Quarrie invalidTile6 = new Quarrie(new Point3D(1, 2, 1));
        TileTrio tileTrio2 = new TileTrio(invalidTile4, invalidTile5, invalidTile6);
        // We verify that we can't add a tile to the board if it has no neighbors
        assertFalse(board.addTile(tileTrio2));
        // And that we can't get it back
        assertNotEquals(invalidTile4, board.getTile(0, 3));
        assertNotEquals(invalidTile5, board.getTile(-1, 3));
        assertNotEquals(invalidTile6, board.getTile(1, 2));

        // Finally we will verify we cant overlap tiles of the same type more than once
        Quarrie invalidTile7 = new Quarrie(new Point3D(0, 0, 1));
        Quarrie invalidTile8 = new Quarrie(new Point3D(-1, -1, 1));
        Quarrie invalidTile9 = new Quarrie(new Point3D(1, 0, 1));
        TileTrio tileTrio3 = new TileTrio(invalidTile7, invalidTile8, invalidTile9);
        // We verify that we can't add a tile to the board if it's not respecting the rules
        assertFalse(board.addTile(tileTrio3));
        // And that we can't get it back
        assertNotEquals(invalidTile7, board.getTile(0, 0));
        assertNotEquals(invalidTile8, board.getTile(-1, -1));
        assertNotEquals(invalidTile9, board.getTile(1, 0));
    }

    @Test
    public void testOverlapTile(){
        Board board = new Board();
        District validTile1 = new District(new Point3D(0, 0, 1), DistrictColor.RED);
        District validTile2 = new District(new Point3D(-1, -1, 1), DistrictColor.BLUE);
        District validTile3 = new District(new Point3D(1, 0, 1), DistrictColor.GREEN);
        TileTrio tileTrio1 = new TileTrio(validTile1, validTile2, validTile3);
        // We verify that we can add a tile to the board
        assertTrue(board.addTile(tileTrio1));
        // We verify that their height has been updated
        assertEquals(2, validTile1.getElevation());
        assertEquals(2, validTile2.getElevation());
        assertEquals(2, validTile3.getElevation());
        // And that we can get it back
        assertEquals(validTile1, board.getTile(0, 0));
        assertEquals(validTile2, board.getTile(-1, -1));
        assertEquals(validTile3, board.getTile(1, 0));
    }

    @Test
    public void testTotalScore(){
        // This test is a bit more complex, it's wrong to test the score of the grid here 
        // I should fix this later
        Board board = new Board();
        District validTile1 = new District(new Point3D(0, 0, 1), DistrictColor.RED);
        District validTile2 = new District(new Point3D(-1, -1, 1), DistrictColor.BLUE);
        District validTile3 = new District(new Point3D(1, -1, 1), DistrictColor.GREEN);

        District validTile4 = new District(new Point3D(0, 2, 1), DistrictColor.BLUE);
        District validTile5 = new District(new Point3D(-1, 3, 1), DistrictColor.BLUE);
        District validTile6 = new District(new Point3D(1, 3, 1), DistrictColor.BLUE);

        TileTrio tileTrio1 = new TileTrio(validTile1, validTile2, validTile3);
        TileTrio tileTrio2 = new TileTrio(validTile4, validTile5, validTile6);
        // We verify that we can add a tile to the board
        assertTrue(board.addTile(tileTrio1));
        assertTrue(board.addTile(tileTrio2));
        // We verify that their height has been updated
        assertEquals(2, validTile1.getElevation());
        assertEquals(2, validTile2.getElevation());
        assertEquals(2, validTile3.getElevation());
        assertEquals(1, validTile4.getElevation());
        assertEquals(1, validTile5.getElevation());
        assertEquals(1, validTile6.getElevation());
        // And that we can get it back
        assertEquals(validTile1, board.getTile(0, 0));
        assertEquals(validTile2, board.getTile(-1, -1));
        assertEquals(validTile3, board.getTile(1, -1));
        assertEquals(validTile4, board.getTile(0, 2));
        assertEquals(validTile5, board.getTile(-1, 3));
        assertEquals(validTile6, board.getTile(1, 3));
        // We verify that the score is correct
        assertEquals(5, board.getScore());
    }
}
