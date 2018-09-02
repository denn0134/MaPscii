import java.awt.*;
import java.util.ArrayList;

/***
 * Class representing the stored data
 * defining a map.  The data is stored
 * as tiles, these tiles may be square
 * or hexagonal, but all tiles must be
 * of the same shape.  This grid will
 * always have a horizontal and vertical
 * component such that the grid forms
 * a rectangle in shape.
 */
public class MaPsciiGrid
{
    /***
     * Property: GUID
     * Unique identifier for the map
     * data currently loaded.
     */
    private String guid;
    public String getGuid() {
        return guid;
    }
    public void setGuid(String guid) {
        this.guid = guid;
    }

    /***
     * The name of the map data.
     */
    private String name;
    public String getName(){
        return name;
    }
    public void setName(String value){
        name = value;
    }

    /***
     * File name to use when saving the map.
     */
    private String fileName;
    public String getFileName(){
        return fileName;
    }
    public void setFileName(String value){
        fileName = value;
    }

    /***
     * Collection of column objects making
     * up the map.
     */
    private ArrayList<MaPsciiColumn> columns = new ArrayList<>();
    public ArrayList<MaPsciiColumn> getColumns(){
        return columns;
    }

    /***
     * Calculates the width of the grid.
     * @return Returns the width of the grid.
     */
    public int width(){
        return columns.size();
    }
    /***
     * Calculates the height of the grid.
     * @return Returns the height of the grid/
     */
    public int height(){
        if(columns.size() == 0)
            return 0;
        else
            return column(0).getTiles().size();
    }

    /***
     * Retrieve the column at the
     * specified index.  Note that
     * the index is not the same
     * as the ColumnId of the column.
     * @param index Index of the column
     *              to retrieve.
     * @return Returns the column at the
     *         specified index position.
     *         Returns null if the index
     *         is invalid.
     */
    public MaPsciiColumn column(int index){
        if(index < 0 || index >= columns.size()){
            return null;
        }//end if
        else{
            return columns.get(index);
        }//end else
    }

    /***
     * Retrieves the tile at the col and
     * row index specified.
     * @param col Column index of the tile.
     * @param row Row index of the tile.
     * @return Returns the tile at the
     *         specified index position.
     *         Returns null if any index
     *         is invalid.
     */
    public MaPsciiTile tile(int col, int row){
        boolean colValid = (col >= 0 && col < columns.size() );
        boolean rowValid = (row >= 0 && row < height());
        if(colValid && rowValid){
            return column(col).tile(row);
        }//end if
        else{
            return null;
        }//end else
    }

    /***
     * Retrieves the tile at the index
     * position indicated by a Point object.
     * @param p Point object specifying
     *          the col and row index
     *          positions.
     * @return Returns the tile at the
     *         specified index position.
     *         Returns null if any index
     *         is invalid.
     */
    public MaPsciiTile tile(Point p){
        return tile(p.x, p.y);
    }

    /***
     * Adds a column to either the left
     * or the right edge of the grid.
     * @param col The column to add.
     * @param dir The Direction to
     *            add the column to
     *            (LEFT or RIGHT).
     * @throws MaPsciiContextException
     */
    public void add(MaPsciiColumn col,
                    MaPsciiTile.Direction dir)
            throws MaPsciiContextException{
        switch(dir){
            case LEFT:
                columns.add(0, col);
                break;
            case RIGHT:
                columns.add(col);
                break;
            default:
                throw new MaPsciiContextException("MaPsciiGrid.add requires a horizontal Direction.");
        }//end switch
    }
}
