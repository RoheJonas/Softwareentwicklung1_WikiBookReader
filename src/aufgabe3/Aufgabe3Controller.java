/**
 * Aufgabe 3: Meine erste GUI (Listen Ansicht)
 * Autor: Jonas Rohe
 * Matrikelnr: 21511
 * Umgebung: IntelliJ
 * Erstellt: 22.12.2025
 * Letzte Änderung: 31.12.2025
 */

package aufgabe3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * Controller für die GUI von Aufgabe 3.
 * Verwaltet eine Liste von eingegebenen Begriffen.
 */
public class Aufgabe3Controller {

    @FXML
    private TextField _inputField;

    @FXML
    private ListView<String> _listView;

    private final ObservableList<String> _items = FXCollections.observableArrayList();

    /**
     * Wird von JavaFX automatisch nach dem Laden der FXML aufgerufen.
     * Hier wird die Datenliste mit der ListView verbunden.
     */
    @FXML
    public void initialize() {
        _listView.setItems(_items);
    }

    /**
     * Fügt den aktuellen Text aus dem Eingabefeld zur Liste hinzu.
     *
     * @param _event ActionEvent vom Button
     */
    @FXML
    public void onAddButtonClicked(ActionEvent _event) {
        String _text = _inputField.getText();

        if (_text != null) {
            _text = _text.trim();
        }

        if (_text != null && !_text.isEmpty()) {
            _items.add(_text);
            _inputField.clear();
        }
    }

    /**
     * Löscht alle Elemente aus der Liste.
     *
     * @param _event ActionEvent vom Button
     */
    @FXML
    public void onResetButtonClicked(ActionEvent _event) {
        _items.clear();
    }
}
