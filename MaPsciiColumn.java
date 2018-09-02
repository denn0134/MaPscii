import java.util.ArrayList;

/***
 * Collection of tiles making up a
 * vertical column within a map.
 */
public class MaPsciiColumn
{
    /***
     * Property: ColumnId
     * Identifier for the column.
     * This ID is an integer and will
     * never change when new columns
     * are added, either to the top or
     * the bottom of the column.
     * This ID can be negative.  The
     * ColumnId matches the X value
     * in all tiles in the column.
     */
    private int columnId;
    public int getColumnId(){
        return columnId;
    }
    public void setColumnId(int value){
        columnId = value;
    }

    /***
     * Property: Tiles.
     * The list of tiles in the column.
     * The Y value of the tile denotes
     * the position in the column, lower
     * values being higher in the column.
     */
    private ArrayList<MaPsciiTile> tiles;
    public ArrayList<MaPsciiTile> getTiles(){
        return tiles;
    }

    /***
     * Constructor.
     * @param id The ID of the column.
     */
    public MaPsciiColumn(int id){
        columnId = id;
        tiles = new ArrayList<>();
    }

    /***
     * Retrieves the tile at the
     * specified index in the column.
     * Note that index is not the
     * same as the Y value.
     * @param index Index of the tile
     *              to retrieve.
     * @return Returns the tile at the
     *         specified index position.
     *         Returns null if the index
     *         is invalid.
     */
    public MaPsciiTile tile(int index){
        if(index < 0 || index >= tiles.size()){
            return null;
        }//end if
        else{
            return tiles.get(index);
        }//end else
    }

    /***
     * Adds a tile to either the top or
     * the bottom of the column.
     * @param tile The tile to add.
     * @param dir The Direction to add
     *            the tile (TOP or BOTTOM).
     * @throws MaPsciiContextException
     */
    public void add(MaPsciiTile tile,
                    MaPsciiTile.Direction dir)
            throws MaPsciiContextException{
        switch (dir){
            case TOP:
                tiles.add(0, tile);
                break;
            case BOTTOM:
                tiles.add(tile);
                break;
            default:
                throw new MaPsciiContextException("MaPsciiColumn.add requires a vertical Direction.");
        }//end switch
    }
}
