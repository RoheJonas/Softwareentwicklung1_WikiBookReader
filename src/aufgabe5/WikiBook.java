/**
 * Datenklasse für ein WikiBooks-Buch
 * Autor: Jonas Rohe
 * Matrikelnr: 21511
 * Umgebung: IntelliJ
 * Erstellt: 28.12.2025
 * Letzte Änderung: 31.12.2025
 */

package aufgabe5;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WikiBook implements Serializable {
    private static final long serialVersionUID = 1L;

    private String titel;
    private String urheber;
    private String timestampLokal;
    private List<String> regale = new ArrayList<>();

    public WikiBook(String _titel) {
        this.titel = _titel;
        this.urheber = "Unbekannt";
        this.timestampLokal = "Unbekannt";
    }

    public String getTitel() { return titel; }

    public String getUrheber() { return urheber; }
    public void setUrheber(String _urheber) { this.urheber = _urheber; }

    public String getTimestampLokal() { return timestampLokal; }
    public void setTimestampLokal(String _ts) { this.timestampLokal = _ts; }

    public void addRegal(String _regal) {
        if (!this.regale.contains(_regal)) {
            this.regale.add(_regal);
        }
    }
    public List<String> getRegale() { return regale; }

    @Override
    public String toString() {
        return titel + " (von " + urheber + ", " + timestampLokal + ")";
    }
}
