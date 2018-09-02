import javafx.scene.control.TextArea;
import javafx.scene.text.Font;

/***
 * Control for viewing an ascii text
 * map, allowing pan and zoom
 * functionality.
 */
public class MaPsciiPane extends TextArea
{
    //pixel-to-character conversion factors
    private static final double ADJ_P_W = 123;
    private static final double ADJ_C_W = 8;
    private static final double ADJ_P_H = 60;
    private static final double ADJ_C_H = 2;
    private static final double SCALE_W = 12;
    private static final double SCALE_H = 24;

    /***
     * Property: Screen.
     * The scratch text area to render
     * the map images to before directly
     * copying them in the text area of
     * the control.
     */
    private AsciiScreen screen;
    public AsciiScreen getScreen(){
        return screen;
    }

    /***
     * Property: Map.
     * The linked map object to
     * render from.
     */
    private MaPsciiMap map;
    public  MaPsciiMap getMap(){
        return map;
    }
    public void setMap(MaPsciiMap value){
        map = value;
    }

    /***
     * Property: BackgroundChar.
     * The character used to fill in
     * empty areas of the map.
     */
    private char backgroundChar;
    public char getBackgroundChar(){
        return backgroundChar;
    }
    public void setackgroundChar(char value){
        backgroundChar = value;
    }

    /***
     * Initializes the object during
     * construction.
     */
    private void constructorInit(){
        //default the font and editability
        setFont(new Font("Courier New", 20));
        setEditable(false);
        setWrapText(false);

        //create the screen
        screen = new AsciiScreen();
    }

    /***
     * Constructor.
     */
    public MaPsciiPane(){
        super();

        constructorInit();
    }

    /***
     * Constructor.
     * @param background Background character
     *                   to use for the screen.
     */
    public MaPsciiPane(char background){
        super();

        backgroundChar = background;
        constructorInit();
    }

    /***
     * Calculates and sets the screen
     * size based on the current
     * control size.
     */
    public void resize(){
        double taWidth = getWidth();
        double taHeight = getHeight();

        taWidth -= ADJ_P_W;
        taWidth /= SCALE_W;
        taWidth += ADJ_C_W;
        int screenWidth = (int) taWidth;

        taHeight -= ADJ_P_H;
        taHeight /= SCALE_H;
        taHeight += ADJ_C_H;
        int screenHeight = (int) taHeight;

        screen.setWidth(screenWidth);
        screen.setHeight(screenHeight);

    }

    /***
     * Renders the map to the screen
     * and then copies the image to
     * the text area.
     */
    public void render(){
        map.renderMap(screen);
        setText(screen.flush());
    }
}
