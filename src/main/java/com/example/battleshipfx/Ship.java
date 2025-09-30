package com.example.battleshipfx;

import java.util.HashSet;
import java.util.Set;

public class Ship {
    final ShipType type;
    final Set<CellFX> positions = new HashSet<>();
    final Set<CellFX> hits = new HashSet<>();

    public Ship(ShipType type) {
        this.type = type;
    }

    public ShipType getType() {
        return type;
    }

    public void addPosition(CellFX cell) {
        positions.add(cell);
        cell.ship = this;
    }

    public boolean isSunk() {
        return hits.size() >= positions.size();
    }

    public void registerHit(CellFX cell) {
        if (positions.contains(cell)) {
            hits.add(cell);
        }
    }
}
