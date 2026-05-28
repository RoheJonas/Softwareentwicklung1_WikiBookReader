/**
 * Einstiegspunkt für das JavaFX-Tutorial Beispiel.
 *
 * Diese Klasse lädt die FXML-Datei und startet die Anwendung.
 *
 * @author Jonas Rohe
 * @version 1.0
 * @since 18.12.2025
 */

package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Hauptanwendungsklasse für das Tutorial-Beispiel.
 * Erweitert Application, was sie zu einer JavaFX-Anwendung macht.
 */
public class SampleMain extends Application {

    /**
     * Startet die Anwendung und setzt die GUI auf.
     *
     * @param _primaryStage Die primäre Bühne (Fenster) der Anwendung
     * @throws Exception Falls das Laden der FXML-Datei fehlschlägt
     */
    @Override
    public void start(Stage _primaryStage) throws Exception {
        FXMLLoader _fxmlLoader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent _root = _fxmlLoader.load();

        Scene _scene = new Scene(_root, 400, 300);

        _primaryStage.setTitle("JavaFX Tutorial - Hello World");
        _primaryStage.setScene(_scene);
        _primaryStage.show();
    }

    /**
     * Einstiegspunkt der Anwendung.
     *
     * @param _args Befehlszeilenargumente
     */
    public static void main(String[] _args) {
        launch(_args);
    }
}
