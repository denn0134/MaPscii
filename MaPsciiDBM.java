import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

/***
 * Data module class for storage of
 * MaPscii map objects.
 */
public class MaPsciiDBM
{
    //database constants
    private static final String TN_MAP = "MAP";
    private static final String TN_MAP_TILES = "MAP_TILES";
    private static final String FN_MAP_GUID = "GUID";
    private static final String FN_MAP_NAME = "NAME";
    private static final String FN_MAP_DESC = "DESCRIPTION";
    private static final String FN_MT_GUID = "GUID";
    private static final String FN_MT_CLASS = "CLASS_NAME";
    private static final String FN_MT_MAP = "MAP_ID";
    private static final String FN_MT_TILE_X = "TILE_X";
    private static final String FN_MT_TILE_Y = "TILE_Y";
    private static final String FN_MT_MAP_X = "MAP_X";
    private static final String FN_MT_MAP_Y = "MAP_Y";
    private static final String FN_MT_RENDER = "RENDER_PASS";

    private enum TILE_CLASSES{
        MaPsciiSquare
    }

    //SQL for loading the map tiles
    //for a given map ID
    private static final String
            SQL_LOAD_TILES_FOR_MAP = "SELECT * FROM MAP_TILES WHERE MAP_ID = '%0$S' ORDER BY MAP_X, MAP_Y";

    /***
     * Property: ConnStr
     * The connection string for the database.
     */
    private String connstr;
    public String getConnstr() {
        return connstr;
    }
    public void setConnstr(String connstr) {
        this.connstr = connstr;
    }

    private IMaPsciiExceptionHandler handler;

    private Connection conn;

    /***
     * Constructor.
     * @param connectionString The connection
     *                         string for the
     *                         databbase.
     */
    public MaPsciiDBM(String connectionString, IMaPsciiExceptionHandler excHandler)
    {
        connstr = connectionString;
        handler = excHandler;
    }

    /***
     * Handles exceptions which occur.
     * @param exc Te exception.
     */
    private void exception(Exception exc){
        if(handler != null)
            handler.handleException(exc);
        else
            System.out.println(exc.getMessage());
    }

    /***
     * Determines if there is an
     * active connection to the database.
     * @return Returns True if the database
     *         is currently connected; False
     *         otherwise.
     */
    public boolean isConnected(){
        return (conn != null);
    }

    /***
     * Connects to the database using
     * the connection string.
     * @return Returns True if the connection
     *         is made; False if not.
     */
    public boolean connect(){
        try{
            conn = DriverManager.getConnection(connstr);
        }//end try
        catch(SQLException sqle){
            conn = null;

            exception(sqle);

            return false;
        }//end catch sqle

        return true;
    }

    /***
     * Disconnects the current database
     * connection.
     */
    public void disconnect(){
        if(conn != null){
            try{
                conn.close();
            }//end try
            catch(SQLException sqle){
                exception(sqle);
            }//end catch sqle
        }//end if
    }

    /***
     * Loads a MaPsciiGrid object from
     * the database.
     * @param mapGrid The map grid object
     *                to load data into.
     */
    public void loadMap(MaPsciiGrid mapGrid){
        //clear the map before loading it
        ArrayList<MaPsciiColumn> cols = mapGrid.getColumns();
        cols.clear();

        //get the data set to load from
        String mapGuid = mapGrid.getGuid();
        String loadSQL = String.format(SQL_LOAD_TILES_FOR_MAP, mapGuid);

        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(loadSQL);

            //load the tiles column by column
            int colIdx = -1;
            int newIdx = -1;
            MaPsciiColumn curCol = null;
            while(rs.next()){
                //if the column ID has
                //changed, then start a
                //new column
                newIdx = rs.getInt(FN_MT_MAP_X);
                if(newIdx != colIdx){
                    colIdx = newIdx;
                    curCol = new MaPsciiColumn(colIdx);
                    cols.add(curCol);
                }//end if

                //create the tile and add
                //it to the current column
                if(curCol != null) {
                    int rowIdx = rs.getInt(FN_MT_MAP_Y);
                    Point p = new Point(colIdx, rowIdx);
                    MaPsciiTile tile = loadTile(p, rs);

                    if(tile != null){
                        curCol.getTiles().add(tile);
                    }//end if
                    else{
                        //column load failed
                    }//end else
                }//end if
            }//end while
        }//end try
        catch(SQLException sqle){
            handler.handleException(sqle);
        }//end sqle
    }

    /***
     * Creates and loads a MaPsciiTile
     * object from the database.
     * @param tile The map coordinates
     *             of the tile to load.
     * @param rs The ResultSet to load from.
     * @return Returns the requested
     *         tile object.
     */
    private MaPsciiTile loadTile(Point tile, ResultSet rs)
            throws SQLException{
        MaPsciiTile retTile = null;

        //create the tile
        String className = rs.getString(FN_MT_CLASS);
        TILE_CLASSES tileClass = TILE_CLASSES.valueOf(className);
        retTile = createTile(tileClass);

        //load the tile from the database
        if(retTile != null){
            retTile.setGuid(rs.getString(FN_MT_GUID));
            retTile.setX(rs.getInt(FN_MT_TILE_X));
            retTile.setY(rs.getInt(FN_MT_TILE_Y));

            String pass = rs.getString(FN_MT_RENDER);
            retTile.setRenderPass(MaPsciiTile.RenderPass.valueOf(pass));
        }//end if

        return retTile;
    }

    /***
     * Creates a tile of the specified
     * sub-class of MaPsciiTile.
     * @param classType Class type enumeration
     *                  to create.
     * @return Returns the tile.
     */
    private MaPsciiTile createTile(TILE_CLASSES classType){
        switch (classType){
            case MaPsciiSquare:
                return new MaPsciiSquare();
            default:
                return null;
        }//end switch
    }
}
