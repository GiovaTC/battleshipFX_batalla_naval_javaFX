package com.example.battleshipfx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.*;

public class BattleshipFX extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    // config
    private final int SIZE = 10;

    // UI
    private GridPane playerGrid;
    private GridPane enemyGrid;
    private Label statusLabel;
    private ToggleButton rotateBtn;
    private Button startBtn;
    private VBox shipBox; // selección de barcos

    // model
    private CellFX[][] playerCells;
    private CellFX[][] enemyCells;

    private final List<Ship> playerShips = new ArrayList<>();
    private final List<Ship> enemyShips = new ArrayList<>();

    private boolean placingPhase = true;
    private ShipType selectedShipType = null;
    private boolean placeHorizontal = true;

    private final Random rnd = new Random();

    @Override
    public void start(Stage primaryStage) {
        playerCells = new CellFX[SIZE][SIZE];
        enemyCells = new CellFX[SIZE][SIZE];

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // top = título y controles
        HBox top = new HBox(10);
        top.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Batalla Naval - JavaFX");
        title.setFont(Font.font(20));
        rotateBtn = new ToggleButton("Horizontal");
        rotateBtn.setOnAction(e -> {
            placeHorizontal = !placeHorizontal;
            rotateBtn.setText(placeHorizontal ? "Horizontal" : "Vertical");
        });

        startBtn = new Button("Comenzar Partida");
        startBtn.setOnAction(e -> onStart());

        Button autoPlaceBtn = new Button("Colocar Aleatoriamente");
        autoPlaceBtn.setOnAction(e -> {
            clearPlayerBoard();
            placeShipsRandom(playerShips, playerCells);
            refreshPlayerView();
            selectedShipType = null;
            if (shipBox != null) shipBox.setDisable(true);
            statusLabel.setText("Barcos colocados. Pulsa 'Comenzar Partida'.");
        });

        top.getChildren().addAll(title, rotateBtn, autoPlaceBtn, startBtn);

        statusLabel = new Label("Coloca tus barcos.");
        VBox bottom = new VBox(statusLabel);
        bottom.setAlignment(Pos.CENTER);

        root.setTop(top);
        root.setBottom(bottom);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("BattleshipFX");
        primaryStage.show();
    }

    // iniciar partida
    private void onStart() {
        if (playerShips.size() < ShipType.values().length) {
            statusLabel.setText("Aún faltan barcos por colocar. Usa 'Colocar Aleatoriamente'.");
            return;
        }
        placingPhase = false;
        if (shipBox != null) shipBox.setDisable(true);
        startBtn.setDisable(true);
        statusLabel.setText("Partida iniciada. Haz clic en el tablero enemigo para disparar.");

        // ocultar barcos enemigos visualmente
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                enemyCells[i][j].rect.setFill(Color.LIGHTBLUE);
            }
        }
    }

    private void clearPlayerBoard() {
        playerShips.clear();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                playerCells[i][j].clear();
            }
        }
    }

    // colocar barcos aleatoriamente
    private void placeShipsRandom(List<Ship> shipList, CellFX[][] cells) {
        shipList.clear();
        for (ShipType st : ShipType.values()) {
            boolean placed = false;
            for (int attempt = 0; attempt < 500 && !placed; attempt++) {
                int r = rnd.nextInt(SIZE);
                int c = rnd.nextInt(SIZE);
                boolean horiz = rnd.nextBoolean();
                if (canPlace(cells, r, c, st.getSize(), horiz)) {
                    Ship s = new Ship(st);
                    if (horiz) {
                        for (int j = c; j < c + st.getSize(); j++) {
                            s.addPosition(cells[r][j]);
                        }
                    } else {
                        for (int i = r; i < r + st.getSize(); i++) {
                            s.addPosition(cells[i][c]);
                        }
                    }
                    shipList.add(s);
                    placed = true;
                }
            }
            if (!placed) throw new IllegalStateException("No se pudo colocar: " + st.getName());
        }
    }

    private boolean canPlace(CellFX[][] cells, int r, int c, int len, boolean horiz) {
        if (horiz) {
            if (c + len > SIZE) return false;
            for (int j = c; j < c + len; j++) if (cells[r][j].hasShip()) return false;
        } else {
            if (r + len > SIZE) return false;
            for (int i = r; i < r + len; i++) if (cells[i][c].hasShip()) return false;
        }
        return true;
    }

    private void refreshPlayerView() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                CellFX cell = playerCells[i][j];
                if (cell.hasShip()) cell.rect.setFill(Color.LIGHTGRAY);
                else cell.rect.setFill(Color.LIGHTBLUE);
            }
        }
    }
}
