package sample;

import javafx.scene.paint.Color;

/**
 * Created by PiratePowWow on 3/30/16.
 */
public class Ant {
    double x;
    double y;
    Color color = Color.BLACK;

    public Ant(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Ant(double x, double y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }
}
