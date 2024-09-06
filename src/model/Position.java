package model;

import java.awt.*;

public record Position (Point point, Color color) {
    public int getX() {
        return point.x;
    }

    public int getY() {
        return point.y;
    }
}
