package online.timvisser;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import online.timvisser.interfaces.FXMLController;

import java.io.IOException;

public class PrimaryController implements FXMLController {

    @FXML
    private Canvas canvas;
    private GraphicsContext context;

    @FXML
    private Canvas preview;

    private Point2D center;

    @Override
    public void initialize() {

        canvas.setWidth(400);
        canvas.setHeight(200);
        center = new Point2D(canvas.getWidth() / 2, canvas.getHeight() / 2);
        context = canvas.getGraphicsContext2D();

        this.resetCanvas();

        canvas.setOnMouseClicked(mouseEvent -> {
            var target = new Point2D(mouseEvent.getX(), mouseEvent.getY());

            var color = this.pickColor(mouseEvent.getX(), mouseEvent.getY());
            preview.getGraphicsContext2D().setFill(color);
            preview.getGraphicsContext2D().fillRect(0, 0, 20, 20);

            this.resetCanvas();

            System.out.println(color);

            context.strokeLine(center.getX(), center.getY(), target.getX(), target.getY());
        });
    }

    /**
     * Resets the canvas with the correct background gradient
     */
    private void resetCanvas() {
        var canvasWidth = canvas.getWidth();
        var canvasHeight = canvas.getHeight();
        var centerX = center.getX();
        var centerY = center.getY();

        // Clear the canvas of everything on it
        context.clearRect(0, 0, canvasWidth, canvasHeight);

        context.setGlobalAlpha(0.5);

        // Set our stroke to the appropriate gradient
        // and paint it from left to right over our canvas
        context.setStroke(GradientFactory.buildX());
        context.setLineWidth(canvasHeight);
        context.strokeLine(0, centerY, canvasWidth, centerY);
        context.stroke();

        // and again, but this time from the bottom to the top
        context.setStroke(GradientFactory.buildY());
        context.setLineWidth(canvasWidth);
        context.strokeLine(centerX, canvasHeight, centerX, 0);
        context.stroke();

        // Reset our dirty stroke settings
        context.setStroke(Color.BLACK);
        context.setLineWidth(1);
        context.setGlobalAlpha(1);
    }

    private Color pickColor(Point2D point) {
        return this.pickColor(point.getX(), point.getY());
    }

    /**
     * Maps a canvas position to the corresponding Color
     * @param x The x-position (from left to right)
     * @param y The y-position (from top to bottom
     * @return The Color at that position
     */
    private Color pickColor(double x, double y) {
        // 0 ---------> canvas width
        // maps to
        // 0 ---------> 1
        var horizontal = x / canvas.getWidth();

        // 0 ---------> canvas height
        // maps to
        // 0 ---------> 1
        var vertical = 1 - (y / canvas.getHeight());

        return new Color(horizontal, vertical, 0, 1);
    }

    @FXML
    private void switchToSecondary() throws IOException {
        //App.setRoot("secondary");

        System.out.println(canvas);
//        System.out.println(rect);
    }
}
