/**
 * Controller für das JavaFX-Tutorial Beispiel.
 *
 * Diese Klasse verwaltet die Benutzerinteraktionen mit der GUI,
 * die in der sample.fxml Datei definiert ist.
 *
 * @author Jonas Rohe
 * @version 1.0
 * @since 18.12.2025
 */

package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controller-Klasse für sample.fxml.
 * Verwaltet die Interaktion zwischen GUI und Geschäftslogik.
 */
public class SampleController {

    /**
     * Label-Element aus der FXML-Datei.
     * Die Annotation @FXML verbindet diese Variable mit dem
     * Label-Element, das in der FXML mit fx:id="helloWorld" definiert ist.
     */
    @FXML
    private Label _helloWorld;

    /**
     * Event-Handler für den "Say 'Hello World'" Button.
     *
     * Diese Methode wird aufgerufen, wenn der Button in der GUI
     * angeklickt wird (definiert durch onAction="#sayHelloWorld" in der FXML).
     *
     * @param _actionEvent Das ActionEvent, das von JavaFX weitergeleitet wird
     */
    @FXML
    public void sayHelloWorld(ActionEvent _actionEvent) {
        _helloWorld.setText("Hello World!");
    }
}
