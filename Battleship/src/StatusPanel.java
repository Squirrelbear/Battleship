import java.awt.*;

/**
 * Battleship
 * Author: Peter Mitchell (2021)
 *
 * StatusPanel class:
 * Defines a simple text panel to show a top and bottom line of text.
 * Some of these are already defined in the class, and it provides
 * additional methods to set the messages to custom values.
 */
public class StatusPanel extends Rectangle{
    /**
     * The font to use for drawing both of the messages.
     */
    private final Font font = new Font("Arial", Font.BOLD, 20);
    /**
     * Message to show on the top line during ship placement.
     */
    private final String placingShipLine1 = "Place your Ships below!";
    /**
     * Message to show on the bottom line during ship placement.
     */
    private final String placingShipLine2 = "Z to rotate.";
    /**
     * Message to show on the top line when the game is lost.
     */
    private final String gameOverLossLine = "Game Over! You Lost :(";
    /**
     * Message to show on the top line when the game is won.
     */
    private final String gameOverWinLine = "You won! Well done!";
    /**
     * Message to show on the bottom line when the game is won or lost.
     */
    private final String gameOverBottomLine = "Press R to restart.";

    /**
     * The current message to display on the top line.
     */
    private String topLine;
    /**
     * The current message to display on the bottom line.
     */
    private String bottomLine;

    /**
     * Configures the status panel to be ready for drawing a background,
     * and initial default text.
     *
     * @param position Top left corner of the panel.
     * @param width Width of the area to draw within.
     * @param height Height of the area to draw within.
     */
    public StatusPanel(Position position, int width, int height) {
        super(position, width, height);
        reset();
    }

    /**
     * Resets the message back to the default for ship placement.
     */
    public void reset() {
        topLine = placingShipLine1;
        bottomLine = placingShipLine2;
    }

    /**
     * Sets the message to display based on whether the player has won or lost.
     *
     * @param playerWon True if the player has won, or false if the player lost.
     */
    public void showGameOver(boolean playerWon) {
        topLine = (playerWon) ? gameOverWinLine : gameOverLossLine;
        bottomLine = gameOverBottomLine;
    }

    /**
     * Sets the message to display on the top line of output to any specified String.
     *
     * @param message Message to display on the top line.
     */
    public void setTopLine(String message) {
        topLine = message;
    }

    /**
     * Sets the message to display on the bottom line of output to any specified String.
     *
     * @param message Message to display on the bottom line.
     */
    public void setBottomLine(String message) {
        bottomLine = message;
    }

    /**
     * Draws a light gray background with black text centred over two lines using
     * the top line and bottom line messages.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    public void paint(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(position.x, position.y, width, height);
        g.setColor(Color.BLACK);
        g.setFont(font);
        int strWidth = g.getFontMetrics().stringWidth(topLine);
        g.drawString(topLine, position.x+width/2-strWidth/2, position.y+20);
        strWidth = g.getFontMetrics().stringWidth(bottomLine);
        g.drawString(bottomLine, position.x+width/2-strWidth/2, position.y+40);
    }
}
