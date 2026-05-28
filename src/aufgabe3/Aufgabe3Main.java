/**
 * Aufgabe 3: Startklasse
 * Autor: Jonas Rohe
 * Matrikelnr: 21511
 * Umgebung: IntelliJ
 * Erstellt: 22.12.2025
 * Letzte Änderung: 31.12.2025
 */

package aufgabe3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Startet die JavaFX-Anwendung für Aufgabe 3.
 */
public class Aufgabe3Main extends Application {

    @Override
    public void start(Stage _primaryStage) throws Exception {
        FXMLLoader _loader = new FXMLLoader(getClass().getResource("aufgabe3.fxml"));

        Scene _scene = new Scene(_loader.load(), 500, 400);

        _primaryStage.setTitle("Aufgabe 3 - Listenansicht");
        _primaryStage.setScene(_scene);
        _primaryStage.show();
    }

    public static void main(String[] _args) {
        launch(_args);
    }
}
