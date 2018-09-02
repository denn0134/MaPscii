import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;

/***
 * Form for testing the MaPscii component.
 */
public class MaPsciiForm extends Application
{
    private static final String FMT_CONN_STR = "jdbc:sqlite:%0$s";

    MaPsciiPane mapPane;
    MaPsciiMap map;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //map objects
        mapPane = new MaPsciiPane(MaPsciiTile.GSTAR);
        map = new MaPsciiMap();
        mapPane.setMap(map);

        //layout objects
        BorderPane root = new BorderPane();
        TilePane topPanel = new TilePane();
        topPanel.setHgap(4);
        topPanel.setVgap(4);
        topPanel.setPrefColumns(3);
        HBox topLeft = new HBox();
        HBox topCenter = new HBox();
        topCenter.setAlignment(Pos.CENTER);
        HBox topRight = new HBox();
        topRight.setAlignment(Pos.CENTER_RIGHT);
        StackPane centerLeft = new StackPane();
        StackPane centerRight = new StackPane();
        StackPane bottomPanel = new StackPane();

        //buttons
        Button btnLoad = new Button("Load Map");
        btnLoad.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fc = new FileChooser();
                fc.setTitle("Select Map Database File");
                fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Database files", "*.db"));
                //fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Map files", "*.map"));
                File file = fc.showOpenDialog(primaryStage);
                if(file != null){
                    String path = file.getPath();
                    path = path.replace("\\", "\\\\");
                    String dbconnect = String.format(FMT_CONN_STR, path);

                    MaPsciiDBM dbm = new MaPsciiDBM(dbconnect, new Handler());
                    dbm.connect();

                    MaPsciiGrid grid = new MaPsciiGrid();
                    grid.setGuid("{02A0EEEE-D254-4839-8075-1C419F580CCA}");
                    map.setGrid(grid);
                    dbm.loadMap(grid);
                    dbm.disconnect();

                    //set the scale and center
                    map.setScale(1);
                    map.setMapCenter(new Point(grid.width() / 2, grid.height() / 2));

                    mapPane.render();
                }//end if
            }
        });
        Button btnSave = new Button("Save Map");
        btnSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                map.saveMap();
            }
        });
        Button btnIn = new Button("Zoom In");
        btnIn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int scale = map.getScale();
                scale++;
                map.setScale(scale);
                mapPane.render();
            }
        });
        Button btnOut = new Button("Zoom Out");
        btnOut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int scale = map.getScale();
                if(scale > 1) {
                    scale--;
                    map.setScale(scale);
                    mapPane.render();
                }//end if
            }
        });
        Button btnUp = new Button("Pan");
        btnUp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                panMap(0, -1);
            }
        });
        Button btnLeft = new Button("Pan");
        btnLeft.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                panMap(-1, 0);
            }
        });
        Button btnRight = new Button("Pan");
        btnRight.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                panMap(1, 0);
            }
        });
        Button btnDown = new Button("Pan");
        btnDown.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                panMap(0, 1);
            }
        });

        //set up the layouts
        topLeft.getChildren().addAll(btnLoad, btnSave);
        topCenter.getChildren().add(btnUp);
        topRight.getChildren().addAll(btnIn, btnOut);
        topPanel.getChildren().addAll(topLeft, topCenter, topRight);

        centerLeft.getChildren().add(btnLeft);
        centerRight.getChildren().add(btnRight);
        bottomPanel.getChildren().add(btnDown);

        root.setTop(topPanel);
        root.setLeft(centerLeft);
        root.setCenter(mapPane);
        root.setRight(centerRight);
        root.setBottom(bottomPanel);

        //set up the resize event
        ChangeListener<Number> stageResize = (observable, oldValue, newValue) -> {
            mapPane.resize();
            mapPane.render();
        };//end resize listener
        primaryStage.widthProperty().addListener(stageResize);
        primaryStage.heightProperty().addListener(stageResize);

        Scene scene = new Scene(root, 500, 480);

        primaryStage.setTitle("MaPscii Control");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /***
     * Pans the map by the x and y
     * provided and re-renders it.
     * @param x The x distance in tiles to pan by.
     * @param y The y distance in tiles to pan by.
     */
    private void panMap(int x, int y){
        Point p = map.getMapCenter();
        p.x += x;
        p.y += y;
        map.setMapCenter(p);
        mapPane.render();
    }

    private class Handler implements IMaPsciiExceptionHandler
    {
        @Override
        public void handleException(Exception exc) {

        }
    }
}
