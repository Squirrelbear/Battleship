import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Battleship
 * Author: Peter Mitchell (2021)
 *
 * Ship class:
 * Defines a simple ship object that can be drawn onto the screen. Stores
 * information about how many segments it includes, the direction of the
 * ship, how many of the segments have been destroyed, and properties to
 * set the colour.
 */
public class Ship {
    /**
     * Used for placement colour changing.
     * Valid: Indicates the ship could be placed at the current location shown as a Green ship.
     * Invalid: Indicates the ship can't be placed at the current location shown as a Red ship.
     * Placed: Used when the ship has been placed and will use default colour settings.
     */
    public enum ShipPlacementColour {Valid, Invalid, Placed}

    /**
     * The position in grid coordinates for where the ship is located.
     */
    private Position gridPosition;
    /**
     * The position in pixels for drawing the ship.
     */
    private Position drawPosition;
    /**
     * The number of segments in the ship to show how many cells it goes across.
     */
    private int segments;
    /**
     * True indicates the ship is horizontal, and false indicates the ship is vertical.
     */
    private boolean isSideways;
    /**
     * The number of destroyed sections to help determine if all of the ship has been destroyed when compared to segments.
     */
    private int destroyedSections;
    /**
     * Used to change the colour during manual placement by the player to show Green or Red to show valid and invalid placement.
     */
    private ShipPlacementColour shipPlacementColour;

    /**
     * Creates the ship with default properties ready for use. Assumes it has already been placed when created.
     *
     * @param gridPosition The position where the ship is located in terms of grid coordinates.
     * @param drawPosition Top left corner of the cell to start drawing the ship in represented in pixels.
     * @param segments The number of segments in the ship to show how many cells it goes across.
     * @param isSideways True indicates the ship is horizontal, and false indicates the ship is vertical.
     */
    public Ship(Position gridPosition, Position drawPosition, int segments, boolean isSideways) {
        this.gridPosition = gridPosition;
        this.drawPosition = drawPosition;
        this.segments = segments;
        this.isSideways = isSideways;
        destroyedSections = 0;
        shipPlacementColour = ShipPlacementColour.Placed;
    }

    /**
     * Draws the ship by first selecting the colour and then drawing the ship in the correct direction.
     * Colour is selected to be: Green if currently placing and it is valid, red if it is placing and invalid.
     * If it has already been placed it will show as red if destroyed, or dark gray in any other case.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    public void paint(Graphics g) {
        if(shipPlacementColour == ShipPlacementColour.Placed) {
            g.setColor(destroyedSections >= segments ? Color.RED : Color.DARK_GRAY);
        } else {
            g.setColor(shipPlacementColour == ShipPlacementColour.Valid ? Color.GREEN : Color.RED);
        }
        if(isSideways) paintHorizontal(g);
        else paintVertical(g);
    }

    /**
     * Sets the placement colour to indicate the state of the ship.
     *
     * @param shipPlacementColour Valid sets ship to show Green, Invalid sets ship to show Red, Placed sets to defaults.
     */
    public void setShipPlacementColour(ShipPlacementColour shipPlacementColour) {
        this.shipPlacementColour = shipPlacementColour;
    }

    /**
     * Toggles the current state between vertical and horizontal.
     */
    public void toggleSideways() {
        isSideways = !isSideways;
    }

    /**
     * Call when a section has been destroyed to let the ship keep track of how many sections have been destroyed.
     */
    public void destroySection() {
        destroyedSections++;
    }

    /**
     * Tests if the number of sections destroyed indicate all segments have been destroyed.
     *
     * @return True if all sections have been destroyed.
     */
    public boolean isDestroyed() { return destroyedSections >= segments; }

    /**
     * Updates the position to draw the ship at to the newPosition.
     *
     * @param gridPosition Position where the ship is now on the grid.
     * @param drawPosition Position to draw the Ship at in Pixels.
     */
    public void setDrawPosition(Position gridPosition, Position drawPosition) {
        this.drawPosition = drawPosition;
        this.gridPosition = gridPosition;
    }

    /**
     * Gets the current direction of the ship.
     *
     * @return True if the ship is currently horizontal, or false if vertical.
     */
    public boolean isSideways() {
        return isSideways;
    }

    /**
     * Gets the number of segments that make up the ship.
     *
     * @return The number of cells the ship occupies.
     */
    public int getSegments() {
        return segments;
    }

    /**
     * Gets a list of all cells that this ship occupies to be used for validation in AI checks.
     *
     * @return A list of all cells that this ship occupies.
     */
    public List<Position> getOccupiedCoordinates() {
        List<Position> result = new ArrayList<>();
        if(isSideways) { // handle the case when horizontal
            for(int x = 0; x < segments; x++) {
                result.add(new Position(gridPosition.x+x, gridPosition.y));
            }
        } else { // handle the case when vertical
            for(int y = 0; y < segments; y++) {
                result.add(new Position(gridPosition.x, gridPosition.y+y));
            }
        }
        return result;
    }

    /**
     * Draws the vertical ship by first drawing a triangle for the first cell, and then a
     * rectangle to cover the remaining cells based on the number of segments.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    public void paintVertical(Graphics g) {
        int boatWidth = (int)(SelectionGrid.CELL_SIZE * 0.8);
        int boatLeftX = drawPosition.x + SelectionGrid.CELL_SIZE / 2 - boatWidth / 2;
        g.fillPolygon(new int[]{drawPosition.x+SelectionGrid.CELL_SIZE/2,boatLeftX,boatLeftX+boatWidth},
                new int[]{drawPosition.y+SelectionGrid.CELL_SIZE/4,drawPosition.y+SelectionGrid.CELL_SIZE,drawPosition.y+SelectionGrid.CELL_SIZE},3);
        g.fillRect(boatLeftX,drawPosition.y+SelectionGrid.CELL_SIZE, boatWidth,
                (int)(SelectionGrid.CELL_SIZE * (segments-1.2)));
    }

    /**
     * Draws the horizontal ship by first drawing a triangle for the first cell, and then a
     * rectangle to cover the remaining cells based on the number of segments.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    public void paintHorizontal(Graphics g) {
        int boatWidth = (int)(SelectionGrid.CELL_SIZE * 0.8);
        int boatTopY = drawPosition.y + SelectionGrid.CELL_SIZE / 2 - boatWidth / 2;
        g.fillPolygon(new int[]{drawPosition.x+SelectionGrid.CELL_SIZE/4,drawPosition.x+SelectionGrid.CELL_SIZE,drawPosition.x+SelectionGrid.CELL_SIZE},
                      new int[]{drawPosition.y+SelectionGrid.CELL_SIZE/2,boatTopY,boatTopY+boatWidth},3);
        g.fillRect(drawPosition.x+SelectionGrid.CELL_SIZE,boatTopY,
                (int)(SelectionGrid.CELL_SIZE * (segments-1.2)), boatWidth);
    }
}
