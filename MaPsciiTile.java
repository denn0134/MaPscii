import java.awt.*;
import java.util.EnumSet;
import java.util.Set;

/***
 * Base class for representing an
 * object which can be used to store
 * and render a map using ASCII
 * graphic text.
 */
public abstract class MaPsciiTile
{
    /***
     * Enumeration of the directions
     * used for adding MaPscii entities.
     */
    public enum Direction{
        TOP, RIGHT, BOTTOM, LEFT
    }

    //glyph constants
    protected static final char GBLANK = ' ';
    protected static final char GPLUS = '+';
    protected static final char GDASH = '-';
    protected static final char GBAR = '|';
    protected static final char GANG_60 = '/';
    protected static final char GANG_120 = '\\';
    protected static final char GUNDSCR = '_';
    protected static final char GSTAR = '*';

    /***
     * Property: GUID
     * Unique identifier for the tile.
     */
    private String guid;
    public String getGuid(){
        return guid;
    }
    public void setGuid(String value){
        guid = value;
    }

    /***
     * Property: X
     * The horizontal component of the
     * coordinates for the tile.
     */
    private int x;
    public int getX(){
        return x;
    }
    public void setX(int value){
        x = value;
    }

    /***
     * Property: Y
     * The vertical component of the
     * coordinates for the tile.
     */
    private int y;
    public int getY(){
        return y;
    }
    public void setY(int value){
        y = value;
    }

    /***
     * Property: AsPoint
     * The coordinates of the tile as a
     * Point object.
     */
    public Point getAsPoint(){
        return new Point(x, y);
    }
    public void setAsPoint(Point value)
    {
        x = value.x;
        y = value.y;
    }

    /***
     * Property: IsNullTile
     * Determines if the tile is a null
     * tile representing a missing tile
     * in the map.
     */
    public abstract boolean getIsNullTile();

    /***
     * Constructor.
     */
    public MaPsciiTile(){}

    /***
     * Constructor.
     * @param x The x coordinate of the tile.
     * @param y The y coordinate of the tile.
     */
    public MaPsciiTile(int x, int y){
        this.x = x;
        this.y = y;
    }

    /***
     * Calculate the width of the tile
     * rendered at a specified scale.
     * @param scale The render scale to use.
     * @return Returns the width of the tile
     *         measured in characters when
     *         rendered at scale.
     */
    public abstract int calcWidth(int scale);

    /***
     * Calculate the height of the tile
     * rendered at a specified scale.
     * @param scale The render scale to use.
     * @return Returns the height of the tile
     *         measured in characters when
     *         rendered at scale.
     */
    public abstract int calcHeight(int scale);

    /***
     * Calculates the x and y distance
     * from the center of the map center
     * tile to the center of this tile.
     * @param scale The render scale to use.
     * @param mapVector The distance
     *                  (measured in tiles)
     *                  from the map center
     *                  tile to this tile.
     * @return Returns a Point object with
     *         the x and y distances.
     */
    public abstract Point calcVector(int scale,
                                     Point mapVector);

    /***
     * Calculates the distance from the
     * center of the tile to the outside
     * edge of the bounding box given a
     * specified direction.
     * @param scale Render scale to use.
     * @param dir Direction to use.
     * @return Returns the distance in
     *         characters from the center
     *         pixel to the outside edge
     *         of the bounding box.
     */
    protected abstract int calcDirectionBox(int scale, Direction dir);

    /***
     * Calculates the x and y distances
     * of the tile's bounding box in the
     * direction of the map center.
     * @param scale The render scale to use.
     * @param mapVector The distance
     *                  (measured in tiles)
     *                  from the map center
     *                  tile to this tile.
     * @return Returns a Point object with
     *         the x and y distances.
     */
    public Point calcBoxVector(int scale,
                               Point mapVector){
        Direction xDir = (mapVector.x > 0) ? Direction.LEFT : Direction.RIGHT;
        Direction yDir = (mapVector.y > 0) ? Direction.TOP : Direction.BOTTOM;

        return new Point(calcDirectionBox(scale, xDir),
                         calcDirectionBox(scale, yDir));
    }

    /***
     * Determines if a given line is at
     * the top or bottom of the tile at
     * the given scale.
     * @param scale The render scale to use.
     * @param line The line to check.
     * @return Returns True if the line is
     *         either the top line or the
     *         bottom line; returns False
     *         otherwise.
     */
    protected boolean isTopOrBottom(int scale, int line){
        boolean result = false;

        if(line == 0)
            result = true;
        else if(line == calcHeight(scale) - 1)
            result = true;

        return result;
    }

    /***
     * Renders a specified line of the tile
     * as a String object.
     * @param scale The render scale to use.
     * @param line The line number to be
     *             rendered, measured from
     *             the top and beginning
     *             at zero.
     * @return Returns the rendered line of
     *         ASCII graphic text.
     */
    protected abstract String renderLine(int scale,
                                      int line);

    /***
     * Renders the interior of the tile
     * at a given scale and line as a
     * String object.
     * @param scale The render scale to use.
     * @param line The line number to be
     *             rendered, measured from
     *             the top and beginning
     *             at zero.
     * @return Returns the rendered line of
     *         ASCII graphic text.
     */
    protected abstract String renderInterior(int scale, int line);

    /***
     * Calculates the number of null
     * characters in a line before the
     * actual tile begins.
     * @param scale The render scale to use.
     * @param line The line to check.
     * @return Return the number of
     *         characters to skip when
     *         rendering this line to
     *         a map.
     */
    protected abstract int calcRenderOffset(int scale, int line);
}
