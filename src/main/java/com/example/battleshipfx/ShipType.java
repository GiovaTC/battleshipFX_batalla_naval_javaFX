package com.example.battleshipfx;

public enum ShipType {
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
