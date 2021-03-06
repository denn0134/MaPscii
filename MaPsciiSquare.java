import java.awt.*;

/***
 * Base class for representing a
 * map using square tiles.
 */
public class MaPsciiSquare extends MaPsciiTile
{
    /***
     * Constructor.
     */
    public MaPsciiSquare(){
        super();
    }

    /***
     * Constructor.
     * @param x The x coordinate of the tile.
     * @param y The y coordinate of the tile.
     */
    public MaPsciiSquare(int x, int y){
        super(x, y);
    }

    /***
     * Get the character to use for
     * the horizontal border of the tile.
     * @param pass The render pass.
     * @return Returns the horizontal
     *         border character.
     */
    private char hBorder(RenderPass pass){
        return (pass == RenderPass.RP_STANDARD) ? GDASH : GBLANK;
    }

    /***
     * Get the character to use for
     * the vertical border of the tile.
     * @param pass The render pass.
     * @return Returns the vertical
     *         border character.
     */
    private char vBorder(RenderPass pass){
        return (pass == RenderPass.RP_STANDARD) ? GBAR : GBLANK;
    }

    /***
     * Get the character to use for
     * the corner border of the tile.
     * @param pass The render pass.
     * @return Returns the corner character.
     */
    private char cBorder(RenderPass pass){
        return (pass == RenderPass.RP_STANDARD) ? GPLUS : GBLANK;
    }

    @Override
    public int calcWidth(int scale) {
        if(scale == 1){
            return 5;
        }//end if
        else {
            float w = scale * 2.38f;
            return Math.round(w) + 2;
        }//end else
    }

    @Override
    public int calcHeight(int scale) {
        return scale + 2;
    }

    @Override
    public boolean getIsNullTile() {
        return false;
    }

    @Override
    protected int calcDirectionBox(int scale, Direction dir) {
        int w = calcWidth(scale);
        int h = calcHeight(scale);
        int centerX = Math.round(w / 2.0f);
        int centerY = Math.round(h / 2.0f);

        int r = w - centerX;
        int b = h - centerY;

        switch (dir){
            case LEFT:
                return (w - r - 1);
            case TOP:
                return (h - b - 1);
            case RIGHT:
                return r;
            case BOTTOM:
                return b;
            default:
                return 0;
        }//end switch
    }

    @Override
    public Point calcVector(int scale,
                            Point mapVector) {
        int x = Math.abs(mapVector.x) * (calcWidth(scale) - 1);
        int y = Math.abs(mapVector.y) * (calcHeight(scale) - 1);
        return new Point(x, y);
    }

    @Override
    protected String renderLine(int scale, int line, RenderPass pass) {
        int w = calcWidth(scale);
        boolean tb = isTopOrBottom(scale, line);
        StringBuilder sb = new StringBuilder(w);

        //render the line
        char pix, hpix;
        pix = (tb) ? cBorder(pass) : vBorder(pass);
        hpix = hBorder(pass);

        sb.append(pix);

        if(tb){
            for(int i = 1; i < w - 1; i++){
                sb.append(hpix);
            }//end for i
        }//end if
        else{
            //render the interior
            sb.append(renderInterior(scale, line, pass));
        }//end else

        sb.append(pix);

        return sb.toString();
    }

    @Override
    protected String renderInterior(int scale, int line, RenderPass pass) {
        int w = calcWidth(scale);
        StringBuilder sb = new StringBuilder(w);

        for(int i = 1; i < w - 1; i++){
            sb.append(GBLANK);
        }//end for i

        return sb.toString();
    }

    @Override
    protected int calcRenderOffset(int scale, int line) {
        return 0;
    }
}
