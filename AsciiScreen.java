import java.util.ArrayList;

/***
 * Class representing a screen
 * scratch pad to render to.  The
 * screen can then be played to an
 * output control or to the console.
 */
public class AsciiScreen
{
    /***
     * Property: Width
     * The width of the screen measured
     * in characters.  All Strings in
     * the screen ArrayList will have a
     * length equal to the width.
     */
    private int width;
    public int getWidth(){
        return width;
    }
    public void setWidth(int value){
        width = value;
    }

    /***
     * Property: Height
     * The height of the screen measured
     * in characters (or lines).  The
     * screen ArrayList has a size equal
     * to the height.
     */
    private int height;
    public int getHeight(){
        return height;
    }
    public void setHeight(int value){
        height = value;
    }

    /***
     * Property: Background
     * The character used for filling
     * in the background of the screen,
     * i.e. where no tiles get rendered.
     */
    private char background = MaPsciiTile.GBLANK;
    public char getBackground() {
        return background;
    }
    public void setBackground(char value) {
        this.background = value;
    }

    /***
     * Property: Screen
     * The array list of String objects
     * which make up the screen.
     */
    private ArrayList<String> screen;
    public ArrayList<String> getScreen(){
        return screen;
    }

    /***
     * Constructor.
     */
    public AsciiScreen(){
        screen = new ArrayList<>();
    }

    /***
     * Constructor.
     * @param bg The default background
     *           character.
     */
    public AsciiScreen(char bg){
        screen = new ArrayList<>();
        background = bg;
    }

    /***
     * Constructor.
     * @param w The width of the screen.
     * @param h The height of the screen.
     */
    public AsciiScreen(int w, int h){
        screen = new ArrayList<>();
        width = w;
        height = h;
        initScreen();
    }

    /***
     * Constructor.
     * @param w The width of the screen.
     * @param h The height of the screen.
     * @param bg The default background
     *           character.
     */
    public AsciiScreen(int w, int h, char bg){
        screen = new ArrayList<>();
        width = w;
        height = h;
        background = bg;
        initScreen();
    }

    /***
     * Creates a blank line for the screen.
     * @return Returns a String object of
     * length = width filled with the
     * background character.
     */
    public String blankLine(){
        char[] c = new char[width];
        for(int i = 0; i < c.length; i++){
            c[i] = background;
        }//end for i

        return new String(c);
    }

    /***
     * Initializes the screen to a blank
     * screen of the correct width and
     * height filled with the default
     * background character.
     */
    public void initScreen(){
        screen.clear();
        String s = blankLine();

        for(int i = 0; i < height; i++){
            screen.add(s);
        }//end for i
    }

    /***
     * Generates a String object with
     * newlines which is the contents
     * of the screen.
     * @return Returns the contents
     *         of the screen as a
     *         String object.
     */
    public String flush(){
        StringBuilder sb = new StringBuilder();

        for( int i = 0; i < screen.size(); i++){
            if(i == screen.size() - 1)
                sb.append(screen.get(i));
            else
                sb.append(screen.get(i) + '\n');
        }//end for i

        return sb.toString();
    }
}
