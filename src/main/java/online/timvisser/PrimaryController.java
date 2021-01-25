package online.timvisser;

import ar.com.hjg.pngj.ImageInfo;
import ar.com.hjg.pngj.ImageLineHelper;
import ar.com.hjg.pngj.ImageLineInt;
import ar.com.hjg.pngj.PngWriter;
import ar.com.hjg.pngj.chunks.ChunkFactory;
import ar.com.hjg.pngj.chunks.PngChunk;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import online.timvisser.enums.Axis;
import online.timvisser.interfaces.FXMLController;
import online.timvisser.models.RGBAxisMapping;

import java.io.File;
import java.io.IOException;

public class PrimaryController implements FXMLController {

    @FXML
    private Canvas canvas;
    private GraphicsContext context;

    @FXML
    private Canvas preview;

    @FXML
    private TextField r, g, b;

    private Point2D center;
    private Color selectedColor;

    private boolean cursorOnCanvas;

    @Override
    public void initialize() {

        //debug
        RGBAxisMapping.associateColor(Color.BLUE, Axis.X);
        RGBAxisMapping.associateColor(Color.RED, Axis.Y);

        canvas.setWidth(400);
        canvas.setHeight(200);
        center = new Point2D(canvas.getWidth() / 2, canvas.getHeight() / 2);
        context = canvas.getGraphicsContext2D();

        this.resetCanvas();

        // Hook up all the plumbing around mouse events on our canvas
        canvas.setOnMouseEntered(__ -> cursorOnCanvas = true);
        canvas.setOnMouseExited(__ -> cursorOnCanvas = false);
        canvas.setOnMouseDragged(mouseEvent -> { if (cursorOnCanvas) renderVector(mouseEvent); });
        canvas.setOnMouseClicked(mouseEvent -> { if (cursorOnCanvas) renderVector(mouseEvent); });
    }

    private void renderVector(MouseEvent mouseEvent) {
        var target = new Point2D(mouseEvent.getX(), mouseEvent.getY());

        var color = this.pickColor(mouseEvent.getX(), mouseEvent.getY());
        preview.getGraphicsContext2D().setFill(color);
        preview.getGraphicsContext2D().fillRect(0, 0, 20, 20);

        // Update the RGB values
        r.setText((int) (color.getRed() * 255) + "");
        g.setText((int) (color.getGreen() * 255) + "");
        b.setText((int) (color.getBlue() * 255) + "");

        this.resetCanvas();

        System.out.println(color);

        context.strokeLine(center.getX(), center.getY(), target.getX(), target.getY());
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
     * @param y The y-position (from top to bottom)
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

        selectedColor = new Color(vertical, 0, horizontal, 1);
        return selectedColor;
    }

    @FXML
    private void writeToPNG() throws IOException {
        //App.setRoot("secondary");

        System.out.println(canvas);

        if (selectedColor != null) {
            var file = new File("./shader-animation.png");
            var imageInfo = new ImageInfo(100, 100, 8, false);
            var imageLine = new ImageLineInt(imageInfo);
            var tim = new PngWriter(file, imageInfo);

            var red = (int) (selectedColor.getRed() * 255);
            var green = (int) (selectedColor.getGreen() * 255);
            var blue = (int) (selectedColor.getBlue() * 255);

            for (int col = 0; col < imageInfo.cols; col++) {
                ImageLineHelper.setPixelRGB8(imageLine, col, red, green, blue);
            }

            for (int row = 0; row < imageInfo.rows; row++) {
                tim.writeRow(imageLine);
            }

            tim.end();
            tim.close();
        }
    }
}
