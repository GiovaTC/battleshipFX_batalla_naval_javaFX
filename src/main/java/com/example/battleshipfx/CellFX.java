package com.example.battleshipfx;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

// Visual cell wrapper
public class CellFX {
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
