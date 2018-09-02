import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MaPsciiTest
{
    public static void main(String[] args){
        int scale = 2;
        MaPsciiSquare square = new MaPsciiSquare(0, 0);
        int h = square.calcHeight(scale);

        for(int i = 0; i < h; i++){
            System.out.println(square.renderLine(scale, i));
        }//end for i

        MaPsciiGrid grid = new MaPsciiGrid();
        grid.setName("First Grid");
        grid.setFileName("C:\\Debug\\First Grid.json");

        MaPsciiMap map = new MaPsciiMap();
        map.setGrid(grid);

        try {
            System.out.println(getGridDims(grid));

            map.addTiles(MaPsciiTile.Direction.BOTTOM, MaPsciiSquare.class);
            map.addTiles(MaPsciiTile.Direction.BOTTOM, MaPsciiSquare.class);
            map.addTiles(MaPsciiTile.Direction.TOP, MaPsciiSquare.class);
            map.addTiles(MaPsciiTile.Direction.TOP, MaPsciiSquare.class);
            map.addTiles(MaPsciiTile.Direction.LEFT, MaPsciiSquare.class);
            map.addTiles(MaPsciiTile.Direction.RIGHT, MaPsciiSquare.class);
            map.addTiles(MaPsciiTile.Direction.RIGHT, MaPsciiSquare.class);
            map.addTiles(MaPsciiTile.Direction.RIGHT, MaPsciiSquare.class);
            map.addTiles(MaPsciiTile.Direction.RIGHT, MaPsciiSquare.class);
            map.addTiles(MaPsciiTile.Direction.RIGHT, MaPsciiSquare.class);
            map.addTiles(MaPsciiTile.Direction.RIGHT, MaPsciiSquare.class);
            map.addTiles(MaPsciiTile.Direction.BOTTOM, MaPsciiSquare.class);
            map.addTiles(MaPsciiTile.Direction.BOTTOM, MaPsciiSquare.class);
            map.addTiles(MaPsciiTile.Direction.BOTTOM, MaPsciiSquare.class);
            map.addTiles(MaPsciiTile.Direction.BOTTOM, MaPsciiSquare.class);
            map.addTiles(MaPsciiTile.Direction.BOTTOM, MaPsciiSquare.class);

            System.out.println(getGridDims(grid));

            map.setMapCenter(new Point(3, 3));
            map.setScale(3);

            AsciiScreen screen = new AsciiScreen(40, 24, MaPsciiTile.GSTAR);
            map.renderMap(screen);

            System.out.println("Rendered map");
            System.out.println(screen.flush());

            map.saveMap();
        }//end try
        catch(MaPsciiContextException mce){
            System.out.println(mce.getMessage());
        }//end catch mce
    }

    private static String getGridDims(MaPsciiGrid g){
        return String.format("(%1$d,%2$d)", g.getColumns().size(), g.height());
    }
}
