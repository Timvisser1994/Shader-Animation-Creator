module online.timvisser {
    requires javafx.controls;
    requires javafx.fxml;
    requires pngj;

    opens online.timvisser to javafx.fxml;
    exports online.timvisser;
}