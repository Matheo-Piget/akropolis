package model;

/* This class is used to represent the different types of tiles that can be used in the game which are:
- Quarries, which allow you to earn stones useful for buying tiles.
- Places, which can contain from 1 to 3 stars, and which increase the value of the districts.
- The districts, of different colors and which, if they are well placed, earn points. */

public enum TileType {
    Quarries, Places, Districts
}
