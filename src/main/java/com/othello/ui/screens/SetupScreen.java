package com.othello.ui.screens;

import com.othello.ui.Constants;
import com.othello.ui.Styles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import com.othello.ui.UIUtils;

/**
 * Setup Screen - FIXED version with proper layout
 */
public class SetupScreen extends BorderPane {

    private TextField player1NameField;
    private TextField player2NameField;
    private CheckBox aiCheckBox;
    private ComboBox<String> difficultyDropdown;
    private TableView<LeaderboardEntry> leaderboardTable;
    private Button playButton;

    private Button closeButton;
    private Button maximizeButton;
    private Button minimizeButton;

    private Runnable onPlayButtonPressed;
    private Runnable onRulesButtonPressed;

    public SetupScreen() {
        setStyle(Styles.SURFACE_BACKGROUND);

        // ✅ FIX: Use BorderPane for proper layout management
        setTop(createHeader());
        setCenter(createContentContainer());
        setBottom(createFooter());

        updatePlayer2Field();
    }

    // ============================================================
    // HEADER
    // ============================================================

    private VBox createHeader() {
        VBox header = new VBox();
        header.setStyle(Styles.HEADER_STYLE);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPrefHeight(70);  // ✅ FIX: Fixed header height

        HBox headerContent = new HBox();
        headerContent.setAlignment(Pos.CENTER_LEFT);
        headerContent.setSpacing(Constants.SPACING_16);
        headerContent.setPadding(new Insets(Constants.SPACING_16, Constants.SPACING_24, Constants.SPACING_16, Constants.SPACING_24));

        Label title = new Label("🎮 OTHELLO GAME");
        title.setStyle(Styles.LABEL_TITLE);

        HBox gameInfo = new HBox();
        gameInfo.setSpacing(Constants.SPACING_12);
        gameInfo.setAlignment(Pos.CENTER_LEFT);
        Label modeLabel = new Label("Mode: Normal");
        modeLabel.setStyle(Styles.LABEL_SECONDARY);
        gameInfo.getChildren().add(modeLabel);

        javafx.scene.layout.Region spacer = new javafx.scene.layout.Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox windowControls = new HBox(12);
        windowControls.setAlignment(Pos.CENTER_RIGHT);

        minimizeButton = new Button("—");
        minimizeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #5f6368; -fx-font-size: 18px; -fx-cursor: hand; -fx-font-weight: bold; -fx-padding: 5;");

        maximizeButton = new Button("🗖");
        maximizeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #5f6368; -fx-font-size: 14px; -fx-cursor: hand; -fx-padding: 5;");

        closeButton = new Button("✕");
        closeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #5f6368; -fx-font-size: 18px; -fx-cursor: hand; -fx-font-weight: bold; -fx-padding: 5;");

        closeButton.setOnMouseEntered(e -> closeButton.setStyle("-fx-background-color: #e81123; -fx-text-fill: white; -fx-font-size: 18px; -fx-cursor: hand; -fx-padding: 5;"));
        closeButton.setOnMouseExited(e -> closeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #5f6368; -fx-font-size: 18px; -fx-cursor: hand; -fx-padding: 5;"));

        windowControls.getChildren().addAll(minimizeButton, maximizeButton, closeButton);

        headerContent.getChildren().addAll(title, gameInfo, spacer, windowControls);
        header.getChildren().add(headerContent);
        return header;
    }

    // ============================================================
    // CENTER CONTENT
    // ============================================================

    private HBox createContentContainer() {
        HBox content = new HBox();
        content.setStyle(Styles.SURFACE_BACKGROUND);
        content.setSpacing(Constants.STACK_GAP);
        content.setPadding(new Insets(Constants.CONTAINER_PADDING));
        content.setAlignment(Pos.TOP_CENTER);

        VBox leftColumn = createParametersCard();
        // ✅ REMOVE: leftColumn.setPrefWidth(400);
        // ✅ REMOVE: leftColumn.setMaxWidth(450);
        leftColumn.setMaxWidth(Double.MAX_VALUE); // ← Grow as needed
        HBox.setHgrow(leftColumn, Priority.ALWAYS); // ← GROW with window

        VBox rightColumn = createLeaderboardCard();
        // ✅ REMOVE: rightColumn.setPrefWidth(400);
        // ✅ REMOVE: rightColumn.setMaxWidth(450);
        rightColumn.setMaxWidth(Double.MAX_VALUE); // ← Grow as needed
        HBox.setHgrow(rightColumn, Priority.ALWAYS); // ← GROW with window

        content.getChildren().addAll(leftColumn, rightColumn);
        return content;
    }

    private VBox createParametersCard() {
        VBox card = new VBox();
        card.setStyle(Styles.CARD_STYLE);
        card.setSpacing(Constants.SPACING_16);
        card.setPadding(new Insets(Constants.SPACING_24));

        Label headingLabel = new Label("PARAMÈTRES DE JEU");
        headingLabel.setStyle(Styles.LABEL_SECTION_HEADING);
        card.getChildren().add(headingLabel);

        Separator separator = new Separator();
        separator.setStyle("-fx-padding: 0; -fx-border-color: " + Constants.PRIMARY_HEX + "; -fx-border-width: 2 0 0 0;");
        card.getChildren().add(separator);

        Label player1Label = new Label("Joueur 1 (Noir) :");
        player1Label.setStyle(Styles.LABEL_FIELD);
        player1NameField = new TextField("Joueur 1");
        player1NameField.setStyle(Styles.TEXT_INPUT);
        player1NameField.setMaxWidth(Double.MAX_VALUE);
        card.getChildren().addAll(player1Label, player1NameField);

        aiCheckBox = new CheckBox("Jouer contre l'IA");
        aiCheckBox.setStyle("-fx-font-family: '" + Constants.FONT_FAMILY + "'; -fx-font-size: " + Constants.SUBHEADING_SIZE + "; -fx-text-fill: #1a1a1a;");
        aiCheckBox.setOnAction(e -> updatePlayer2Field());
        card.getChildren().add(aiCheckBox);

        Label player2Label = new Label("Joueur 2 (Blanc) :");
        player2Label.setStyle(Styles.LABEL_FIELD);
        player2NameField = new TextField("Joueur 2");
        player2NameField.setStyle(Styles.TEXT_INPUT);
        player2NameField.setMaxWidth(Double.MAX_VALUE);
        card.getChildren().addAll(player2Label, player2NameField);

        Label difficultyLabel = new Label("Difficulté IA :");
        difficultyLabel.setStyle(Styles.LABEL_FIELD);
        difficultyDropdown = new ComboBox<>();
        difficultyDropdown.getItems().addAll("Facile", "Moyen", "Difficile");
        difficultyDropdown.setValue("Moyen");
        difficultyDropdown.setStyle(Styles.COMBOBOX);
        difficultyDropdown.setPrefWidth(160);
        card.getChildren().addAll(difficultyLabel, difficultyDropdown);

        VBox.setVgrow(card, Priority.ALWAYS);
        return card;
    }

    private VBox createLeaderboardCard() {
        VBox card = new VBox();
        card.setStyle(Styles.CARD_STYLE);
        card.setSpacing(Constants.SPACING_16);
        card.setPadding(new Insets(Constants.SPACING_24));

        Label headingLabel = new Label("TOP JOUEURS");
        headingLabel.setStyle(Styles.LABEL_SECTION_HEADING);
        card.getChildren().add(headingLabel);

        Separator separator = new Separator();
        separator.setStyle("-fx-padding: 0; -fx-border-color: " + Constants.PRIMARY_HEX + "; -fx-border-width: 2 0 0 0;");
        card.getChildren().add(separator);

        leaderboardTable = new TableView<>();
        leaderboardTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        leaderboardTable.setPrefHeight(250);

        TableColumn<LeaderboardEntry, Integer> rankCol = new TableColumn<>("Rang");
        rankCol.setCellValueFactory(cellData -> cellData.getValue().rankProperty().asObject());
        rankCol.setPrefWidth(60);

        TableColumn<LeaderboardEntry, String> nameCol = new TableColumn<>("Joueur");
        nameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        nameCol.setPrefWidth(120);

        TableColumn<LeaderboardEntry, Integer> gamesCol = new TableColumn<>("Parties");
        gamesCol.setCellValueFactory(cellData -> cellData.getValue().totalGamesProperty().asObject());
        gamesCol.setPrefWidth(80);

        TableColumn<LeaderboardEntry, Integer> winsCol = new TableColumn<>("Victoires");
        winsCol.setCellValueFactory(cellData -> cellData.getValue().winsProperty().asObject());
        winsCol.setPrefWidth(80);

        leaderboardTable.getColumns().addAll(rankCol, nameCol, gamesCol, winsCol);
        java.util.List<java.util.Map<String, Object>> topPlayersData = com.othello.model.DatabaseDAO.getTopPlayers(10);

        int rank = 1;
        for (java.util.Map<String, Object> playerData : topPlayersData) {
            String name = (String) playerData.get("name");
            int totalGames = (Integer) playerData.get("totalGames");
            int wins = (Integer) playerData.get("wins");

            // Constructs UI model using SQL database rows
            leaderboardTable.getItems().add(new LeaderboardEntry(rank++, name, totalGames, wins));
        }

        card.getChildren().add(leaderboardTable);
        VBox.setVgrow(leaderboardTable, Priority.ALWAYS);
        VBox.setVgrow(card, Priority.ALWAYS);

        return card;
    }

    // ============================================================
    // FOOTER
    // ============================================================

    private HBox createFooter() {
        HBox footer = new HBox();
        footer.setStyle(Styles.BOTTOM_NAV_STYLE);
        footer.setAlignment(Pos.CENTER_RIGHT);
        footer.setSpacing(Constants.SPACING_12);
        footer.setPadding(new Insets(Constants.SPACING_16));
        footer.setPrefHeight(60);  // ✅ FIX: Fixed footer height

        playButton = new Button("▶ JOUER");
        playButton.setStyle(Styles.BUTTON_PRIMARY);
        playButton.setOnAction(e -> handlePlayButtonPressed());
        playButton.setOnMouseEntered(e -> playButton.setStyle(Styles.BUTTON_PRIMARY_HOVER));
        playButton.setOnMouseExited(e -> playButton.setStyle(Styles.BUTTON_PRIMARY));

        Button rulesButton = new Button("📋 RÈGLES");
        rulesButton.setStyle(Styles.BUTTON_SECONDARY);
        rulesButton.setOnAction(e -> { if (onRulesButtonPressed != null) onRulesButtonPressed.run(); });
        rulesButton.setOnMouseEntered(e -> rulesButton.setStyle(Styles.BUTTON_SECONDARY_HOVER));
        rulesButton.setOnMouseExited(e -> rulesButton.setStyle(Styles.BUTTON_SECONDARY));

        footer.getChildren().addAll(playButton, rulesButton);
        return footer;
    }

    // ============================================================
    // HELPER METHODS
    // ============================================================

    private void updatePlayer2Field() {
        if (aiCheckBox.isSelected()) {
            player2NameField.setText("IA");
            player2NameField.setDisable(true);
            difficultyDropdown.setDisable(false);
        } else {
            player2NameField.setText("Joueur 2");
            player2NameField.setDisable(false);
            difficultyDropdown.setDisable(true);
        }
    }

    private void handlePlayButtonPressed() {
        String player1 = player1NameField.getText().trim();
        String player2 = player2NameField.getText().trim();
        if (player1.isEmpty() || player2.isEmpty()) {
            UIUtils.showError("Veuillez entrer les noms des joueurs");
            return;
        }
        if (onPlayButtonPressed != null) onPlayButtonPressed.run();
    }

    // ============================================================
    // GETTERS
    // ============================================================

    public String getPlayer1Name() { return player1NameField.getText(); }
    public String getPlayer2Name() { return player2NameField.getText(); }
    public boolean isAISelected() { return aiCheckBox.isSelected(); }
    public int getDifficulty() { return difficultyDropdown.getSelectionModel().getSelectedIndex() + 1; }
    public Button getCloseButton() { return closeButton; }
    public Button getMaximizeButton() {
        return this.maximizeButton;
    }
    public Button getMinimizeButton() { return minimizeButton; }
    public TableView<LeaderboardEntry> getLeaderboardTable() {
        return this.leaderboardTable;
    }

    // ============================================================
    // EVENT HANDLERS
    // ============================================================

    public void setOnPlayButtonPressed(Runnable handler) { this.onPlayButtonPressed = handler; }
    public void setOnRulesButtonPressed(Runnable handler) { this.onRulesButtonPressed = handler; }

    // ============================================================
    // LEADERBOARD ENTRY
    // ============================================================

    public static class LeaderboardEntry {
        private javafx.beans.property.SimpleIntegerProperty rank;
        private javafx.beans.property.SimpleStringProperty name;
        private javafx.beans.property.SimpleIntegerProperty totalGames;
        private javafx.beans.property.SimpleIntegerProperty wins;

        public LeaderboardEntry(int rank, String name, int totalGames, int wins) {
            this.rank = new javafx.beans.property.SimpleIntegerProperty(rank);
            this.name = new javafx.beans.property.SimpleStringProperty(name);
            this.totalGames = new javafx.beans.property.SimpleIntegerProperty(totalGames);
            this.wins = new javafx.beans.property.SimpleIntegerProperty(wins);
        }
        public javafx.beans.property.IntegerProperty rankProperty() { return rank; }
        public javafx.beans.property.StringProperty nameProperty() { return name; }
        public javafx.beans.property.IntegerProperty totalGamesProperty() { return totalGames; }
        public javafx.beans.property.IntegerProperty winsProperty() { return wins; }
    }
}