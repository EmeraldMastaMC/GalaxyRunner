package org.firstinspires.ftc.team26923.GalaxyRunner.Utils;

public enum Direction {
    FORWARD(1),
    BACKWARD(-1),
    RIGHT(1),
    LEFT(-1),
    CLOCKWISE(-1),
    COUNTERCLOCKWISE(1);

    private final int multiplier;

    Direction(int multiplier) {
        this.multiplier = multiplier;
    }

    public int getMultiplier() {
        return multiplier;
    }

}
