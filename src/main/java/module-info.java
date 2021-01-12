module online.timvisser {
    requires javafx.controls;
    requires javafx.fxml;

    opens online.timvisser to javafx.fxml;
    exports online.timvisser;
}