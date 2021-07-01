/**
 * Battleship
 * Author: Peter Mitchell (2021)
 *
 * Rectangle class:
 * Defines a simple Rectangle with a position for the top left corner,
 * and a width/height to represent the size of the Rectangle.
 */
public class Rectangle {
    /**
     * The top left corner of the Rectangle.
     */
    protected Position position;
    /**
     * Width of the Rectangle.
     */
    protected int width;
    /**
     * Height of the Rectangle.
     */
    protected int height;

    /**
     * Creates the new Rectangle with provided properties.
     *
     * @param position The top left corner of the Rectangle.
     * @param width Width of the Rectangle.
     * @param height Height of the Rectangle.
     */
    public Rectangle(Position position, int width, int height) {
        this.position = position;
        this.width = width;
        this.height = height;
    }

    /**
     * @param x X coordinate of the top left corner.
     * @param y Y coordinate of the top left corner.
     * @param width Width of the rectangle.
     * @param height Height of the rectangle.
     */
    public Rectangle(int x, int y, int width, int height) {
        this(new Position(x,y),width,height);
    }

    /**
     * Gets the height of the Rectangle.
     *
     * @return Height of the Rectangle.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets the width of the Rectangle.
     *
     * @return Width of the Rectangle.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the top left corner of the Rectangle.
     *
     * @return Top left corner of the Rectangle.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Tests if the targetPosition is inside the Rectangle.
     *
     * @param targetPosition Position to test if it is inside the Rectangle.
     * @return True if the targetPosition is inside this Rectangle.
     */
    public boolean isPositionInside(Position targetPosition) {
        return targetPosition.x >= position.x && targetPosition.y >= position.y
                && targetPosition.x < position.x + width && targetPosition.y < position.y + height;
    }
}
