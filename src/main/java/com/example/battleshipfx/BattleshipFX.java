package com.example.battleshipfx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.*;

public class BattleshipFX extends Application {

    private static final int SIZE = 10;
    private Button[][] playerBoard = new Button[SIZE][SIZE];
    private Button[][] cpuBoard = new Button[SIZE][SIZE];

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Batalla Naval - JavaFX");

        // Título
        Label title = new Label("Batalla Naval");
        title.setFont(new Font("Arial", 24));
        title.setTextFill(Color.DARKBLUE);

        HBox titleBox = new HBox(title);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(10));

        // Tableros
        GridPane playerGrid = createBoard(playerBoard, "Jugador");
        GridPane cpuGrid = createBoard(cpuBoard, "CPU");

        HBox boardsBox = new HBox(20, playerGrid, cpuGrid);
        boardsBox.setAlignment(Pos.CENTER);

        // Botones de control
        Button startButton = new Button("Iniciar Juego");
        Button resetButton = new Button("Reiniciar");

        HBox controls = new HBox(15, startButton, resetButton);
        controls.setAlignment(Pos.CENTER);
        controls.setPadding(new Insets(10));

        // Layout principal
        BorderPane root = new BorderPane();
        root.setTop(titleBox);
        root.setCenter(boardsBox);
        root.setBottom(controls);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createBoard(Button[][] board, String title) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(2);
        grid.setVgap(2);
        grid.setAlignment(Pos.CENTER);

        Label label = new Label(title);
        label.setFont(new Font("Arial", 18));
        label.setTextFill(Color.BLACK);

        HBox titleBox = new HBox(label);
        titleBox.setAlignment(Pos.CENTER);

        // Crear celdas
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Button cell = new Button();
                cell.setPrefSize(30, 30);
                cell.setStyle("-fx-background-color: lightblue; -fx-border-color: black;");
                board[row][col] = cell;
                grid.add(cell, col, row);
            }
        }

        return grid;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

// --------------------------------------------------
// CLASES AUXILIARES
// --------------------------------------------------

class CellFX {
    public final int row, col;
    public Rectangle rect;
    public boolean shot = false;
    public Ship ship = null;

    public CellFX(int row, int col) {
        this.row = row;
        this.col = col;
        rect = new Rectangle();
        rect.setFill(Color.LIGHTBLUE);
        rect.setStroke(Color.BLACK);
    }

    public boolean hasShip() {
        return ship != null;
    }

    public void clear() {
        ship = null;
        shot = false;
        rect.setFill(Color.LIGHTBLUE);
    }
}

class Ship {
    public final ShipType type;
    public final List<Pos> positions = new ArrayList<>();
    public final List<Pos> hits = new ArrayList<>();

    public Ship(ShipType type) {
        this.type = type;
    }

    public boolean isSunk() {
        return hits.size() == type.getSize();
    }
}

enum ShipType {
    CARRIER("Portaaviones", 5),
    BATTLESHIP("Acorazado", 4),
    CRUISER("Crucero", 3),
    SUBMARINE("Submarino", 3),
    DESTROYER("Destructor", 2);

    private final String name;
    private final int size;

    ShipType(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }
}

class Pos {
    public final int row, col;

    public Pos(int row, int col) {
        this.row = row;
        this.col = col;
    }
}
public class BattleshipFX extends Application {

    private static final int SIZE = 10;
    private Button[][] playerBoard = new Button[SIZE][SIZE];
    private Button[][] cpuBoard = new Button[SIZE][SIZE];

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Batalla Naval - JavaFX");

        // Título
        Label title = new Label("Batalla Naval");
        title.setFont(new Font("Arial", 24));
        title.setTextFill(Color.DARKBLUE);

        HBox titleBox = new HBox(title);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(10));

        // Tableros
        GridPane playerGrid = createBoard(playerBoard, "Jugador");
        GridPane cpuGrid = createBoard(cpuBoard, "CPU");

        HBox boardsBox = new HBox(20, playerGrid, cpuGrid);
        boardsBox.setAlignment(Pos.CENTER);

        // Botones de control
        Button startButton = new Button("Iniciar Juego");
        Button resetButton = new Button("Reiniciar");

        HBox controls = new HBox(15, startButton, resetButton);
        controls.setAlignment(Pos.CENTER);
        controls.setPadding(new Insets(10));

        // Layout principal
        BorderPane root = new BorderPane();
        root.setTop(titleBox);
        root.setCenter(boardsBox);
        root.setBottom(controls);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createBoard(Button[][] board, String title) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(2);
        grid.setVgap(2);
        grid.setAlignment(Pos.CENTER);

        Label label = new Label(title);
        label.setFont(new Font("Arial", 18));
        label.setTextFill(Color.BLACK);

        HBox titleBox = new HBox(label);
        titleBox.setAlignment(Pos.CENTER);

        // Crear celdas
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Button cell = new Button();
                cell.setPrefSize(30, 30);
                cell.setStyle("-fx-background-color: lightblue; -fx-border-color: black;");
                board[row][col] = cell;
                grid.add(cell, col, row);
            }
        }

        return grid;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

// --------------------------------------------------
// CLASES AUXILIARES
// --------------------------------------------------

class CellFX {
    public final int row, col;
    public Rectangle rect;
    public boolean shot = false;
    public Ship ship = null;

    public CellFX(int row, int col) {
        this.row = row;
        this.col = col;
        rect = new Rectangle();
        rect.setFill(Color.LIGHTBLUE);
        rect.setStroke(Color.BLACK);
    }

    public boolean hasShip() {
        return ship != null;
    }

    public void clear() {
        ship = null;
        shot = false;
        rect.setFill(Color.LIGHTBLUE);
    }
}

class Ship {
    public final ShipType type;
    public final List<Pos> positions = new ArrayList<>();
    public final List<Pos> hits = new ArrayList<>();

    public Ship(ShipType type) {
        this.type = type;
    }

    public boolean isSunk() {
        return hits.size() == type.getSize();
    }
}

enum ShipType {
    CARRIER("Portaaviones", 5),
    BATTLESHIP("Acorazado", 4),
    CRUISER("Crucero", 3),
    SUBMARINE("Submarino", 3),
    DESTROYER("Destructor", 2);

    private final String name;
    private final int size;

    ShipType(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }
}

class Pos {
    public final int row, col;

    public Pos(int row, int col) {
        this.row = row;
        this.col = col;
    }
}