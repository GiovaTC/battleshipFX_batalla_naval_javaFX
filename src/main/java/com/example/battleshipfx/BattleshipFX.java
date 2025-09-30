package com.example.battleshipfx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.controlsfx.control.spreadsheet.Grid;

import java.util.*;
import java.util.stream.IntStream;

import static java.lang.Long.SIZE;

public class BattleshipFX extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    // config
    private final int size = 10;
    private final List<ShipType> SHIP_TYPES = Arrays.asList(
            new ShipType("carrier", 5),
            new ShipType("battleship", 4),
            new ShipType("cruiser", 3),
            new ShipType("submarine", 3),
            new ShipType("destroyer", 2)
    );

    // UI
    private GridPane playerGrid;
    private GridPane enemyGrid;
    private Label statusLabel;
    private ToggleButton rotateBtn;
    private Button startBtn;
    private VBox shipBox; // ship selection / drag source

    // model
    private CellFX[][] playerCells;
    private CellFX[][] enemyCells;

    private final List<Ship> playerShips = new ArrayList<>();
    private final List<Ship> enemyShips = new ArrayList<>();

    private boolean placingPhase = true;
    private ShipType selectedShipType = null; // selected from list
    private boolean placeHorizontal = true;

    private final Random rnd = new Random();
    @Override
    public void start(Stage primaryStage) {
        playerCells = new CellFX[SIZE][SIZE];
        enemyCells = new CellFX[SIZE][SIZE];

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // top = title and controls :. .

    }
}
