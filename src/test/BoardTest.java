package test;

import model.*;
import util.Point3D;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

/**
 * Tests for the Board class.
 */
public class BoardTest {
    @Test
    public void testAddTile() {
        Player player = new Player("TestPlayer");
        Board board = new Board(List.of(player));
        Quarries validHexagon1 = new Quarries(new Point3D(0, -2));
        Quarries validHexagon2 = new Quarries(new Point3D(-1, -1));
        Quarries validHexagon3 = new Quarries(new Point3D(1, -1));
        Tile tileTrio1 = new Tile(validHexagon1, validHexagon2, validHexagon3);
        // We verify that we can add a tile to the board
        assertTrue(board.addTile(tileTrio1));
        // And that we can get it back
        assertEquals(validHexagon1, board.getHexagon(0, -2));
        assertEquals(validHexagon2, board.getHexagon(-1, -1));
        assertEquals(validHexagon3, board.getHexagon(1, -1));
    }

    @Test
    public void testAddInvalidTile() {
        Player player = new Player("TestPlayer");
        Board board = new Board(List.of(player));
        Quarries invalidHexagon1 = new Quarries(new Point3D(0, -1));
        Quarries invalidHexagon2 = new Quarries(new Point3D(0, 0));
        Quarries invalidHexagon3 = new Quarries(new Point3D(1, 0));
        Tile tileTrio1 = new Tile(invalidHexagon1, invalidHexagon2, invalidHexagon3);
        // We verify that we can't add a tile to the board if it's not correctly
        // overlapping
        assertFalse(board.addTile(tileTrio1));
        // And that we can't get it back
        assertNotEquals(invalidHexagon1, board.getHexagon(0, -1));
        assertNotEquals(invalidHexagon2, board.getHexagon(0, 0));
        assertNotEquals(invalidHexagon3, board.getHexagon(1, 0));

        // Then we will verify we can't add a tile that has no neighbors
        Quarries invalidHexagon4 = new Quarries(new Point3D(0, 3));
        Quarries invalidHexagon5 = new Quarries(new Point3D(-1, 3));
        Quarries invalidHexagon6 = new Quarries(new Point3D(1, 3));
        Tile tileTrio2 = new Tile(invalidHexagon4, invalidHexagon5, invalidHexagon6);
        // We verify that we can't add a tile to the board if it has no neighbors
        assertFalse(board.addTile(tileTrio2));
        // And that we can't get it back
        assertNotEquals(invalidHexagon4, board.getHexagon(0, 3));
        assertNotEquals(invalidHexagon5, board.getHexagon(-1, 3));
        assertNotEquals(invalidHexagon6, board.getHexagon(1, 3));

        // Finally we will verify we cant overlap hexagons of the same type more than
        // once
        Quarries invalidTile7 = new Quarries(new Point3D(0, 0));
        Quarries invalidTile8 = new Quarries(new Point3D(1, 0));
        Quarries invalidTile9 = new Quarries(new Point3D(-1, 1));
        Tile tileTrio3 = new Tile(invalidTile7, invalidTile8, invalidTile9);
        // We verify that we can't add a tile to the board if it's not respecting the
        // rules
        assertFalse(board.addTile(tileTrio3));
        // And that we can't get it back
        assertNotEquals(invalidTile7, board.getHexagon(0, 0));
        assertNotEquals(invalidTile8, board.getHexagon(1, 0));
        assertNotEquals(invalidTile9, board.getHexagon(-1, 1));
    }

    @Test
    public void testOverlapTile() {
        Player player = new Player("TestPlayer");
        Board board = new Board(List.of(player));
        District validHexagon1 = new District(new Point3D(0, 0), DistrictColor.RED);
        District validHexagon2 = new District(new Point3D(1, 0), DistrictColor.BLUE);
        District validHexagon3 = new District(new Point3D(-1, 1), DistrictColor.GREEN);
        Tile tileTrio1 = new Tile(validHexagon1, validHexagon2, validHexagon3);
        // We verify that we can add a tile to the board
        assertTrue(board.addTile(tileTrio1));
        // We verify that their height has been updated
        assertEquals(2, validHexagon1.getElevation());
        assertEquals(2, validHexagon2.getElevation());
        assertEquals(2, validHexagon3.getElevation());
        // And that we can get it back
        assertEquals(validHexagon1, board.getHexagon(0, 0));
        assertEquals(validHexagon2, board.getHexagon(1, 0));
        assertEquals(validHexagon3, board.getHexagon(-1, 1));
    }

    @Test
    public void testScore() {
        Player player = new Player("TestPlayer");
        Board board = new Board(List.of(player));

        // Add tiles to cover multiple districts with different heights
        District district1 = new District(new Point3D(0, 0), DistrictColor.RED);
        District district2 = new District(new Point3D(-1, -1), DistrictColor.BLUE);
        District district3 = new District(new Point3D(1, -1), DistrictColor.GREEN);
        District district4 = new District(new Point3D(0, 2), DistrictColor.BLUE);
        District district5 = new District(new Point3D(-1, 3), DistrictColor.BLUE);
        District district6 = new District(new Point3D(1, 3), DistrictColor.BLUE);
        Tile tile1 = new Tile(district1, district2, district3);
        Tile tile2 = new Tile(district4, district5, district6);
        board.addTile(tile1);
        board.addTile(tile2);

        // Add more tiles to create complex overlapping scenarios
        // Add more complex tile configurations to test scoring
        District district7 = new District(new Point3D(2, 0), DistrictColor.RED);
        District district8 = new District(new Point3D(1, -1), DistrictColor.BLUE);
        District district9 = new District(new Point3D(3, -1), DistrictColor.GREEN);
        District district10 = new District(new Point3D(2, 2), DistrictColor.BLUE);
        District district11 = new District(new Point3D(3, 3), DistrictColor.BLUE);
        District district12 = new District(new Point3D(4, 3), DistrictColor.BLUE);
        Tile tile3 = new Tile(district7, district8, district9);
        Tile tile4 = new Tile(district10, district11, district12);
        board.addTile(tile3);
        board.addTile(tile4);

        // Ensure that the score is calculated correctly
        assertEquals(2, board.getScore(player));
    }

    public void testScoreWithPlaces() {
        Player player = new Player("TestPlayer");
        Board board = new Board(List.of(player));

        // Add tiles to cover multiple districts with different heights
        District district1 = new District(new Point3D(0, 0), DistrictColor.RED);
        District district2 = new District(new Point3D(-1, -1), DistrictColor.BLUE);
        District district3 = new District(new Point3D(1, -1), DistrictColor.GREEN);
        District district4 = new District(new Point3D(0, 2), DistrictColor.BLUE);
        District district5 = new District(new Point3D(-1, 3), DistrictColor.BLUE);
        District district6 = new District(new Point3D(1, 3), DistrictColor.BLUE);
        Tile tile1 = new Tile(district1, district2, district3);
        Tile tile2 = new Tile(district4, district5, district6);
        board.addTile(tile1);
        board.addTile(tile2);

        // Add more tiles to create complex overlapping scenarios
        // Add more complex tile configurations to test scoring
        District district7 = new District(new Point3D(2, 0), DistrictColor.RED);
        District district8 = new District(new Point3D(1, -1), DistrictColor.BLUE);
        District district9 = new District(new Point3D(3, -1), DistrictColor.GREEN);
        District district10 = new District(new Point3D(2, 2), DistrictColor.BLUE);
        District district11 = new District(new Point3D(3, 3), DistrictColor.BLUE);
        District district12 = new District(new Point3D(4, 3), DistrictColor.BLUE);
        Tile tile3 = new Tile(district7, district8, district9);
        Tile tile4 = new Tile(district10, district11, district12);
        board.addTile(tile3);
        board.addTile(tile4);

        // Add place tiles for scoring
        Place place1 = new Place(new Point3D(1, 0), 3, DistrictColor.BLUE);
        Place place2 = new Place(new Point3D(2, 1), 2, DistrictColor.RED);
        Place place3 = new Place(new Point3D(1, 2), 4, DistrictColor.GREEN);
        board.addTile(new Tile(place1, place2, place3));

        // Ensure that the score is calculated correctly
        assertEquals(26, board.getScore(player));
    }

    @Test
    public void testScoreWithMultiplePlayers() {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        Board board = new Board(List.of(player1, player2));

        // Add tiles to cover multiple districts with different heights for player 1 we
        // dont have stars so we wont get any points
        District district1 = new District(new Point3D(0, 0), DistrictColor.RED);
        District district2 = new District(new Point3D(-1, -1), DistrictColor.BLUE);
        District district3 = new District(new Point3D(1, -1), DistrictColor.GREEN);
        Tile tile1 = new Tile(district1, district2, district3);
        board.addTile(tile1);

        // Add tiles to cover multiple districts with different heights for player 2
        District district4 = new District(new Point3D(0, 2), DistrictColor.BLUE);
        District district5 = new District(new Point3D(-1, 3), DistrictColor.BLUE);
        District district6 = new District(new Point3D(1, 3), DistrictColor.BLUE);
        Tile tile2 = new Tile(district4, district5, district6);
        board.addTile(tile2);

        // Ensure that the score is calculated correctly for each player
        assertEquals(1, board.getScore(player1));
        assertEquals(2, board.getScore(player2));
    }

    @Test
    public void testScoreWithEmptyBoard() {
        Player player = new Player("TestPlayer");
        Board board = new Board(List.of(player));

        // Ensure that the score is 1 when the board is empty because of the one rock he
        // starts with
        assertEquals(1, board.getScore(player));
    }

    @Test
    public void testScoreWithSingleDistrict() {
        Player player = new Player("TestPlayer");
        Board board = new Board(List.of(player));

        // Add a single district to the board
        District district = new District(new Point3D(0, 0), DistrictColor.RED);
        Tile tile = new Tile(district, new Quarries(new Point3D(-1, 0)), new Quarries(new Point3D(1, 0)));
        board.addTile(tile);

        // Ensure that the score is equal to the district's height
        assertEquals(1, board.getScore(player));
    }

    @Test
    public void testScoreWithSinglePlace() {
        Player player = new Player("TestPlayer");
        Board board = new Board(List.of(player));

        // Add a single place to the board
        Place place = new Place(new Point3D(0, 0), 3, DistrictColor.RED);
        Tile tile = new Tile(place, new Quarries(new Point3D(-1, 0)), new Quarries(new Point3D(1, 0)));
        board.addTile(tile);

        // Ensure that the score is equal to the place's stars
        assertEquals(1, board.getScore(player));
    }

    @Test
    public void testScoreWithMultipleLevels() {
        Player player = new Player("TestPlayer");
        Board board = new Board(List.of(player));

        // Add tiles to cover multiple districts with different heights
        District district1 = new District(new Point3D(0, 0), DistrictColor.RED);
        District district2 = new District(new Point3D(-1, -1), DistrictColor.BLUE);
        District district3 = new District(new Point3D(1, -1), DistrictColor.GREEN);
        District district4 = new District(new Point3D(0, 1), DistrictColor.RED);
        District district5 = new District(new Point3D(-1, 0), DistrictColor.BLUE);
        District district6 = new District(new Point3D(1, 0), DistrictColor.GREEN);
        Tile tile1 = new Tile(district1, district2, district3);
        Tile tile2 = new Tile(district4, district5, district6);
        board.addTile(tile1);
        board.addTile(tile2);

        // Add more tiles to create complex overlapping scenarios
        // Add more complex tile configurations to test scoring
        District district7 = new District(new Point3D(2, 0), DistrictColor.RED);
        District district8 = new District(new Point3D(1, -1), DistrictColor.BLUE);
        District district9 = new District(new Point3D(3, -1), DistrictColor.GREEN);
        District district10 = new District(new Point3D(2, 1), DistrictColor.BLUE);
        District district11 = new District(new Point3D(3, 0), DistrictColor.BLUE);
        District district12 = new District(new Point3D(4, 0), DistrictColor.BLUE);
        Tile tile3 = new Tile(district7, district8, district9);
        Tile tile4 = new Tile(district10, district11, district12);
        board.addTile(tile3);
        board.addTile(tile4);

        // Ensure that the score is calculated correctly considering multiple levels
        assertEquals(4, board.getScore(player));
    }

}
