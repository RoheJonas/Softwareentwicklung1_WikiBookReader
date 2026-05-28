/**
 * Aufgabe 4: Webbrowser und KeyPressed-Event
 * Autor: Jonas Rohe
 * Matrikelnr: 21511
 * Umgebung: IntelliJ
 * Erstellt: 26.12.2025
 * Letzte Änderung: 31.12.2025
 */

package aufgabe4;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * Controller für die WikiBooks-Browser-GUI von Aufgabe 4.
 * Verwaltet die Eingabe und das Laden von WikiBooks-Artikeln.
 */
public class Aufgabe4Controller {

    @FXML
    private TextField _searchField;

    @FXML
    private WebView _webView;

    private WebEngine _webEngine;

    /**
     * Wird von JavaFX automatisch nach dem Laden der FXML aufgerufen.
     * Initialisiert die WebEngine und lädt die WikiBooks-Startseite.
     */
    @FXML
    public void initialize() {
        _webEngine = _webView.getEngine();
        _webEngine.load(WikiBooks.getBaseUrl());
    }

    /**
     * Handler für den "Suchen"-Button.
     * Navigiert zu der URL des eingegebenen Buchtitels.
     *
     * @param _event ActionEvent vom Button
     */
    @FXML
    public void onSearchButtonClicked(ActionEvent _event) {
        navigateBrowser();
    }

    /**
     * Handler für die ENTER-Taste im Suchfeld.
     * Wenn ENTER gedrückt wird, wird die Suche ausgelöst.
     *
     * @param _event KeyEvent vom TextField
     */
    @FXML
    public void onSearchFieldKeyPressed(KeyEvent _event) {
        if (_event.getCode() == KeyCode.ENTER) {
            navigateBrowser();
        }
    }

    /**
     * Navigiert den Browser zur gesuchten WikiBooks-Seite.
     * Wird sowohl vom Button als auch von der ENTER-Taste aufgerufen.
     * Doppelter Code wird dadurch vermieden.
     */
    private void navigateBrowser() {
        String _titel = _searchField.getText();

        if (_titel != null && !_titel.trim().isEmpty()) {
            String _url = WikiBooks.buildURL(_titel);
            _webEngine.load(_url);
        }
    }
}
