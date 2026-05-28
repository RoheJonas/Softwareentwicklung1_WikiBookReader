/**
 * Aufgabe 4: Startklasse
 * Autor: Jonas Rohe
 * Matrikelnr: 21511
 * Umgebung: IntelliJ
 * Erstellt: 31.12.2025
 * Letzte Änderung: 26.12.2025
 */

package aufgabe4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Startet die JavaFX-Anwendung für Aufgabe 4 (WikiBooks-Browser).
 */
public class Aufgabe4Main extends Application {

    @Override
    public void start(Stage _primaryStage) throws Exception {
        FXMLLoader _loader = new FXMLLoader(getClass().getResource("aufgabe4.fxml"));

        Scene _scene = new Scene(_loader.load(), 800, 600);

        _primaryStage.setTitle("Aufgabe 4 - WikiBooks Browser");
        _primaryStage.setScene(_scene);
        _primaryStage.show();
    }

    public static void main(String[] _args) {
        launch(_args);
    }
}
