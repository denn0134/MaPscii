import java.awt.*;

public class MaPsciiDBTest implements IMaPsciiExceptionHandler
{
    public static void main(String[] args){
        MaPsciiDBTest test = new MaPsciiDBTest();
        test.runTest();
    }

    @Override
    public void handleException(Exception exc) {
        System.out.println(exc.getMessage());
    }

    public void runTest(){
        MaPsciiGrid grid = new MaPsciiGrid();
        MaPsciiMap map = new MaPsciiMap();

        int scale = 1;
        Point center = new Point(3,3);
        String mapID = "{02A0EEEE-D254-4839-8075-1C419F580CCA}";

        map.setGrid(grid);
        map.setScale(scale);

        //load the map from the database
        String connStr = "jdbc:sqlite:C:\\\\Dev\\\\MaPscii\\\\data\\\\MaPsciiDB.db";
        MaPsciiDBM dbm = new MaPsciiDBM(connStr, this);

        dbm.connect();

        if(dbm.isConnected())
            System.out.println("Connected");

        grid.setGuid(mapID);
        dbm.loadMap(grid);

        dbm.disconnect();

        map.setMapCenter(center);

        AsciiScreen screen = new AsciiScreen(40,30,MaPsciiTile.GSTAR);

        map.renderMap(screen);
        System.out.println(screen.flush());
    }
}
