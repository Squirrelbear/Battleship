import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Battleship
 * Author: Peter Mitchell (2021)
 *
 * SelectionGrid class:
 * Defines the grid for storing Ships with a grid of markers to
 * indicate hit/miss detection.
 */
public class SelectionGrid extends Rectangle {
    /**
     * Size of each grid cell in pixels.
     */
    public static final int CELL_SIZE = 30;
    /**
     * Number of grid cells on the Horizontal axis.
     */
    public static final int GRID_WIDTH = 10;
    /**
     * Number of grid cells on the Vertical axis.
     */
    public static final int GRID_HEIGHT = 10;
    /**
     * Definitions of the number of Ships, and the number of segments that make up each of those ships.
     */
    public static final int[] BOAT_SIZES = {5,4,3,3,2};

    /**
     * A grid of markers to indicate visually the hit/miss on attacks.
     */
    private Marker[][] markers = new Marker[GRID_WIDTH][GRID_HEIGHT];
    /**
     * A list of all the ships on this grid.
     */
    private List<Ship> ships;
    /**
     * Shared random reference to use for randomisation of the ship placement.
     */
    private Random rand;
    /**
     * Ships are drawn when true. This is mostly used to make the player's ships always show.
     */
    private boolean showShips;
    /**
     * True once all the elements in ships have been destroyed.
     */
    private boolean allShipsDestroyed;

    /**
     * Configures the grid to create the default configuration of markers.
     *
     * @param x X coordinate to offset the grid by in pixels.
     * @param y Y coordinate to offset the grid by in pixels.
     */
    public SelectionGrid(int x, int y) {
        super(x, y, CELL_SIZE*GRID_WIDTH, CELL_SIZE*GRID_HEIGHT);
        createMarkerGrid();
        ships = new ArrayList<>();
        rand = new Random();
        showShips = false;
    }

    /**
     * Draws the ships if all ships on this grid are to be shown, or if debug mode is active,
     * or if each individual ship is flagged as having been destroyed. Then draws all markers
     * that should be shown for attacks made so far, and a grid of lines to show where the grid
     * is overlaid.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    public void paint(Graphics g) {
        for(Ship ship : ships) {
            if(showShips || GamePanel.debugModeActive || ship.isDestroyed()) {
                ship.paint(g);
            }
        }
        drawMarkers(g);
        drawGrid(g);
    }

    /**
     * Modifies the state of the grid to show all the ships if set to true.
     *
     * @param showShips True will make all the ships on this grid be visible.
     */
    public void setShowShips(boolean showShips) {
        this.showShips = showShips;
    }

    /**
     * Resets the SelectionGrid by telling all the markers to reset,
     * removing all ships from the grid, and defaulting back to not
     * showing any ships, and a state where no ships have been destroyed.
     */
    public void reset() {
        for(int x = 0; x < GRID_WIDTH; x++) {
            for(int y = 0; y < GRID_HEIGHT; y++) {
                markers[x][y].reset();
            }
        }
        ships.clear();
        showShips = false;
        allShipsDestroyed = false;
    }

    /**
     * Marks the specified position and then checks all ships to determine if they have
     * all been destroyed.
     *
     * @param posToMark Position to mark.
     * @return True if the marked position was a ship.
     */
    public boolean markPosition(Position posToMark) {
        markers[posToMark.x][posToMark.y].mark();

        allShipsDestroyed = true;
        for(Ship ship : ships) {
            if(!ship.isDestroyed()) {
                allShipsDestroyed = false;
                break;
            }
        }
        return markers[posToMark.x][posToMark.y].isShip();
    }

    /**
     * Checks if all ships have been destroyed.
     *
     * @return True if all the ships have been destroyed.
     */
    public boolean areAllShipsDestroyed() {
        return allShipsDestroyed;
    }

    /**
     * Checks if the specified position is already marked.
     *
     * @param posToTest Position to test if it is marked.
     * @return True if the marker at the specified position is marked.
     */
    public boolean isPositionMarked(Position posToTest) {
        return markers[posToTest.x][posToTest.y].isMarked();
    }

    /**
     * Gets the marker at the specified position. Useful for allowing the AI more access to the data on the grid.
     *
     * @param posToSelect Position on the grid to select the marker at.
     * @return Returns a reference to the marker at the specified position.
     */
    public Marker getMarkerAtPosition(Position posToSelect) {
        return markers[posToSelect.x][posToSelect.y];
    }

    /**
     * Translates the mouse position to a grid coordinate if possible.
     *
     * @param mouseX Mouse X coordinate.
     * @param mouseY Mouse Y coordinate.
     * @return Returns either (-1,-1) for an invalid position, or the corresponding grid position related to the coordinates.
     */
    public Position getPositionInGrid(int mouseX, int mouseY) {
        if(!isPositionInside(new Position(mouseX,mouseY))) return new Position(-1,-1);

        return new Position((mouseX - position.x)/CELL_SIZE, (mouseY - position.y)/CELL_SIZE);
    }

    /**
     * Tests if a ship given the specified properties would be valid for placement.
     * Tests this by checking if the ship fits within the bounds of the grid, and then
     * checks if all the segments would fall on places where a ship does not already sit.
     * This is handled separately depending on whether it is a horizontal (sideways) or
     * vertical ship.
     *
     * @param gridX Grid X coordinate.
     * @param gridY Grid Y coordinate.
     * @param segments The number of cells that tail the coordinate.
     * @param sideways True indicates it is horizontal, false insides it is vertical.
     * @return True if the ship can be placed with the specified properties.
     */
    public boolean canPlaceShipAt(int gridX, int gridY, int segments, boolean sideways) {
        if(gridX < 0 || gridY < 0) return false;

        if(sideways) { // handle the case when horizontal
            if(gridY > GRID_HEIGHT || gridX + segments > GRID_WIDTH) return false;
            for(int x = 0; x < segments; x++) {
                if(markers[gridX+x][gridY].isShip()) return false;
            }
        } else { // handle the case when vertical
            if(gridY + segments > GRID_HEIGHT || gridX > GRID_WIDTH) return false;
            for(int y = 0; y < segments; y++) {
                if(markers[gridX][gridY+y].isShip()) return false;
            }
        }
        return true;
    }

    /**
     * Draws a grid made up of single pixel black lines.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    private void drawGrid(Graphics g) {
        g.setColor(Color.BLACK);
        // Draw vertical lines
        int y2 = position.y;
        int y1 = position.y+height;
        for(int x = 0; x <= GRID_WIDTH; x++)
            g.drawLine(position.x+x * CELL_SIZE, y1, position.x+x * CELL_SIZE, y2);

        // Draw horizontal lines
        int x2 = position.x;
        int x1 = position.x+width;
        for(int y = 0; y <= GRID_HEIGHT; y++)
            g.drawLine(x1, position.y+y * CELL_SIZE, x2, position.y+y * CELL_SIZE);
    }

    /**
     * Draws all the markers. The markers will determine individually if it is necessary to draw.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    private void drawMarkers(Graphics g) {
        for(int x = 0; x < GRID_WIDTH; x++) {
            for(int y = 0; y < GRID_HEIGHT; y++) {
                markers[x][y].paint(g);
            }
        }
    }

    /**
     * Creates all the marker objects setting their draw positions on the grid to initialise them.
     */
    private void createMarkerGrid() {
        for(int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                markers[x][y] = new Marker(position.x+x*CELL_SIZE, position.y + y*CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    /**
     * Clears all current ships, and then randomly places all the ships. The ships
     * will not be placed over the top of other ships. This method assumes there is
     * plenty of space to place all the ships regardless of configuration.
     */
    public void populateShips() {
        ships.clear();
        for(int i = 0; i < BOAT_SIZES.length; i++) {
            boolean sideways = rand.nextBoolean();
            int gridX,gridY;
            do {
                gridX = rand.nextInt(sideways?GRID_WIDTH-BOAT_SIZES[i]:GRID_WIDTH);
                gridY = rand.nextInt(sideways?GRID_HEIGHT:GRID_HEIGHT-BOAT_SIZES[i]);
            } while(!canPlaceShipAt(gridX,gridY,BOAT_SIZES[i],sideways));
            placeShip(gridX, gridY, BOAT_SIZES[i], sideways);
        }
    }

    /**
     * Places a ship on the grid with the specified properties. Assumes checks have already been
     * made to verify the ship can be placed there. Indicates to the marker cells that a ship is
     * on top of them to use for placement of other ships, and hit detection.
     *
     * @param gridX X coordinate on the grid.
     * @param gridY Y coordinate on the grid.
     * @param segments Number of cells the ship occupies.
     * @param sideways True indicates horizontal, or false indicates vertical.
     */
    public void placeShip(int gridX, int gridY, int segments, boolean sideways) {
        placeShip(new Ship(new Position(gridX, gridY),
                           new Position(position.x+gridX*CELL_SIZE, position.y+gridY*CELL_SIZE),
                            segments, sideways), gridX, gridY);
    }

    /**
     * Places a ship on the grid with the specified properties. Assumes checks have already been
     * made to verify the ship can be placed there. Indicates to the marker cells that a ship is
     * on top of them to use for placement of other ships, and hit detection.
     *
     * @param ship The ship to place on the grid with already configured properties.
     * @param gridX X coordinate on the grid.
     * @param gridY Y coordinate on the grid.
     */
    public void placeShip(Ship ship, int gridX, int gridY) {
        ships.add(ship);
        if(ship.isSideways()) { // If the ship is horizontal
            for(int x = 0; x < ship.getSegments(); x++) {
                markers[gridX+x][gridY].setAsShip(ships.get(ships.size()-1));
            }
        } else { // If the ship is vertical
            for(int y = 0; y < ship.getSegments(); y++) {
                markers[gridX][gridY+y].setAsShip(ships.get(ships.size()-1));
            }
        }
    }
}
