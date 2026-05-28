/**
 * Custom Exception für Fehler beim Laden von WikiBooks-Metadaten
 * Autor: Jonas Rohe
 * Matrikelnr: 21511
 * Umgebung: IntelliJ
 * Erstellt: 28.12.2025
 * Letzte Änderung: 31.12.2025
 */

package aufgabe5;

public class MyWebException extends Exception {

    public MyWebException(String _message) {
        super(_message);
    }

    public MyWebException(String _message, Throwable _cause) {
        super(_message, _cause);
    }
}
