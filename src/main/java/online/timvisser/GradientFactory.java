package online.timvisser;

import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

/**
 * Used to build LinearGradients that correspond with the
 * colors that the user can select
 */
public class GradientFactory {

    public static LinearGradient buildX() {
        var stops = new Stop[] {
            new Stop(0.0, new Color(0, 0, 0, 0)),
            new Stop(1.0, Color.RED)
        };
        return new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);
    }

    public static LinearGradient buildY() {
        var stops = new Stop[] {
            new Stop(0.0, new Color(0, 0, 0, 0)),
            new Stop(1.0, Color.GREEN)
        };
        return new LinearGradient(0, 1, 1, 0, true, CycleMethod.NO_CYCLE, stops);
    }
}
