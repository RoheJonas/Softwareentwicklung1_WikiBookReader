/**
 * Main-Anwendungsklasse für Aufgabe 5 WikiBooks Browser
 * Mit Layout-Optimierung (Aufgabe 11)
 * Autor: Jonas Rohe
 * Matrikelnr: 21511
 * Umgebung: IntelliJ
 * Erstellt: 26.12.2025
 * Letzte Änderung: 31.12.2025
 */

package aufgabe5;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.InputStream;

public class Aufgabe5Main extends Application {

    @Override
    public void start(Stage _primaryStage) throws Exception {
        // FXML laden
        FXMLLoader loader = new FXMLLoader(getClass().getResource("aufgabe5.fxml"));
        Parent root = loader.load();

        // Scene mit mindestgröße erstellen
        Scene scene = new Scene(root, 1200, 700); // Standard-Größe

        _primaryStage.setScene(scene);

        // --- Aufgabe 11: Titel und Icon ---
        _primaryStage.setTitle("Mein WikiBooks-Browser");

        // Icon laden (falls vorhanden - sonst kommentieren)
        try {
            InputStream iconStream = getClass().getResourceAsStream("wikibooks_icon.png");
            if (iconStream != null) {
                _primaryStage.getIcons().add(new Image(iconStream));
            }
        } catch (Exception e) {
            System.err.println("Icon konnte nicht geladen werden: " + e.getMessage());
        }

        // --- Aufgabe 11: Mindestgröße setzen ---
        _primaryStage.setMinWidth(1000);
        _primaryStage.setMinHeight(600);

        // Optional: Auf aktuellem Screen zentrieren
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        _primaryStage.setX((screenBounds.getWidth() - 1200) / 2);
        _primaryStage.setY((screenBounds.getHeight() - 700) / 2);

        _primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
