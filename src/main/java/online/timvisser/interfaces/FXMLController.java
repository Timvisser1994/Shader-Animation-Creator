package online.timvisser.interfaces;

import javafx.fxml.FXML;

/**
 * Generic interface for all FXML Controllers
 */
public interface FXMLController {

    /**
     * All FXML Controllers hook into the JavaFX lifecycle,
     * and with it, this method becomes available and is called
     * right after all FXML elements are initialized.
     */
    @FXML
    void initialize();
}
