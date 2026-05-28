/**
 * Service zum Laden von Wikipedia-Artikeln via Mediawiki API
 * Autor: Jonas Rohe
 * Matrikelnr: 21511
 * Umgebung: IntelliJ
 * Erstellt: 28.12.2025
 * Letzte Änderung: 31.12.2025
 */

package aufgabe5;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class WikipediaSearchService {

    private static final String WIKIPEDIA_API = "https://de.wikipedia.org/w/api.php";

    /**
     * Sucht Wikipedia-Artikel mit Bezug zum Suchbegriff
     * @param _searchTerm Der Suchbegriff
     * @return Liste der gefundenen Artikel-Titel, alphabetisch sortiert
     * @throws MyWebException Bei Fehler beim Laden oder Parsen
     */
    public static List<String> searchWikipediaArticles(String _searchTerm) throws MyWebException {
        try {
            // URL zusammenstellen
            String urlString = WIKIPEDIA_API +
                    "?action=query&origin=*&format=json&generator=search&gsrnamespace=0&gsrlimit=10&gsrsearch=" +
                    java.net.URLEncoder.encode(_searchTerm, "UTF-8");

            URL url = new URL(urlString);

            // Verbindung herstellen
            java.net.URLConnection conn = url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            // JSON laden
            String jsonResponse = streamToString(conn.getInputStream());

            if (jsonResponse.isEmpty()) {
                throw new MyWebException("Wikipedia hat keine Antwort zurückgegeben");
            }

            // JSON parsen
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(jsonResponse);

            // Artikel-Titel extrahieren
            List<String> articles = new ArrayList<>();
            JSONObject query = (JSONObject) jsonObject.get("query");

            if (query != null && query.containsKey("pages")) {
                JSONObject pages = (JSONObject) query.get("pages");

                for (Object key : pages.keySet()) {
                    JSONObject page = (JSONObject) pages.get(key);
                    String title = (String) page.get("title");
                    if (title != null) {
                        articles.add(title);
                    }
                }
            }

            // Alphabetisch sortieren
            Collections.sort(articles);

            return articles;

        } catch (MyWebException e) {
            throw e;
        } catch (Exception e) {
            throw new MyWebException("Fehler beim Wikipedia-Zugriff: " + e.getMessage(), e);
        }
    }

    /**
     * Konvertiert InputStream zu String
     */
    private static String streamToString(InputStream _is) throws Exception {
        try (Scanner s = new Scanner(_is)) {
            s.useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        }
    }
}
