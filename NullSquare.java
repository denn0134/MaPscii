/***
 * Class for representing empty tiles
 * on a map using square tiles.
 */
public class NullSquare extends MaPsciiSquare
{
    /***
     * Constructor.
     */
    public NullSquare(){
        super();
    }

    /***
     * Constructor.
     * @param x The x coordinate of the tile.
     * @param y The y coordinate of the tile.
     */
    public NullSquare(int x, int y){
        super(x, y);
    }

    @Override
    public boolean getIsNullTile() {
        return true;
    }

    @Override
    protected String renderLine(int scale, int line) {
        int w = calcWidth(scale);
        StringBuilder sb = new StringBuilder(w);

        for(int i = 0; i < w; i++){
            sb.append(GBLANK);
        }//end for i

        return sb.toString();
    }
}
