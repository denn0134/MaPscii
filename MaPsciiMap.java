import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/***
 * Class for rendering an ascii graphic
 * text map composed of tiles.  The map
 * renders tiles based on a center point
 * and a scale combined with a screen
 * size, rendering only those parts of
 * the map which are visible within the
 * screen's viewport.
 */
public class MaPsciiMap
{
    /***
     * Property: Grid.
     * The grid holding the map data.
     */
    private MaPsciiGrid grid;
    public MaPsciiGrid getGrid(){
        return grid;
    }
    public void setGrid(MaPsciiGrid value){
        grid = value;
    }

    /***
     * Property: Scale
     * The scale at which to render the map.
     */
    private int scale;
    public int getScale(){
        return scale;
    }
    public void setScale(int value){
        scale = value;
    }

    /***
     * Property: MapCenter
     * The current center tile of the
     * map (in map coordinates).
     */
    private Point mapCenter;
    public Point getMapCenter(){
        return mapCenter;
    }
    public void setMapCenter(Point value){
        mapCenter = value;
    }

    private RenderContext rcon = new RenderContext(0,0,0,0);

    /***
     * Constructor.
     */
    public MaPsciiMap(){}

    /***
     * Creates a MaPsciiTile based
     * on a tile class.
     * @param tileClass Class of tile
     *                  to instantiate.
     * @param x X value for the tile.
     * @param y Y value for the tile.
     * @return Returns a new MaPsciiTile
     *         object.
     */
    private MaPsciiTile createTile(Class<? extends MaPsciiTile> tileClass,
                                   int x, int y){
        MaPsciiTile tile = null;

        try{
            tile = tileClass.newInstance();
            tile.setX(x);
            tile.setY(y);
        }//end try
        catch(IllegalAccessException | InstantiationException ie){
            System.out.println(ie.getMessage());
            System.out.println(ie.toString());
            System.out.println(ie.getCause().toString());
        }//end catch ie

        return tile;
    }

    /***
     * Saves the map data to a json file.
     */
    public void saveMap(){
        if(grid != null) {
            File file;
            try {
                String fn = grid.getFileName();
                if(fn == null){
                    fn = "C:\\Debug\\mapGrid.json";
                }//end if

                file = new File(fn);
                ObjectMapper om = new ObjectMapper();
                om.enableDefaultTyping();

                om.writeValue(file, grid);
            }//end try
            catch (IOException ioe) {
            }//end catch ioe
        }//end if
    }

    /***
     * Creates and loads a map grid from
     * a json file.
     * @param file The file to load.
     */
    public void loadMap(File file){

    }

    /***
     * Calculate the map origin.  The
     * map origin is the map coordinates
     * of the tile with tile coordinates
     * of (0,0).
     * @return Returns the map origin as
     *         a Point object.  If there
     *         are no tiles in the map
     *         data then the origin is
     *         defined as (0,0).
     */
    public Point calcOrigin(){
        MaPsciiTile tileOrigin = grid.tile(0, 0);
        if(tileOrigin == null) {
            return new Point(0, 0);
        }//end if
        else{
            int x = tileOrigin.getX() * -1;
            int y = tileOrigin.getY() * -1;
            return new Point(x, y);
        }//end else

    }

    /***
     * Adds a row or column of tiles to
     * the map data.  All tiles added
     * will be of a specified class.
     * Only one row or column can be added
     * per call.
     * @param dir The direction to add
     *            the tiles.  TOP or
     *            BOTTOM adds a row,
     *            LEFT or RIGHT adds
     *            a column.
     * @param tileClass The class to use
     *                  when creating the
     *                  tiles to be added.
     * @throws MaPsciiContextException
     */
    public void addTiles(MaPsciiTile.Direction dir,
                         Class<? extends MaPsciiTile> tileClass)
            throws MaPsciiContextException{
        MaPsciiColumn col;
        int id, size;

        switch(dir){
            case TOP:
            case BOTTOM:
                //if there are currently
                //no columns then add one,
                //there can be no rows
                //without columns
                if(grid.getColumns().size() == 0){
                    col = new MaPsciiColumn(0);
                    grid.add(col, MaPsciiTile.Direction.RIGHT);
                }//end if
                else{
                    //add a tile to each column
                    int y;
                    int yOffset = calcOrigin().y;
                    if(dir == MaPsciiTile.Direction.TOP)
                        y = 0 - yOffset - 1;
                    else
                        y = grid.height() - yOffset;

                    for(int i = 0; i < grid.getColumns().size(); i++){
                        col = grid.column(i);
                        MaPsciiTile t = createTile(tileClass,col.getColumnId(), y);
                        col.add(t, dir);
                    }//end for i
                }//end else

                break;
            case LEFT:
            case RIGHT:
                //add the new column
                size = grid.height();
                if(size == 0)
                    id = 0;
                else if(dir == MaPsciiTile.Direction.LEFT)
                    id = grid.column(0).getColumnId() - 1;
                else
                    id = grid.column(grid.width() - 1).getColumnId() + 1;

                col = new MaPsciiColumn(id);

                //fill the column with tiles
                int yOffset = calcOrigin().y;
                for(int i = 0; i < size; i++){
                    MaPsciiTile t = createTile(tileClass, id, i - yOffset);
                    col.add(t, MaPsciiTile.Direction.BOTTOM);
                }//end for i

                grid.add(col, dir);

                break;
        }//end switch
    }


    public void renderMap(AsciiScreen screen){
        //initialize the screen and
        //get the render context
        screen.initScreen();
        int width = screen.getWidth();
        int height = screen.getHeight();
        rcon.center.x = Math.round(width / 2.0f);
        rcon.center.y = Math.round(height / 2.0f);
        rcon.bottom = height - rcon.center.y;
        rcon.right = width - rcon.center.x;
        rcon.top = height - rcon.bottom - 1;
        rcon.left = width - rcon.right - 1;

        if(grid != null) {
            //create and populate the render list
            //this is the list of tiles to render
            //in order, the order is determined in
            //two passes (null and then non-null
            //tiles) and in columnar order top-down
            //left to right
            int preIndex = 0;
            ArrayList<RenderTile> renderList = new ArrayList<>();
            for (int i = 0; i < grid.width(); i++) {
                MaPsciiColumn col = grid.column(i);
                for (int j = 0; j < grid.height(); j++) {
                    MaPsciiTile t = grid.tile(i, j);
                    Point mapVector = new Point(mapCenter.x - i, mapCenter.y - j);
                    if (t != null) {
                        //get the deltaVector and the boxVector
                        Point dv = t.calcVector(scale, mapVector);
                        Point bv = t.calcBoxVector(scale, mapVector);

                        int pixelX = dv.x - bv.x;
                        int pixelY = dv.y - bv.y;

                        boolean xOverlap = (pixelX < rcon.xRange(mapVector));
                        boolean yOverlap = (pixelY < rcon.yRange(mapVector));

                        //add the tile to the list if it
                        //can be rendered
                        if (xOverlap && yOverlap) {
                            RenderTile rt = new RenderTile(t, mapVector, dv);
                            if (t.getIsNullTile())
                                renderList.add(preIndex++, rt);
                            else
                                renderList.add(rt);
                        }//end if
                    }//end if
                }//end for j
            }//end for i

            //now go through the render list
            //and render the tiles
            for (int i = 0; i < renderList.size(); i++) {
                RenderTile rt = renderList.get(i);
                renderTile(rt, screen);
            }//end for i
        }//end if
    }

    /***
     * Renders the specified tile to
     * the screen.
     * @param rt Structure holding the tile
     *           to be rendered.
     * @param screen Screen to render to.
     */
    private void renderTile(RenderTile rt, AsciiScreen screen){
        //determine the starting lines
        //for the render - always start
        //the screen line at zero
        int tileTop = rt.tile.calcDirectionBox(scale, MaPsciiTile.Direction.TOP);
        int sIndex = 0;
        int yOffset;
        if(rt.mapVector.y < 0)
            yOffset = -1 * rt.deltaVector.y;
        else
            yOffset = rt.deltaVector.y;
        int tIndex = yOffset + tileTop - rcon.top;

        //loop through the lines and
        //render those that need it
        boolean started = false;
        boolean done = false;
        while(!started || !done){
            //check if the screen line is valid
            if(sIndex >= 0 && sIndex < screen.getHeight()){
                //check if the tile line is valid
                if(tIndex >= 0 && tIndex < rt.tile.calcHeight(scale)){
                    started = true;

                    //render this line
                    renderTileLine(rt, screen, tIndex, sIndex);
                }//end if
                else{
                    if(started)
                        done = true;
                }//end else
            }//end if
            else{
                if(started)
                    done = true;
            }//end else

            //increment the lines
            tIndex++;
            sIndex++;
        }//end while
    }

    /***
     * Render the specified line of the
     * tile to the screen.
     * @param rt Structure holding the tile
     *           to be rendered.
     * @param screen Screen to render to.
     * @param tileLine Line number of the
     *                 tile to be rendered.
     * @param screenLine Line number of the
     *                   screen to render to.
     */
    private void renderTileLine(RenderTile rt,
                                AsciiScreen screen,
                                int tileLine,
                                int screenLine){
        //determine the starting character
        //of the screen and the tile
        //relative to one another
        int sIndex, tIndex, cutOff;
        cutOff = rt.tile.calcWidth(scale);
        if(rt.mapVector.x < 0){
            tIndex = 0;
            sIndex = (screen.getWidth() - rcon.right) +
                     (rt.deltaVector.x - rt.tile.calcDirectionBox(scale, MaPsciiTile.Direction.LEFT)) - 1;

            int tRight = rt.deltaVector.x + rt.tile.calcDirectionBox(scale, MaPsciiTile.Direction.RIGHT);
            int sRight = rcon.right;
            if(sRight < tRight)
                cutOff -= (tRight - sRight);
        }//end if
        else{
            int tl = rt.deltaVector.x + rt.tile.calcDirectionBox(scale, MaPsciiTile.Direction.LEFT);
            int sl = rcon.left;
            if(sl < tl){
                sIndex = 0;
                tIndex = tl - sl;
            }//end if
            else{
                sIndex = Math.abs(tl - sl);
                tIndex = 0;
            }//end else
        }//end else

        //first get the text to be
        //rendered from the tile
        String rawLine = rt.tile.renderLine(scale, tileLine);

        //calculate the offset for the tile
        int tileOffset = rt.tile.calcRenderOffset(scale, tileLine);
        int tileBegin = Math.max(tIndex, tileOffset);
        int tileEnd = rt.tile.calcWidth(scale) - tileOffset;
        tileEnd = Math.min(tileEnd, cutOff);

        //calculate the offset for the screen
        int scrOffset;
        if(tIndex == 0)
            scrOffset = tileOffset;
        else
            scrOffset = (tileOffset > tIndex) ? tileOffset - tIndex : 0;

        //trim the text down to fit the screen
        String lineText = rawLine.substring(tileBegin, tileEnd);

        //calculate the begin and end for the screen
        int scrBegin = sIndex + scrOffset;
        int scrEnd = scrBegin + lineText.length() - scrOffset;
        scrEnd = Math.min(scrEnd, screen.getWidth());

        //insert the tile text into the screen line
        String rndrLine = screen.getScreen().get(screenLine);
        StringBuilder sb = new StringBuilder(screen.getWidth());

        sb.append(rndrLine.substring(0, scrBegin));
        sb.append(lineText);
        sb.append(rndrLine.substring(scrEnd));

        screen.getScreen().set(screenLine, sb.toString());
    }

    /***
     * Private class for easy storage of
     * commonly needed rendering values.
     */
    private class RenderContext
    {
        int left;
        int top;
        int right;
        int bottom;
        Point center;

        RenderContext(int l,
                      int t,
                      int r,
                      int b){
            left = l;
            top = t;
            right = r;
            bottom = b;
            center = new Point(0,0);
        }

        int xRange(Point mapVector){
            return (mapVector.x < 0) ? left : right;
        }
        int yRange(Point mapVector){
            return (mapVector.y < 0) ? top : bottom;
        }
    }

    private class RenderTile
    {
        MaPsciiTile tile;
        Point mapVector;
        Point deltaVector;

        RenderTile(MaPsciiTile t,
                   Point mv,
                   Point dv){
            tile = t;
            mapVector = mv;
            deltaVector = dv;
        }
    }
}
