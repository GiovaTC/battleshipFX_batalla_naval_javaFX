# BattleshipFX — Batalla Naval (JavaFX)

<img width="203" height="193" alt="image" src="https://github.com/user-attachments/assets/acd0ce8a-579d-497b-a76e-797430d0bb13" />  

<img width="1536" height="1024" alt="image" src="https://github.com/user-attachments/assets/5496a092-c548-475b-841e-3bce5dec6c83" />

Archivo principal: **`BattleshipFX.java`**  
Versión JavaFX del juego **Batalla Naval (Jugador vs CPU)**.  
Archivo único listo para pegar en IntelliJ: crea un proyecto Java y pega el contenido en un archivo llamado `BattleshipFX.java`.

---

## 📑 Contenido
- ✅ Resumen y características
- ✅ Requisitos
- ✅ Instrucciones rápidas para ejecutar en IntelliJ
- ✅ Código completo (`BattleshipFX.java`) — copia y pega en tu proyecto
- ✅ Notas y posibles mejoras

---

## 📌 Resumen corto
- Interfaz con **dos tableros** (Tu tablero y Tablero enemigo).
- Colocación de barcos por **selección + clic y drag & drop**. Rotación con botón.
- **CPU coloca barcos aleatoriamente** y juega con IA sencilla (disparos aleatorios).
- Lógica para disparos, impactos, hundimientos y fin de juego.

---

## 🚀 Características implementadas
- Interfaz gráfica con dos tableros (**Jugador y Enemigo**).
- Colocación de barcos mediante selección + clic, y drag & drop desde la lista de barcos.
- Rotación de barcos con botón **"Rotar"** (Horizontal / Vertical).
- CPU coloca barcos aleatoriamente.
- **Turnos:**
    - Jugador dispara haciendo clic en el tablero enemigo.
    - CPU dispara aleatoriamente.
- Indicadores visuales simples:
    - Agua → `o`
    - Impacto → `X`
    - Barco propio → `S`

---

## ⚙️ Requisitos
- **JDK 11+** (recomendado **JDK 17**).
- **JavaFX SDK** instalado o usar dependencias **OpenJFX**.

Si ejecutas desde IntelliJ con JavaFX SDK, añade en **VM options** al crear la Run Configuration:

(Reemplaza /ruta/a/javafx/lib por la ruta real a la carpeta lib de tu SDK OpenJFX).
👉 Opcional: usar Maven/Gradle con dependencias org.openjfx:javafx-*.

🖥️ Instrucciones rápidas (IntelliJ)

Crear un nuevo proyecto Java (JDK 11+).

Añadir JavaFX al proyecto (SDK local o dependencias Maven/Gradle).

Crear un archivo BattleshipFX.java y pegar el código completo (ver abajo).

Configurar VM options si usas SDK local (ver sección Requisitos).

Ejecutar la clase BattleshipFX (extiende Application) .

/*
BattleshipFX.java

Versión JavaFX del juego Batalla Naval (Jugador vs CPU).
Archivo único para pegar en IntelliJ: crea un proyecto Java y pega este contenido
en un archivo llamado BattleshipFX.java dentro de un paquete (o sin paquete).

Requisitos:
- JDK 11+ (recomendado 17)
- JavaFX SDK instalado o usar dependencias (OpenJFX).
  Al ejecutar desde IntelliJ añade las VM options:
  --module-path /ruta/a/javafx/lib --add-modules javafx.controls,javafx.fxml

Instrucciones rápidas:
1. Crear nuevo proyecto Java en IntelliJ.
2. Añadir JavaFX en run configuration (module-path) o usar Maven/Gradle con OpenJFX.
3. Crear un archivo BattleshipFX.java y pegar este código.
4. Ejecutar la clase BattleshipFX (Application).

Características implementadas:
- Interfaz gráfica con dos tableros (Jugador y Enemigo).
- Colocación de barcos mediante selección + clic, y drag & drop desde la lista de barcos.
- Rotación de barcos con botón "Rotar".
- CPU coloca barcos aleatoriamente.
- Turnos: jugador dispara haciendo clic en el tablero enemigo; CPU dispara aleatoriamente.
- Indicadores visuales para agua (o), impacto (X) y barco propio (S).

Notas:
- Este archivo está pensado como ejemplo didáctico y como punto de partida.
- Se puede refactorizar separando clases en archivos, mejorando IA (hunt/target),
  añadiendo animaciones, efectos, etc.
  */
```bash
--module-path /ruta/a/javafx/lib --add-modules javafx.controls,javafx.fxml

*/

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

import java.util.*;
import java.util.stream.IntStream;

public class BattleshipFX extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    // Config
    private final int SIZE = 10;
    private final List<ShipType> SHIP_TYPES = Arrays.asList(
            new ShipType("Carrier", 5),
            new ShipType("Battleship", 4),
            new ShipType("Cruiser", 3),
            new ShipType("Submarine", 3),
            new ShipType("Destroyer", 2)
    );

    // UI
    private GridPane playerGrid;
    private GridPane enemyGrid;
    private Label statusLabel;
    private ToggleButton rotateBtn;
    private Button startBtn;
    private VBox shipBox; // ship selection / drag source

    // Model
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

        // Top - title and controls
        HBox top = new HBox(10);
        top.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Batalla Naval - JavaFX");
        title.setFont(Font.font(20));
        rotateBtn = new ToggleButton("Horizontal");
        rotateBtn.setOnAction(e -> toggleRotation());

        startBtn = new Button("Comenzar partida");
        startBtn.setDisable(false); // available after manual placement or auto
        startBtn.setOnAction(e -> onStart());

        Button autoPlaceBtn = new Button("Colocar aleatoriamente");
        autoPlaceBtn.setOnAction(e -> {
            clearPlayerBoard();
            placeShipsRandom(playerShips, playerCells);
            refreshPlayerView();
            selectedShipType = null;
            shipBox.setDisable(true);
            statusLabel.setText("Barcos colocados. Pulsa 'Comenzar partida'.");
        });

        top.getChildren().addAll(title, rotateBtn, autoPlaceBtn, startBtn);

        // Center - boards
        HBox center = new HBox(20);
        center.setAlignment(Pos.CENTER);

        playerGrid = buildGrid(playerCells, true);
        enemyGrid = buildGrid(enemyCells, false);

        VBox leftBox = new VBox(8, new Label("Tu tablero"), playerGrid);
        leftBox.setAlignment(Pos.CENTER);
        VBox rightBox = new VBox(8, new Label("Tablero enemigo"), enemyGrid);
        rightBox.setAlignment(Pos.CENTER);

        center.getChildren().addAll(leftBox, rightBox);

        // Left - ship selection
        shipBox = new VBox(8);
        shipBox.setPadding(new Insets(8));
        shipBox.setPrefWidth(180);
        shipBox.setStyle("-fx-border-color: gray; -fx-border-radius: 6; -fx-background-radius:6;");
        Label shipsLabel = new Label("Barcos (arrastrar o seleccionar + clic)");
        shipBox.getChildren().add(shipsLabel);
        buildShipSelection();

        // Bottom - status
        statusLabel = new Label("Coloca tus barcos.");

        root.setTop(top);
        root.setLeft(shipBox);
        root.setCenter(center);
        root.setBottom(statusLabel);
        BorderPane.setAlignment(statusLabel, Pos.CENTER);
        BorderPane.setMargin(statusLabel, new Insets(10));

        // Setup enemy ships randomly (hidden until start)
        placeShipsRandom(enemyShips, enemyCells);

        Scene scene = new Scene(root, 1000, 640);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Batalla Naval - JavaFX");
        primaryStage.show();
    }

    // Toggle rotation button text
    private void toggleRotation() {
        placeHorizontal = !rotateBtn.isSelected();
        rotateBtn.setText(placeHorizontal ? "Horizontal" : "Vertical");
    }

    // Build ship selection UI (draggable labels)
    private void buildShipSelection() {
        for (ShipType st : SHIP_TYPES) {
            Label lbl = new Label(st.name + " (" + st.size + ")");
            lbl.setPrefWidth(150);
            lbl.setPadding(new Insets(6));
            lbl.setStyle("-fx-border-color: black; -fx-background-color: lightgray;");

            // click to select for placement
            lbl.setOnMouseClicked(e -> {
                selectedShipType = st;
                statusLabel.setText("Seleccionado: " + st.name + ". Haz clic en el tablero para colocar.");
            });

            // drag support
            lbl.setOnDragDetected(e -> {
                if (isShipAlreadyPlaced(playerShips, st)) {
                    // don't allow dragging already-placed ships
                    return;
                }
                Dragboard db = lbl.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(st.name);
                db.setContent(content);
                e.consume();
            });

            shipBox.getChildren().add(lbl);
        }
    }

    private boolean isShipAlreadyPlaced(List<Ship> shipList, ShipType st) {
        return shipList.stream().anyMatch(s -> s.type.name.equals(st.name));
    }

    // Build a grid; if playerGrid then allow drop for ship placement; if enemyGrid allow shooting
    private GridPane buildGrid(CellFX[][] cells, boolean isPlayer) {
        GridPane grid = new GridPane();
        grid.setGridLinesVisible(true);
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                CellFX cell = new CellFX(r, c);
                cells[r][c] = cell;
                Rectangle rect = cell.rect;
                rect.setWidth(30);
                rect.setHeight(30);
                StackPane stack = new StackPane(rect);
                stack.setOnMouseClicked(e -> {
                    if (placingPhase && isPlayer) {
                        // place selected ship by click
                        if (selectedShipType != null) {
                            boolean ok = tryPlacePlayerShip(selectedShipType, r, c, placeHorizontal);
                            if (ok) {
                                statusLabel.setText("Barco " + selectedShipType.name + " colocado.");
                                selectedShipType = null;
                                refreshPlayerView();
                                // disable ship label if already placed
                                if (playerShips.size() == SHIP_TYPES.size()) {
                                    shipBox.setDisable(true);
                                    statusLabel.setText("Todos los barcos colocados. Pulsa 'Comenzar partida'.");
                                }
                            } else {
                                statusLabel.setText("No se puede colocar ahí. Intenta otra posición.");
                            }
                        }
                    } else if (!placingPhase && !isPlayer) {
                        // shoot enemy
                        handlePlayerShoot(r, c);
                    }
                });

                if (isPlayer) {
                    // allow drag-over and drop
                    stack.setOnDragOver(ev -> {
                        if (ev.getGestureSource() != stack && ev.getDragboard().hasString()) {
                            ev.acceptTransferModes(TransferMode.MOVE);
                        }
                        ev.consume();
                    });
                    stack.setOnDragDropped(ev -> {
                        Dragboard db = ev.getDragboard();
                        boolean success = false;
                        if (db.hasString()) {
                            String shipName = db.getString();
                            ShipType st = SHIP_TYPES.stream().filter(s -> s.name.equals(shipName)).findFirst().orElse(null);
                            if (st != null && !isShipAlreadyPlaced(playerShips, st)) {
                                success = tryPlacePlayerShip(st, r, c, placeHorizontal);
                                if (success) {
                                    statusLabel.setText("Barco " + st.name + " colocado por drag&drop.");
                                    if (playerShips.size() == SHIP_TYPES.size()) {
                                        shipBox.setDisable(true);
                                        statusLabel.setText("Todos los barcos colocados. Pulsa 'Comenzar partida'.");
                                    }
                                } else {
                                    statusLabel.setText("No se pudo colocar allí (colisión o fuera).");
                                }
                            }
                        }
                        ev.setDropCompleted(success);
                        ev.consume();
                    });
                }

                grid.add(stack, c, r);
            }
        }
        return grid;
    }

    // Attempt to place ship for player; returns true if placed
    private boolean tryPlacePlayerShip(ShipType st, int r, int c, boolean horiz) {
        Ship ship = new Ship(st);
        // check bounds and collisions
        if (horiz) {
            if (c + st.size > SIZE) return false;
            for (int j = c; j < c + st.size; j++) if (playerCells[r][j].hasShip()) return false;
            for (int j = c; j < c + st.size; j++) {
                playerCells[r][j].ship = ship;
                ship.positions.add(new Pos(r, j));
            }
        } else {
            if (r + st.size > SIZE) return false;
            for (int i = r; i < r + st.size; i++) if (playerCells[i][c].hasShip()) return false;
            for (int i = r; i < r + st.size; i++) {
                playerCells[i][c].ship = ship;
                ship.positions.add(new Pos(i, c));
            }
        }
        playerShips.add(ship);
        return true;
    }

    // Place ships randomly on given cells list
    private void placeShipsRandom(List<Ship> shipList, CellFX[][] cells) {
        shipList.clear();
        for (ShipType st : SHIP_TYPES) {
            boolean placed = false;
            for (int attempt = 0; attempt < 500 && !placed; attempt++) {
                int r = rnd.nextInt(SIZE);
                int c = rnd.nextInt(SIZE);
                boolean horiz = rnd.nextBoolean();
                if (canPlace(cells, r, c, st.size, horiz)) {
                    Ship s = new Ship(st);
                    if (horiz) {
                        for (int j = c; j < c + st.size; j++) {
                            cells[r][j].ship = s;
                            s.positions.add(new Pos(r, j));
                        }
                    } else {
                        for (int i = r; i < r + st.size; i++) {
                            cells[i][c].ship = s;
                            s.positions.add(new Pos(i, c));
                        }
                    }
                    shipList.add(s);
                    placed = true;
                }
            }
            if (!placed) throw new IllegalStateException("No se pudo colocar: " + st.name);
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

    private void clearPlayerBoard() {
        playerShips.clear();
        for (int i = 0; i < SIZE; i++) for (int j = 0; j < SIZE; j++) playerCells[i][j].clear();
    }

    private void refreshPlayerView() {
        for (int i = 0; i < SIZE; i++) for (int j = 0; j < SIZE; j++) {
            CellFX cell = playerCells[i][j];
            if (cell.hasShip()) cell.rect.setFill(Color.LIGHTGRAY);
            else cell.rect.setFill(Color.LIGHTBLUE);
        }
    }

    // Start the game: lock placements, enable shooting
    private void onStart() {
        if (playerShips.size() < SHIP_TYPES.size()) {
            statusLabel.setText("Aún faltan barcos por colocar. Puedes colocar aleatoriamente.");
            return;
        }
        placingPhase = false;
        shipBox.setDisable(true);
        startBtn.setDisable(true);
        statusLabel.setText("Partida iniciada. Es tu turno: haz clic en el tablero enemigo para disparar.");
        // hide enemy ships visuals (ensure enemy cells show water color)
        for (int i = 0; i < SIZE; i++) for (int j = 0; j < SIZE; j++) enemyCells[i][j].rect.setFill(Color.LIGHTBLUE);
    }

    // Player shoots enemy at r,c
    private void handlePlayerShoot(int r, int c) {
        CellFX cell = enemyCells[r][c];
        if (cell.shot) {
            statusLabel.setText("Ya disparaste ahí.");
            return;
        }
        cell.shot = true;
        if (cell.hasShip()) {
            cell.rect.setFill(Color.DARKRED);
            cell.markX();
            Ship s = cell.ship;
            s.hits.add(new Pos(r, c));
            statusLabel.setText("¡Impacto!");
            if (s.isSunk()) {
                statusLabel.setText("¡Hundiste un barco enemigo: " + s.type.name + "!");
                if (allSunk(enemyShips)) {
                    statusLabel.setText("¡Ganaste! Todos los barcos enemigos han sido hundidos.");
                    endGame(true);
                    return;
                }
            }
            // player keeps turn on hit
        } else {
            cell.rect.setFill(Color.DARKBLUE);
            statusLabel.setText("Agua.");
            // CPU turn after a short pause (we won't use real threading delays; just immediate)
            cpuTurn();
        }
    }

    private void cpuTurn() {
        // simple random AI that picks an untried cell
        List<Pos> avail = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) for (int j = 0; j < SIZE; j++) if (!playerCells[i][j].shot) avail.add(new Pos(i, j));
        if (avail.isEmpty()) return;
        Pos pick = avail.get(rnd.nextInt(avail.size()));
        CellFX cell = playerCells[pick.row][pick.col];
        cell.shot = true;
        if (cell.hasShip()) {
            cell.rect.setFill(Color.DARKRED);
            cell.markX();
            Ship s = cell.ship;
            s.hits.add(new Pos(pick.row, pick.col));
            statusLabel.setText("CPU impactó en " + cellCoord(pick.row, pick.col));
            if (s.isSunk()) {
                statusLabel.setText("CPU hundió tu barco: " + s.type.name);
                if (allSunk(playerShips)) {
                    statusLabel.setText("La CPU ha ganado. Todos tus barcos hundidos.");
                    endGame(false);
                    return;
                }
            }
            // CPU continues if hit
            cpuTurn();
        } else {
            cell.rect.setFill(Color.DARKBLUE);
            statusLabel.setText("CPU disparó en " + cellCoord(pick.row, pick.col) + " -> Agua.");
        }
    }

    private String cellCoord(int r, int c) {
        return "" + (char)('A' + c) + (r + 1);
    }

    private boolean allSunk(List<Ship> ships) {
        return ships.stream().allMatch(Ship::isSunk);
    }

    private void endGame(boolean playerWon) {
        placingPhase = true; // freeze further actions (we don't disable UI fully)
        shipBox.setDisable(true);
        startBtn.setDisable(false);
    }

    // --- Inner helper classes ---
    static class ShipType {
        final String name;
        final int size;
        ShipType(String n, int s) { name = n; size = s; }
    }

    static class Ship {
        final ShipType type;
        final Set<Pos> positions = new HashSet<>();
        final Set<Pos> hits = new HashSet<>();
        Ship(ShipType t) { type = t; }
        boolean isSunk() { return hits.size() >= positions.size(); }
    }

    static class Pos {
        final int row, col;
        Pos(int r, int c) { row = r; col = c; }
        @Override public boolean equals(Object o) { if (!(o instanceof Pos)) return false; Pos p=(Pos)o; return p.row==row && p.col==col; }
        @Override public int hashCode() { return Objects.hash(row,col); }
    }

    // Visual cell wrapper
    static class CellFX {
        final int row, col;
        final Rectangle rect = new Rectangle();
        Ship ship = null; // reference to ship occupying
        boolean shot = false;

        CellFX(int r, int c) { row = r; col = c; rect.setFill(Color.LIGHTBLUE); rect.setStroke(Color.GRAY); }
        boolean hasShip() { return ship != null; }
        void clear() { ship = null; shot = false; rect.setFill(Color.LIGHTBLUE); }
        void markX() {
            // Could add cross mark; simple color change already used
        }
    }
}


Notas / Sugerencias de mejora :
Separar responsabilidades en varias clases/archivos (Board, Player, AI, UI), para facilitar mantenimiento .
Mejorar la IA (estrategia hunt/target para seguir impactos) .
Agregar animaciones/sonidos y una pantalla de inicio/reinicio .

Mostrar visualmente hundimiento (marcar todas las casillas del barco hundido) .
Guardar puntuaciones locales o partida en progreso .
