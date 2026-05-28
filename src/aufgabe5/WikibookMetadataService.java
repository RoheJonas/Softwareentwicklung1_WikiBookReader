/**
 * Service zum Laden von WikiBooks-Metadaten (Urheber, Timestamp)
 * Autor: Jonas Rohe
 * Matrikelnr: 21511
 * Umgebung: IntelliJ
 * Erstellt: 28.12.2025
 * Letzte Änderung: 31.12.2025
 */

package aufgabe5;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class WikibookMetadataService {

    private static final String EXPORT_URL = "https://de.wikibooks.org/wiki/Spezial:Exportieren/";

    /**
     * Lädt die Metadaten eines WikiBooks-Artikels
     * @param _titel Der Buchtitel
     * @return WikiBook-Objekt mit Urheber und Timestamp
     * @throws MyWebException Bei Fehler beim Laden oder Parsen
     */
    public static WikiBook getMetadata(String _titel) throws MyWebException {
        try {
            String urlString = EXPORT_URL + URLEncoder.encode(_titel, "UTF-8");
            URL url = new URL(urlString);

            URLConnection conn = url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            // XML laden
            StringBuilder xmlContent = new StringBuilder();
            try (InputStream is = conn.getInputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    xmlContent.append(new String(buffer, 0, bytesRead, "UTF-8"));
                }
            }

            String xmlString = xmlContent.toString();

            // Prüfe ob XML gültig ist
            if (xmlString.isEmpty()) {
                throw new MyWebException("Server hat leere Antwort zurückgegeben");
            }

            // SAX-Parsing
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(false);
            factory.setFeature("http://xml.org/sax/features/namespaces", false);
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

            SAXParser parser = factory.newSAXParser();
            WikibookContributorHandler handler = new WikibookContributorHandler();

            java.io.InputStream parseStream = new java.io.ByteArrayInputStream(xmlString.getBytes("UTF-8"));
            parser.parse(parseStream, handler);

            // WikiBook-Objekt erstellen
            WikiBook book = new WikiBook(_titel);
            book.setUrheber(handler.getUrheber());
            book.setTimestampLokal(formatTimestamp(handler.getTimestamp()));

            return book;

        } catch (MyWebException e) {
            throw e;
        } catch (Exception e) {
            throw new MyWebException("Fehler beim Laden von WikiBooks: " + e.getMessage(), e);
        }
    }

    /**
     * Konvertiert ISO 8601 UTC-Timestamp in lokale Zeit
     * @param _iso Der Timestamp im ISO 8601 Format (UTC)
     * @return Der Timestamp in lokaler Zeit
     */
    private static String formatTimestamp(String _iso) {
        try {
            if (_iso == null || _iso.equals("Unbekannt") || _iso.isEmpty()) {
                return "Unbekannt";
            }

            SimpleDateFormat iso = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            iso.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = iso.parse(_iso);

            SimpleDateFormat local = new SimpleDateFormat("dd.MM.yyyy 'um' HH:mm 'Uhr'");
            return local.format(date);
        } catch (Exception e) {
            return _iso;
        }
    }
}
