/**
 * Aufgabe 1: Event-Bearbeitung
 *
 * Programm zur Behandlung von Tastatur-Events. Das Programm registriert
 * KeyEvent-Handler auf zwei Eingabefelder und reagiert auf die Funktionstasten
 * F1, F2 und F3. Die gedrückte Taste wird im zweiten Eingabefeld ausgegeben.
 *
 * @author Jonas Rohe
 * @version 1.0
 * @since 18.12.2025
 */

package application;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class Main extends Application {

    /**
     * Startet die JavaFX-Anwendung und setzt die grafische Benutzeroberfläche auf.
     *
     * @param _primaryStage Die primäre Bühne (Fenster) der Anwendung
     */
    @Override
    public void start(Stage _primaryStage) {
        try {
            GridPane _root = new GridPane();
            Scene _scene = new Scene(_root, 400, 400);

            TextField _textField1 = new TextField();
            _textField1.setPromptText("Schreibe hier");

            TextField _textField2 = new TextField();
            _textField2.setPromptText("Schreibe hier");

            /**
             * Event-Handler für KeyEvent-Ereignisse. Überprüft, welche Taste
             * gedrückt wurde und schreibt die entsprechende Meldung in
             * textField2. F1, F2 und F3 werden erkannt, alle anderen Tasten
             * werden ignoriert.
             */
            final EventHandler<KeyEvent> _keyEventHandler =
                    new EventHandler<KeyEvent>() {
                        @Override
                        public void handle(final KeyEvent _keyEvent) {
                            KeyCode _code = _keyEvent.getCode();

                            if (_code == KeyCode.F1) {
                                _textField2.setText("F1 gedrückt");
                            } else if (_code == KeyCode.F2) {
                                _textField2.setText("F2 gedrückt");
                            } else if (_code == KeyCode.F3) {
                                _textField2.setText("F3 gedrückt");
                            }

                            _keyEvent.consume();
                        }
                    };

            _textField1.setOnKeyPressed(_keyEventHandler);
            _textField2.setOnKeyPressed(_keyEventHandler);

            _root.setConstraints(_textField1, 0, 0);
            _root.setConstraints(_textField2, 0, 1);
            _root.getChildren().addAll(_textField1, _textField2);

            _primaryStage.setScene(_scene);
            _primaryStage.show();
        } catch(Exception _e) {
            _e.printStackTrace();
        }
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
