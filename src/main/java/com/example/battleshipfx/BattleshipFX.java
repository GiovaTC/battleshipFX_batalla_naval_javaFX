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
}
