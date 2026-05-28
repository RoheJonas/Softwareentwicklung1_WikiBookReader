/**
 * Controller für Aufgabe 5 - WikiBooks Browser mit Wikipedia und Zettelkasten
 * Inklusive Aufgabe 8: Begriffshistorie (Vor/Zurück)
 * Inklusive Aufgabe 9: ComboBox mit Begriffshistorie
 * Inklusive Aufgabe 10: Menü mit Info-Dialog
 * Inklusive Aufgabe 12: Tab-Reihenfolge und Shortcuts (F1)
 * Autor: Jonas Rohe
 * Matrikelnr: 21511
 * Umgebung: IntelliJ
 * Erstellt: 26.12.2025
 * Letzte Änderung: 31.12.2025
 */

package aufgabe5;

import aufgabe4.WikiBooks;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Aufgabe5Controller {

    @FXML private TextField _searchField;
    @FXML private WebView _webView;
    @FXML private Label _labelUrheber;
    @FXML private Label _labelZeit;
    @FXML private ListView<WikiBook> _listViewZettelkasten;
    @FXML private Button _btnBack;
    @FXML private Button _btnForward;
    @FXML private ComboBox<String> _comboBoxHistory;

    private WebEngine _webEngine;
    private ObservableList<WikiBook> _wikiBuecherzusammlung;

    // Für Dialogs (Aufgabe 6B/7)
    private Stage _wikipediaDialogStage;
    private ListView<String> _wikipediaListView;
    private Button _searchWikipediaButton;
    private ListView<String> _zettelkastenListView;

    // Historien-Verwaltung (Aufgabe 8/9)
    private List<String> _history = new ArrayList<>();
    private int _historyIndex = -1;
    private boolean _isNavigating = false;
    private boolean _isComboSelecting = false;

    @FXML
    public void initialize() {
        _webEngine = _webView.getEngine();

        _wikiBuecherzusammlung = FXCollections.observableArrayList();
        _listViewZettelkasten.setItems(_wikiBuecherzusammlung);

        updateNavigationButtons();
        _comboBoxHistory.setOnAction(this::onComboBoxHistorySelected);

        createWikipediaDialog();

        // --- Aufgabe 12: Global F1-Shortcut für Info-Dialog ---
        setupGlobalKeyHandlers();
    }

    // ========== AUFGABE 12: SHORTCUTS UND TAB-REIHENFOLGE ==========

    /**
     * Registriert globale Key-Handler für Shortcuts (F1)
     */
    private void setupGlobalKeyHandlers() {
        // F1 überall in der App → Info-Dialog öffnen
        _searchField.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(this::onGlobalKeyPressed);
            }
        });
    }

    /**
     * Handler für globale Tastatur-Events (F1)
     */
    @FXML
    private void onGlobalKeyPressed(KeyEvent _event) {
        if (_event.getCode() == KeyCode.F1) {
            onAboutClicked(null);
            _event.consume();
        }
    }

    /**
     * Handler für ENTER in der Wikipedia-Liste → Suche auslösen
     */
    private void setupWikipediaListKeyHandler() {
        _wikipediaListView.setOnKeyPressed(_event -> {
            if (_event.getCode() == KeyCode.ENTER) {
                // Nur auslösen, wenn Button aktiv wäre
                if (!_searchWikipediaButton.isDisable()) {
                    onSearchWikipediaClicked(null);
                }
                _event.consume();
            }
        });
    }

    /**
     * Setzt die Tab-Reihenfolge für die GUI (Aufgabe 12)
     * Laut Aufgabenblatt: 1. Suchfeld, 2. Suchen-Button, 3-14. usw.
     */
    private void setupTabTraversal() {
        // Tab-Order definieren per setStyle (einfachste Variante)
        // Die Reihenfolge ist die Priorität (0 = wichtig, Integer.MAX_VALUE = ignorieren)

        // 1. Suchfeld (höchste Priorität)
        _searchField.setStyle("-fx-focus-traversable: true;");

        // Alle nicht erwünschten Elemente ausgrauen (nicht tabbbar)
        _webView.setStyle("-fx-focus-traversable: false;");
        _labelUrheber.setStyle("-fx-focus-traversable: false;");
        _labelZeit.setStyle("-fx-focus-traversable: false;");

        // Die Buttons sind standardmäßig tabbbar (ok so)
    }

    // ========== AUFGABE 10: MENÜ ==========

    @FXML
    private void onAboutClicked(ActionEvent _event) {
        String infoText = "Alle redaktionellen Inhalte stammen von den Internetseiten der Projekte " +
                "Wikibooks und Wortschatz.\n\n" +
                "Die von Wikibooks bezogenen Inhalte unterliegen seit dem 22. Juni 2009 unter der " +
                "Lizenz CC-BY-SA 3.0 Unported zur Verfügung. Eine deutschsprachige Dokumentation " +
                "für Weiternutzer findet man in den Nutzungsbedingungen der Wikimedia Foundation. " +
                "Für alle Inhalte von Wikibooks galt bis zum 22. Juni 2009 standardmäßig die GNU FDL " +
                "(GNU Free Documentation License, engl. für GNU-Lizenz für freie Dokumentation). " +
                "Der Text der GNU FDL ist unter http://de.wikipedia.org/wiki/Wikipedia:GNU_Free_Documentation_License verfügbar.\n\n" +
                "Die von Wortschatz (http://wortschatz.uni-leipzig.de/) oder Wikipedia (www.wikipedia.de) " +
                "bezogenen Inhalte sind urheberrechtlich geschützt. Sie werden hier für wissenschaftliche " +
                "Zwecke eingesetzt und dürfen darüber hinaus in keiner Weise genutzt werden.\n\n" +
                "Dieses Programm ist nur zur Nutzung durch den Programmierer selbst gedacht. Dieses Programm " +
                "dient der Demonstration und dem Erlernen von Prinzipien der Programmierung mit Java. Eine " +
                "Verwendung des Programms für andere Zwecke verletzt möglicherweise die Urheberrechte der " +
                "Originalautoren der redaktionellen Inhalte und ist daher untersagt.";

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Über dieses Programm");
        alert.setHeaderText("Lizenzinformationen");
        alert.setContentText(infoText);
        alert.getDialogPane().setPrefWidth(600);
        alert.getDialogPane().setPrefHeight(400);
        alert.showAndWait();
    }

    // ========== AUFGABE 8/9: HISTORIE LOGIK ==========

    private void updateNavigationButtons() {
        if (_btnBack != null) {
            _btnBack.setDisable(_historyIndex <= 0);
        }
        if (_btnForward != null) {
            _btnForward.setDisable(_historyIndex >= _history.size() - 1);
        }
    }

    private void updateComboBox() {
        if (_comboBoxHistory == null) return;

        ObservableList<String> comboItems = FXCollections.observableArrayList();
        for (int i = _history.size() - 1; i >= 0; i--) {
            comboItems.add(_history.get(i));
        }
        _comboBoxHistory.setItems(comboItems);

        if (_historyIndex >= 0 && _historyIndex < _history.size()) {
            String currentTerm = _history.get(_historyIndex);
            _comboBoxHistory.setValue(currentTerm);
        }
    }

    @FXML
    private void onComboBoxHistorySelected(ActionEvent _event) {
        String selected = _comboBoxHistory.getValue();
        if (selected == null || selected.isEmpty()) return;

        int index = _history.indexOf(selected);
        if (index >= 0) {
            _historyIndex = index;
            navigateHistory();
        }
    }

    private void addToHistory(String _term) {
        if (_isNavigating) return;
        if (_isComboSelecting) return;

        if (_historyIndex < _history.size() - 1) {
            _history = _history.subList(0, _historyIndex + 1);
        }

        if (_history.isEmpty() || !_history.get(_history.size() - 1).equals(_term)) {
            _history.add(_term);
            _historyIndex++;
        }

        updateNavigationButtons();
        updateComboBox();
    }

    @FXML
    public void onBackClicked(ActionEvent _event) {
        if (_historyIndex > 0) {
            _historyIndex--;
            navigateHistory();
        }
    }

    @FXML
    public void onForwardClicked(ActionEvent _event) {
        if (_historyIndex < _history.size() - 1) {
            _historyIndex++;
            navigateHistory();
        }
    }

    private void navigateHistory() {
        _isNavigating = true;
        String term = _history.get(_historyIndex);
        _searchField.setText(term);
        performSearch();
        _isNavigating = false;
        updateNavigationButtons();
        updateComboBox();
    }

    // ========== WIKIPEDIA DIALOG ==========

    private void createWikipediaDialog() {
        _wikipediaDialogStage = new Stage();
        _wikipediaDialogStage.setTitle("Wikipedia Artikel und Zettelkasten");
        _wikipediaDialogStage.initModality(Modality.NONE);
        _wikipediaDialogStage.setWidth(500);
        _wikipediaDialogStage.setHeight(600);

        Label wikipediaLabel = new Label("Wikipedia Artikel:");
        wikipediaLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12;");

        _wikipediaListView = new ListView<>();
        _wikipediaListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        _wikipediaListView.setPrefHeight(200);

        _searchWikipediaButton = new Button("Suche Wikipedia");
        _searchWikipediaButton.setOnAction(this::onSearchWikipediaClicked);
        _searchWikipediaButton.setDisable(true);

        _wikipediaListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            boolean isValidSelection = newValue != null && !newValue.equals("<keine>") && !newValue.isEmpty();
            _searchWikipediaButton.setDisable(!isValidSelection);
        });

        // --- Aufgabe 12: ENTER-Key Handler für Wikipedia-Liste ---
        _wikipediaListView.setOnKeyPressed(_event -> {
            if (_event.getCode() == KeyCode.ENTER) {
                if (!_searchWikipediaButton.isDisable()) {
                    onSearchWikipediaClicked(null);
                }
                _event.consume();
            }
        });

        _wikipediaListView.setOnMouseClicked(_event -> {
            if (_event.getClickCount() == 2) {
                if (!_searchWikipediaButton.isDisable()) {
                    onSearchWikipediaClicked(null);
                }
            }
        });

        VBox wikipediaSection = new VBox(8);
        wikipediaSection.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1; -fx-padding: 8;");
        wikipediaSection.getChildren().addAll(wikipediaLabel, _wikipediaListView, _searchWikipediaButton);

        Label zettelkastenLabel = new Label("Zettelkasten Titel:");
        zettelkastenLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12;");

        _zettelkastenListView = new ListView<>();
        _zettelkastenListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        _zettelkastenListView.setPrefHeight(200);

        VBox zettelkastenSection = new VBox(8);
        zettelkastenSection.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1; -fx-padding: 8;");
        zettelkastenSection.getChildren().addAll(zettelkastenLabel, _zettelkastenListView);

        VBox root = new VBox(12);
        root.setStyle("-fx-padding: 12;");
        root.getChildren().addAll(wikipediaSection, zettelkastenSection);

        Scene scene = new Scene(root);
        _wikipediaDialogStage.setScene(scene);
    }

    private void showWikipediaDialog(String _searchTerm) {
        new Thread(() -> {
            try {
                List<String> articles = WikipediaSearchService.searchWikipediaArticles(_searchTerm);

                Platform.runLater(() -> {
                    ObservableList<String> wikipediaItems = FXCollections.observableArrayList();

                    if (articles.isEmpty()) {
                        wikipediaItems.add("<keine>");
                        _wikipediaListView.setDisable(true);
                    } else {
                        wikipediaItems.addAll(articles);
                        _wikipediaListView.setDisable(false);
                    }

                    _wikipediaListView.setItems(wikipediaItems);
                    _searchWikipediaButton.setDisable(true);

                    List<String> zettelkastenTitel = new ArrayList<>();
                    for (WikiBook book : _wikiBuecherzusammlung) {
                        zettelkastenTitel.add(book.getTitel());
                    }

                    ObservableList<String> zettelkastenItems = FXCollections.observableArrayList(zettelkastenTitel);
                    _zettelkastenListView.setItems(zettelkastenItems);

                    if (!_wikipediaDialogStage.isShowing()) {
                        _wikipediaDialogStage.show();
                    }
                });

            } catch (MyWebException e) {
                Platform.runLater(() -> showError("Wikipedia-Fehler:\n" + e.getMessage()));
            } catch (Exception e) {
                Platform.runLater(() -> showError("Unerwarteter Fehler:\n" + e.getMessage()));
            }
        }).start();
    }

    private void onSearchWikipediaClicked(ActionEvent _event) {
        String selected = _wikipediaListView.getSelectionModel().getSelectedItem();
        if (selected == null || selected.isEmpty() || selected.equals("<keine>")) return;
        _searchField.setText(selected);
        performSearch();
    }

    // ========== SUCHEN ==========

    @FXML
    public void onSearchButtonClicked(ActionEvent _event) {
        performSearch();
    }

    @FXML
    public void onSearchFieldKeyPressed(KeyEvent _event) {
        if (_event.getCode() == KeyCode.ENTER) {
            performSearch();
        }
    }

    private void performSearch() {
        String titel = _searchField.getText();
        if (titel == null || titel.trim().isEmpty()) {
            showError("Bitte geben Sie einen Titel ein!");
            return;
        }

        titel = titel.trim();

        addToHistory(titel);

        String url = WikiBooks.buildURL(titel);
        _webEngine.load(url);

        final String searchTitle = titel;
        new Thread(() -> {
            try {
                WikiBook book = WikibookMetadataService.getMetadata(searchTitle);

                Platform.runLater(() -> {
                    _labelUrheber.setText("Letzter Bearbeiter: " + book.getUrheber());
                    _labelZeit.setText("Letzte Änderung: " + book.getTimestampLokal());
                });

            } catch (MyWebException e) {
                Platform.runLater(() -> showError("WikiBooks-Fehler:\n" + e.getMessage()));
            } catch (Exception e) {
                Platform.runLater(() -> showError("Unerwarteter Fehler:\n" + e.getMessage()));
            }
        }).start();

        showWikipediaDialog(searchTitle);
    }

    // ========== ZETTELKASTEN FUNKTIONEN ==========

    @FXML
    public void onHinzufuegenClicked(ActionEvent _event) {
        String titel = _searchField.getText();
        if (titel == null || titel.trim().isEmpty()) {
            showWarning("Bitte suchen Sie zuerst einen Artikel!");
            return;
        }
        titel = titel.trim();

        for (WikiBook book : _wikiBuecherzusammlung) {
            if (book.getTitel().equalsIgnoreCase(titel)) {
                showWarning("\"" + titel + "\" ist bereits in der Liste!");
                return;
            }
        }

        try {
            WikiBook newBook = WikibookMetadataService.getMetadata(titel);
            _wikiBuecherzusammlung.add(newBook);

            List<String> zettelkastenTitel = new ArrayList<>();
            for (WikiBook book : _wikiBuecherzusammlung) {
                zettelkastenTitel.add(book.getTitel());
            }
            _zettelkastenListView.setItems(FXCollections.observableArrayList(zettelkastenTitel));

            showInfo("\"" + titel + "\" wurde hinzugefügt!");
        } catch (MyWebException e) {
            showError("Konnte Buch nicht hinzufügen:\n" + e.getMessage());
        }
    }

    @FXML
    public void onLoeschenClicked(ActionEvent _event) {
        WikiBook selected = _listViewZettelkasten.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Bitte wählen Sie ein Buch aus!");
            return;
        }
        String titel = selected.getTitel();
        _wikiBuecherzusammlung.remove(selected);

        List<String> zettelkastenTitel = new ArrayList<>();
        for (WikiBook book : _wikiBuecherzusammlung) {
            zettelkastenTitel.add(book.getTitel());
        }
        _zettelkastenListView.setItems(FXCollections.observableArrayList(zettelkastenTitel));

        showInfo("\"" + titel + "\" wurde gelöscht!");
    }

    @FXML
    public void onSortierenClicked(ActionEvent _event) {
        List<WikiBook> sortedList = new ArrayList<>(_wikiBuecherzusammlung);
        sortedList.sort((a, b) -> a.getTitel().compareToIgnoreCase(b.getTitel()));
        _wikiBuecherzusammlung.clear();
        _wikiBuecherzusammlung.addAll(sortedList);
        showInfo("Liste wurde alphabetisch sortiert!");
    }

    @FXML
    public void onSpeichernClicked(ActionEvent _event) {
        if (_wikiBuecherzusammlung.isEmpty()) {
            showWarning("Liste ist leer!");
            return;
        }
        try {
            File file = new File("wikibooks_sammlung.dat");
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            List<WikiBook> toSave = new ArrayList<>(_wikiBuecherzusammlung);
            oos.writeObject(toSave);
            oos.close();
            showInfo("Sammlung in \"wikibooks_sammlung.dat\" gespeichert!");
        } catch (IOException e) {
            showError("Fehler beim Speichern:\n" + e.getMessage());
        }
    }

    @FXML
    public void onLadenClicked(ActionEvent _event) {
        try {
            File file = new File("wikibooks_sammlung.dat");
            if (!file.exists()) {
                showWarning("Datei \"wikibooks_sammlung.dat\" nicht gefunden!");
                return;
            }
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            @SuppressWarnings("unchecked")
            List<WikiBook> loaded = (List<WikiBook>) ois.readObject();
            ois.close();

            _wikiBuecherzusammlung.clear();
            _wikiBuecherzusammlung.addAll(loaded);

            List<String> zettelkastenTitel = new ArrayList<>();
            for (WikiBook book : _wikiBuecherzusammlung) {
                zettelkastenTitel.add(book.getTitel());
            }
            _zettelkastenListView.setItems(FXCollections.observableArrayList(zettelkastenTitel));

            showInfo("Sammlung geladen! (" + loaded.size() + " Bücher)");
        } catch (IOException | ClassNotFoundException e) {
            showError("Fehler beim Laden:\n" + e.getMessage());
        }
    }

    @FXML
    public void onImportClicked(ActionEvent _event) {
        showInfo("Import-Funktion noch nicht implementiert");
    }

    @FXML
    public void onExportClicked(ActionEvent _event) {
        if (_wikiBuecherzusammlung.isEmpty()) {
            showWarning("Liste ist leer!");
            return;
        }
        try {
            File file = new File("wikibooks_export.txt");
            FileWriter fw = new FileWriter(file);
            for (WikiBook book : _wikiBuecherzusammlung) {
                fw.write("=== " + book.getTitel() + " ===\n");
                fw.write("Urheber: " + book.getUrheber() + "\n");
                fw.write("Änderung: " + book.getTimestampLokal() + "\n");
                fw.write("Regale: " + book.getRegale() + "\n\n");
            }
            fw.close();
            showInfo("Sammlung als \"wikibooks_export.txt\" exportiert!");
        } catch (IOException e) {
            showError("Fehler beim Export:\n" + e.getMessage());
        }
    }

    // ========== DIALOG HELPER ==========

    private void showError(String _msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler");
        alert.setHeaderText("Ein Fehler ist aufgetreten");
        alert.setContentText(_msg);
        alert.showAndWait();
    }

    private void showWarning(String _msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warnung");
        alert.setHeaderText("Warnung");
        alert.setContentText(_msg);
        alert.showAndWait();
    }

    private void showInfo(String _msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("Information");
        alert.setContentText(_msg);
        alert.showAndWait();
    }
}
