/**
 * SAX ContentHandler zum Parsen von WikiBooks Export-XML
 * Autor: Jonas Rohe
 * Matrikelnr: 21511
 * Umgebung: IntelliJ
 * Erstellt: 28.12.2025
 * Letzte Änderung: 31.12.2025
 */

package aufgabe5;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class WikibookContributorHandler extends DefaultHandler {
    private String urheber = "Unbekannt";
    private String timestamp = "Unbekannt";
    private StringBuilder currentValue = new StringBuilder();

    private boolean inRevision = false;
    private boolean inContributor = false;
    private boolean foundFirst = false;

    public String getUrheber() {
        return urheber;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        currentValue.setLength(0);

        String tagName = !localName.isEmpty() ? localName : qName;
        tagName = tagName.toLowerCase();

        if (tagName.equals("revision") && !foundFirst) {
            inRevision = true;
            foundFirst = true;
        }

        if (inRevision && tagName.equals("contributor")) {
            inContributor = true;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        currentValue.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        String tagName = !localName.isEmpty() ? localName : qName;
        tagName = tagName.toLowerCase();
        String value = currentValue.toString().trim();

        if (inContributor) {
            if (tagName.equals("username")) {
                urheber = value;
            } else if (tagName.equals("ip")) {
                if (urheber.equals("Unbekannt")) {
                    urheber = value;
                }
            } else if (tagName.equals("contributor")) {
                inContributor = false;
            }
        }

        if (inRevision && tagName.equals("timestamp")) {
            timestamp = value;
        }

        if (tagName.equals("revision")) {
            inRevision = false;
        }
    }
}
