/**
 * Hilfsklasse für WikiBooks-URLs
 * Kapselt das Wissen über das URL-Format
 * Autor: Jonas Rohe
 * Matrikelnr: 21511
 * Umgebung: IntelliJ
 * Erstellt: 26.12.2025
 * Letzte Änderung: 31.12.2025
 */

package aufgabe4;

/**
 * Verwaltet WikiBooks-URLs und URL-Generierung
 */
public class WikiBooks {

    private static final String BASE_URL = "http://de.wikibooks.org/wiki/";

    /**
     * Gibt die Basis-URL von WikiBooks zurück
     *
     * @return Die Base-URL als String
     */
    public static String getBaseUrl() {
        return BASE_URL;
    }

    /**
     * Erstellt die komplette URL für einen WikiBooks-Titel
     * Ersetzt Leerzeichen durch Unterstriche (nach WikiBooks-Konvention)
     *
     * @param _titel Der Buchtitel (z.B. "Java Standard")
     * @return Die komplette URL (z.B. "http://de.wikibooks.org/wiki/Java_Standard")
     */
    public static String buildURL(String _titel) {
        if (_titel == null || _titel.trim().isEmpty()) {
            return BASE_URL;
        }

        String _bereinigterTitel = _titel.trim().replace(" ", "_");
        return BASE_URL + _bereinigterTitel;
    }
}
