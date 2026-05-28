# WikiBooks Browser

JavaFX-Desktop-Anwendung zum Recherchieren und Anzeigen von Inhalten aus dem Wikimedia-Umfeld. Das Projekt wurde im Rahmen der Lehrveranstaltung **Softwareentwicklung 1** an der Hochschule Stralsund entwickelt und umfasst mehrere aufeinander aufbauende Aufgaben – von einfacher Event-Behandlung bis hin zu vollständiger GUI-Anwendung mit Web-API-Anbindung, Suchverlauf und lokaler Persistenz.

---

## Projektidee

Der WikiBooks Browser ermöglicht es, Artikel aus **Wikibooks** und **Wikipedia** direkt in einer JavaFX-Anwendung zu suchen, anzuzeigen und zu verwalten. Gesuche werden in einem Verlauf gespeichert, und interessante Artikel lassen sich in einem persönlichen Zettelkasten ablegen – der dann lokal auf der Festplatte gespeichert und wieder geladen werden kann.

---

## Features

- Suche nach Wikibooks- und Wikipedia-Artikeln
- Anzeige von Seiteninhalten direkt in der Anwendung per eingebettetem Browser (WebView)
- Suchverlauf via ComboBox (Vor- und Zurück-Navigation)
- Persoenlicher Zettelkasten zum Speichern interessanter Buecher
- Lokale Persistenz: Speichern und Laden der Buecher-Sammlung via Serialisierung
- Info-Dialog mit Lizenzhinweisen (F1-Shortcut)
- Tastatur-Navigation (Tab-Reihenfolge, F1-Shortcut)
- Abruf von Metadaten (Urheber, Timestamp) ueber die MediaWiki Export-API

---

## Aufgaben und Entwicklungsschritte

Das Projekt ist in mehrere Aufgaben unterteilt, die schrittweise aufeinander aufbauen:

### Aufgabe 1 – Event-Behandlung (`src/application/`)

**Aufgabenstellung:** Einstieg in JavaFX-Events. Zwei Eingabefelder sollen auf Tastendruecke reagieren, insbesondere auf die Funktionstasten F1, F2 und F3.

**Umsetzung:**  
Die Klasse `Main.java` (Package `application`) registriert einen gemeinsamen `EventHandler<KeyEvent>` auf zwei `TextField`-Instanzen. Wird F1, F2 oder F3 gedrueckt, erscheint die entsprechende Meldung im zweiten Eingabefeld. Das Event wird anschliessend mit `_keyEvent.consume()` verbraucht, um eine Weitergabe zu verhindern.

**Beteiligte Dateien:**
| Datei | Beschreibung |
|---|---|
| `src/application/Main.java` | Einstiegspunkt; registriert KeyEvent-Handler auf zwei TextFields |

---

### Aufgabe 2 – JavaFX Tutorial-Beispiel (`src/sample/`)

**Aufgabenstellung:** Einfuehrendes Beispiel zur JavaFX-Struktur mit FXML und Controller-Klasse.

**Umsetzung:**  
Das Sample-Package enthaelt eine minimale JavaFX-Anwendung mit MVC-Struktur: `SampleMain.java` startet die Anwendung und laedt das Layout aus `sample.fxml`. `SampleController.java` verwaltet Benutzerinteraktionen mit der GUI, wie sie in der FXML-Datei definiert sind.

**Beteiligte Dateien:**
| Datei | Beschreibung |
|---|---|
| `src/sample/SampleMain.java` | Startklasse der Sample-Anwendung |
| `src/sample/SampleController.java` | Controller fuer GUI-Interaktionen |
| `src/sample/sample.fxml` | FXML-Layout der Benutzeroberflaeche |

---

### Aufgabe 3 – Meine erste GUI / Listen-Ansicht (`src/aufgabe3/`)

**Aufgabenstellung:** Erste eigene JavaFX-GUI mit FXML und Controller. Ziel: eine dynamische Liste, in der Eintraege hinzugefuegt und entfernt werden koennen.

**Umsetzung:**  
`Aufgabe3Controller.java` verwaltet eine `ObservableList<String>`, die per `@FXML`-Injektion an eine `ListView` gebunden ist. Ueber ein `TextField` und zwei Buttons koennen Eintraege hinzugefuegt (`onAddButtonClicked`) oder die gesamte Liste geleert werden (`onResetButtonClicked`). Das Layout ist in `aufgabe3.fxml` definiert (VBox mit HBox, TextField, Buttons und ListView).

**Beteiligte Dateien:**
| Datei | Beschreibung |
|---|---|
| `src/aufgabe3/Aufgabe3Main.java` | Startklasse; laedt das FXML-Layout |
| `src/aufgabe3/Aufgabe3Controller.java` | Verwaltet ObservableList, Add- und Reset-Logik |
| `src/aufgabe3/aufgabe3.fxml` | GUI-Layout mit VBox, ListView, TextField und Buttons |

---

### Aufgabe 4 – Webbrowser & KeyPressed-Event (`src/aufgabe4/`)

**Aufgabenstellung:** Integration eines eingebetteten Webbrowsers in JavaFX. Die Anwendung soll per Eingabefeld und Enter-Taste Wikibooks-Seiten laden.

**Umsetzung:**  
`Aufgabe4Controller.java` steuert einen `WebView` mit seiner `WebEngine`. Ein Suchfeld nimmt den Buchtitel entgegen; bei Enter-Druck (`KeyCode.ENTER`) wird `navigateBrowser()` aufgerufen. Die Hilfsklasse `WikiBooks.java` kapselt die URL-Logik: `buildURL(String _titel)` bereinigt den Titel (Leerzeichen → Unterstriche) und haengt ihn an die Basis-URL `http://de.wikibooks.org/wiki/` an.

**Beteiligte Dateien:**
| Datei | Beschreibung |
|---|---|
| `src/aufgabe4/Aufgabe4Main.java` | Startklasse |
| `src/aufgabe4/Aufgabe4Controller.java` | WebView-Steuerung, KeyEvent-Handler fuer Enter |
| `src/aufgabe4/WikiBooks.java` | Hilfsklasse zur URL-Generierung fuer Wikibooks |
| `src/aufgabe4/aufgabe4.fxml` | FXML-Layout mit WebView und Suchfeld |

---

### Aufgabe 5 – Vollstaendige WikiBooks-Browser-Anwendung (`src/aufgabe5/`)

**Aufgabenstellung:** Aufbauend auf Aufgabe 4, eine vollstaendige Anwendung mit Suchverlauf (Aufgabe 8), ComboBox-Verlauf (Aufgabe 9), Menue mit Info-Dialog (Aufgabe 10), Zettelkasten, Persistenz und Tastatur-Shortcuts (Aufgabe 12).

**Umsetzung:**  
Der `Aufgabe5Controller.java` (568 Zeilen) ist das Herzstuck der Anwendung und buendelt alle Features:

- **Suche:** `performSearch()` ruft den `WikibookMetadataService` auf, um Metadaten zu laden, und navigiert den `WebView` zur entsprechenden URL.
- **Suchverlauf:** Eine `List<String> _history` mit `_historyIndex` ermoeglicht Vor-/Zurueck-Navigation; die `ComboBox` wird bei jeder Suche aktualisiert.
- **Wikipedia-Suche:** Der `WikipediaSearchService` fragt die MediaWiki-API (`de.wikipedia.org/w/api.php`) per HTTP-GET ab und parst das JSON-Ergebnis mit `org.json.simple.JSONParser`.
- **Zettelkasten:** Artikel werden als `WikiBook`-Objekte in einer `ListView` angezeigt und koennen hinzugefuegt werden.
- **Persistenz:** `onSpeichernClicked` serialisiert die Buecher-Liste per `ObjectOutputStream` in `wikibooks_sammlung.dat`; `onLadenClicked` laedt sie per `ObjectInputStream` wieder.
- **Menue & Info-Dialog:** `onAboutClicked` zeigt einen `Alert`-Dialog mit Lizenzhinweisen zu Wikibooks/Wikipedia.
- **Shortcuts:** `setupGlobalKeyHandlers()` oeffnet den Info-Dialog bei F1-Druck.
- **Tab-Reihenfolge:** `setupTabTraversal()` stellt sicher, dass der Fokus sinnvoll durch die UI-Elemente wandert (WebView wird aus Tab-Traversal ausgeschlossen).

**Beteiligte Dateien:**
| Datei | Beschreibung |
|---|---|
| `src/aufgabe5/Aufgabe5Main.java` | Startklasse |
| `src/aufgabe5/Aufgabe5Controller.java` | Haupt-Controller mit allen Features (568 Zeilen) |
| `src/aufgabe5/WikiBook.java` | Modellklasse fuer ein WikiBook-Objekt (Serializable) |
| `src/aufgabe5/WikibookMetadataService.java` | Laedt Metadaten via MediaWiki Export-API (SAX-Parser) |
| `src/aufgabe5/WikibookContributorHandler.java` | SAX ContentHandler zum Parsen des Wikibooks-Export-XML |
| `src/aufgabe5/WikipediaSearchService.java` | Sucht Wikipedia-Artikel via MediaWiki API (JSON) |
| `src/aufgabe5/MyWebException.java` | Custom Exception fuer Fehler beim Laden von Metadaten |
| `src/aufgabe5/aufgabe5.fxml` | FXML-Layout der Hauptanwendung |

---

## Projektstruktur

```
Softwareentwicklung1_WikiBookReader/
├── src/
│   ├── application/          # Aufgabe 1: KeyEvent-Handler
│   │   └── Main.java
│   ├── sample/               # Aufgabe 2: JavaFX Tutorial-Beispiel
│   │   ├── SampleMain.java
│   │   ├── SampleController.java
│   │   └── sample.fxml
│   ├── aufgabe3/             # Aufgabe 3: Liste mit ObservableList
│   │   ├── Aufgabe3Main.java
│   │   ├── Aufgabe3Controller.java
│   │   └── aufgabe3.fxml
│   ├── aufgabe4/             # Aufgabe 4: WebView + URL-Generierung
│   │   ├── Aufgabe4Main.java
│   │   ├── Aufgabe4Controller.java
│   │   ├── WikiBooks.java
│   │   └── aufgabe4.fxml
│   ├── aufgabe5/             # Aufgabe 5: Vollstaendige Anwendung
│   │   ├── Aufgabe5Main.java
│   │   ├── Aufgabe5Controller.java
│   │   ├── WikiBook.java
│   │   ├── WikibookMetadataService.java
│   │   ├── WikibookContributorHandler.java
│   │   ├── WikipediaSearchService.java
│   │   ├── MyWebException.java
│   │   └── aufgabe5.fxml
│   ├── doc/                  # Generierte Javadoc-Dokumentation
│   └── sample/
├── Aufgabenblatt5.iml        # IntelliJ-Projektdatei
├── wikibooks_export.txt      # Beispiel-Exportdatei (C++, Java)
├── wikibooks_sammlung.dat    # Lokale Persistenz-Datei (Zettelkasten)
└── .gitignore
```

---

## Technologien

| Technologie | Verwendung |
|---|---|
| Java 11+ | Programmiersprache |
| JavaFX | GUI-Framework (WebView, ListView, ComboBox, FXML) |
| MediaWiki Export-API | Abruf von Wikibooks-Metadaten (XML) |
| MediaWiki Action-API | Suche in Wikipedia-Artikeln (JSON) |
| SAX-Parser (`javax.xml`) | Parsen des Wikibooks-Export-XML |
| JSON.simple | Parsen der Wikipedia-API-Antworten |
| Java Serialization | Lokale Persistenz des Zettelkastens |
| IntelliJ IDEA | Entwicklungsumgebung |

---

## Autor

**Jonas Rohe**  
Matrikelnummer: 21511  
Hochschule Stralsund – Softwareentwicklung 1  

---

## Lizenz

Die ueber die Anwendung abgerufenen Inhalte stammen aus Wikibooks und Wikipedia und unterliegen der [Creative Commons Attribution-ShareAlike Lizenz (CC BY-SA)](https://creativecommons.org/licenses/by-sa/4.0/).
