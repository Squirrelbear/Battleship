import java.awt.*;

/**
 * Battleship
 * Author: Peter Mitchell (2021)
 *
 * Marker class:
 * Represents a simple coloured rectangle that can be either
 * shown or hidden, and will change colour based on whether
 * it is representing the location where a ship is.
 */
public class Marker extends Rectangle {
    /**
     * The colour to show when a ship is located at this marker.
     */
    private final Color HIT_COLOUR = new Color(219, 23, 23, 180);
    /**
     * The colour to show when there is no ship at this marker.
     */
    private final Color MISS_COLOUR = new Color(26, 26, 97, 180);
    /**
     * Padding around the edges of the filled rectangle to make it a little smaller.
     */
    private final int PADDING = 3;
    /**
     * When true the marker will be painted.
     */
    private boolean showMarker;
    /**
     * Changes the colour used for drawing. When a ship is assigned will use HIT_COLOUR,
     * when null will use MISS_COLOUR.
     */
    private Ship shipAtMarker;

    /**
     * Prepares the marker with a default state where it is ready to draw
     * at a specified position with no associated ship by default.
     *
     * @param x X coordinate to draw this marker at.
     * @param y Y coordinate to draw this marker at.
     * @param width Width of the marker's cell.
     * @param height Height of the marker's cell.
     */
    public Marker(int x, int y, int width, int height) {
        super(x, y, width, height);
        reset();
    }

    /**
     * Resets to no referenced ship, and with the marker not visible.
     */
    public void reset() {
        shipAtMarker = null;
        showMarker = false;
    }

    /**
     * If not previously marked it will tell the associated ship that
     * another section has been destroyed. Then mark the marker to make
     * it visible for drawing.
     */
    public void mark() {
        if(!showMarker && isShip()) {
            shipAtMarker.destroySection();
        }
        showMarker = true;
    }

    /**
     * Gets if the marker has already been interacted with.
     *
     * @return True if the marker is visible.
     */
    public boolean isMarked() {
        return showMarker;
    }

    /**
     * Sets the ship to the specified reference. Changes the colour used
     * if this marker is revealed, and allows notification on the ship if
     * interaction to mark this marker does happen.
     *
     * @param ship Reference to the ship at this location.
     */
    public void setAsShip(Ship ship) {
        this.shipAtMarker = ship;
    }

    /**
     * Gets if this marker has an associated Ship.
     *
     * @return True if a ship has been set.
     */
    public boolean isShip() {
        return shipAtMarker != null;
    }

    /**
     * Gets the associated ship if there is one, otherwise it will be null.
     *
     * @return Reference to the associated ship for this Marker.
     */
    public Ship getAssociatedShip() {
        return shipAtMarker;
    }

    /**
     * Does nothing if not marked.
     * Draws a rectangle to match the correct padded size of the marker.
     * Uses the colour based on whether this object is over a ship.
     *
     * @param g Reference to the Graphics object for drawing.
     */
    public void paint(Graphics g) {
        if(!showMarker) return;

        g.setColor(isShip() ? HIT_COLOUR : MISS_COLOUR);
        g.fillRect(position.x+PADDING+1, position.y+PADDING+1, width-PADDING*2, height-PADDING*2);
    }
}
