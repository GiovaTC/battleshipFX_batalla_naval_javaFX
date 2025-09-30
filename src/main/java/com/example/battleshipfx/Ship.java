package com.example.battleshipfx;

import javafx.geometry.Pos;

import java.util.HashSet;
import java.util.Set;

public class Ship {
    final ShipType type;
    final Set<Pos> positions = new HashSet<>();
    final Set<Pos> hits = new HashSet<>();
    Ship(ShipType t) { type = t; }
    boolean isSunk() { return hits.size() >= positions.size(); }
}