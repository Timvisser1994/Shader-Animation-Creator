package online.timvisser.models;

import javafx.scene.paint.Color;
import online.timvisser.enums.Axis;

import java.util.HashMap;

/**
 * Maps the R, G and B color channels to X and Y axis in some combination
 */
public class RGBAxisMapping {

    private static HashMap<Color, Axis> mapping = new HashMap<>();

    public static void associateColor(Color color, Axis axis) {
        var previousAxis = mapping.get(color);

        if (previousAxis != null) {
            System.out.println(previousAxis);
            // "Push" the value (X->Y and Y->undefined)
            if (previousAxis == Axis.X) mapping.put(color, Axis.Y);
            else if (previousAxis == Axis.Y) mapping.put(color, Axis.Undefined);
        }

        mapping.put(color, axis);
    }

    public static Color getXColor() {
        var keys = mapping.keySet();

        for (var key : keys) {
            if (mapping.get(key).equals(Axis.X)) {
                return key;
            }
        }

        // Defaults to R
        return Color.RED;
    }

    public static Color getYColor() {
        var keys = mapping.keySet();

        for (var key : keys) {
            if (mapping.get(key).equals(Axis.Y)) {
                return key;
            }
        }

        // Defaults to G
        return Color.GREEN;
    }
}
