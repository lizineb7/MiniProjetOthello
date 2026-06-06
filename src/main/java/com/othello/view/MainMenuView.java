package com.othello.view;

import com.othello.model.DatabaseDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;
import java.util.Map;

/**
 * Écran d'accueil - Choix du mode de jeu et affichage du leaderboard (JavaFX)
 */
public class MainMenuView extends VBox {

    // Couleurs
    private static final Color COLOR_BLACK = Color.web("#2c3e50");
    private static final Color COLOR_WHITE = Color.web("#ecf0f1");
    private static final Color COLOR_GREEN = Color.web("#27ae60");
    private static final Color COLOR_ORANGE = Color.web("#f39c12");
    private static final Color COLOR_BG = Color.web("#f5f5f5");
    private static final Color COLOR_TEXT_SECONDARY = Color.web("#7f8c8d");

    private TextField player1NameField;
    private TextField player2NameField;
    private ComboBox<String> gameModeDropdown;
    private ComboBox<String> difficultyDropdown;
    private CheckBox aiCheckBox;
    private TableView<LeaderboardEntry> leaderboardTable;
    private Button playButton;
    private Button rulesButton;

    private Runnable onPlayButtonPressed;
    private Runnable onRulesButtonPressed;

    public MainMenuView() {
        initializeUI();
    }

    /**
     * Initialise l'interface
     */
    private void initializeUI() {
        setStyle("-fx-background-color: #f5f5f5;");
        setSpacing(15);
        setPadding(new Insets(20));
        setAlignment(Pos.TOP_CENTER);

        // Titre
        Label titleLabel = new Label("🎮 OTHELLO GAME");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(COLOR_BLACK);

        // Contenu principal (grille avec 2 colonnes)
        HBox contentBox = new HBox(20);
        contentBox.setPadding(new Insets(10));
        contentBox.setAlignment(Pos.TOP_CENTER);

        // Panneau gauche : paramètres de jeu
        VBox gameParamsPanel = createGameParamsPanel();

        // Panneau droit : leaderboard
        VBox leaderboardPanel = createLeaderboardPanel();

        contentBox.getChildren().addAll(gameParamsPanel, leaderboardPanel);

        // Barre d'actions
        HBox actionBar = createActionBar();

        // Ajoute tous les éléments
        getChildren().addAll(titleLabel, contentBox, actionBar);
    }

    /**
     * Crée le panneau des paramètres de jeu
     */
    private VBox createGameParamsPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(15));
        panel.setStyle("-fx-border-color: #7f8c8d; -fx-border-width: 1; -fx-background-color: white;");
        panel.setPrefWidth(350);

        // Titre
        Label panelTitle = new Label("⚙ PARAMÈTRES DE JEU");
        panelTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        panelTitle.setTextFill(COLOR_BLACK);

        // Joueur 1
        Label player1Label = new Label("Joueur 1 (Noir) :");
        player1Label.setFont(Font.font("Arial", 12));
        player1Label.setTextFill(COLOR_TEXT_SECONDARY);

        player1NameField = new TextField("Joueur 1");
        player1NameField.setStyle("-fx-padding: 8; -fx-font-size: 12;");

        // Mode IA
        aiCheckBox = new CheckBox("Jouer contre l'IA");
        aiCheckBox.setFont(Font.font("Arial", 12));
        aiCheckBox.setTextFill(COLOR_BLACK);
        aiCheckBox.setOnAction(e -> updatePlayer2Field());

        // Joueur 2
        Label player2Label = new Label("Joueur 2 (Blanc) :");
        player2Label.setFont(Font.font("Arial", 12));
        player2Label.setTextFill(COLOR_TEXT_SECONDARY);

        player2NameField = new TextField("Joueur 2");
        player2NameField.setStyle("-fx-padding: 8; -fx-font-size: 12;");

        // Difficulté
        Label difficultyLabel = new Label("Difficulté IA :");
        difficultyLabel.setFont(Font.font("Arial", 12));
        difficultyLabel.setTextFill(COLOR_TEXT_SECONDARY);

        difficultyDropdown = new ComboBox<>();
        difficultyDropdown.getItems().addAll("Facile", "Moyen", "Difficile");
        difficultyDropdown.setValue("Moyen");
        difficultyDropdown.setStyle("-fx-padding: 8; -fx-font-size: 12;");

        // Ajoute tous les éléments
        panel.getChildren().addAll(
                panelTitle,
                new Separator(),
                player1Label,
                player1NameField,
                aiCheckBox,
                player2Label,
                player2NameField,
                difficultyLabel,
                difficultyDropdown
        );

        return panel;
    }

    /**
     * Crée le panneau du leaderboard
     */
    private VBox createLeaderboardPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(15));
        panel.setStyle("-fx-border-color: #7f8c8d; -fx-border-width: 1; -fx-background-color: white;");
        panel.setPrefWidth(360);

        // Titre
        Label panelTitle = new Label("🏆 TOP JOUEURS");
        panelTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        panelTitle.setTextFill(COLOR_BLACK);

        // Tableau
        leaderboardTable = new TableView<>();
        leaderboardTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        leaderboardTable.setPrefHeight(220);

        TableColumn<LeaderboardEntry, Integer> rankCol = new TableColumn<>("Rang");
        rankCol.setCellValueFactory(new PropertyValueFactory<>("rank"));
        rankCol.setPrefWidth(60);

        TableColumn<LeaderboardEntry, String> nameCol = new TableColumn<>("Joueur");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(120);

        TableColumn<LeaderboardEntry, Integer> gamesCol = new TableColumn<>("Parties");
        gamesCol.setCellValueFactory(new PropertyValueFactory<>("totalGames"));
        gamesCol.setPrefWidth(80);

        TableColumn<LeaderboardEntry, Integer> winsCol = new TableColumn<>("Victoires");
        winsCol.setCellValueFactory(new PropertyValueFactory<>("wins"));
        winsCol.setPrefWidth(80);

        leaderboardTable.getColumns().addAll(rankCol, nameCol, gamesCol, winsCol);

        refreshLeaderboard();

        // Ajoute les éléments
        panel.getChildren().addAll(panelTitle, new Separator(), leaderboardTable);

        return panel;
    }

    /**
     * Crée la barre d'actions
     */
    private HBox createActionBar() {
        HBox actionBar = new HBox(10);
        actionBar.setPadding(new Insets(10));
        actionBar.setAlignment(Pos.CENTER);
        actionBar.setStyle("-fx-background-color: #ecf0f1; -fx-border-color: #7f8c8d; -fx-border-width: 1 0 0 0;");

        // Bouton Jouer
        playButton = createButton("▶ JOUER", COLOR_GREEN);
        playButton.setOnAction(e -> handlePlayButtonPressed());

        // Bouton Règles
        rulesButton = createButton("📋 RÈGLES", COLOR_BLACK);
        rulesButton.setOnAction(e -> showRulesDialog());

        actionBar.getChildren().addAll(playButton, rulesButton);

        return actionBar;
    }

    /**
     * Crée un bouton stylisé
     */
    private Button createButton(String text, Color bgColor) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        button.setStyle("-fx-padding: 10 20; -fx-font-size: 13;");
        button.setStyle(String.format(
                "-fx-padding: 10 20; -fx-font-size: 13; -fx-background-color: %s; -fx-text-fill: white;",
                toHexString(bgColor)
        ));
        button.setCursor(javafx.scene.Cursor.HAND);

        button.setOnMouseEntered(e -> button.setStyle(String.format(
                "-fx-padding: 10 20; -fx-font-size: 13; -fx-background-color: %s; -fx-text-fill: white;",
                toHexString(bgColor.darker())
        )));

        button.setOnMouseExited(e -> button.setStyle(String.format(
                "-fx-padding: 10 20; -fx-font-size: 13; -fx-background-color: %s; -fx-text-fill: white;",
                toHexString(bgColor)
        )));

        return button;
    }

    /**
     * Convertit une Color en hex
     */
    private String toHexString(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    /**
     * Met à jour le champ joueur 2
     */
    private void updatePlayer2Field() {
        if (aiCheckBox.isSelected()) {
            player2NameField.setText("IA");
            player2NameField.setDisable(true);
        } else {
            player2NameField.setText("Joueur 2");
            player2NameField.setDisable(false);
        }
    }

    /**
     * Gère le clic du bouton Jouer
     */
    private void handlePlayButtonPressed() {
        String player1 = player1NameField.getText().trim();
        String player2 = player2NameField.getText().trim();

        if (player1.isEmpty() || player2.isEmpty()) {
            showError("Veuillez entrer les noms des joueurs");
            return;
        }

        if (onPlayButtonPressed != null) {
            onPlayButtonPressed.run();
        }
    }

    /**
     * Affiche la boîte de dialogue des règles
     */
    private void showRulesDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Règles du Jeu Othello");
        alert.setHeaderText("📖 RÈGLES DU JEU OTHELLO");

        String rulesText = "• Grille : 8×8 cases\n" +
                "• Joueurs : 2 joueurs (Noir et Blanc)\n" +
                "• Objectif : Avoir le plus de pions de sa couleur\n\n" +
                "Placement :\n" +
                "• Un coup est valide s'il capture au moins un pion adverse\n" +
                "• Placer un pion et retourner les adversaires encadrés\n\n" +
                "Fin :\n" +
                "• Quand aucun joueur ne peut jouer\n" +
                "• Le joueur avec le plus de pions gagne\n\n" +
                "Variante IA :\n" +
                "• Facile : Coups aléatoires\n" +
                "• Moyen : Stratégie simple\n" +
                "• Difficile : Minimax avancé";

        alert.setContentText(rulesText);
        alert.showAndWait();
    }

    /**
     * Affiche une erreur
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Rafraîchit le leaderboard
     */
    public void refreshLeaderboard() {
        leaderboardTable.getItems().clear();

        List<Map<String, Object>> topPlayers = DatabaseDAO.getTopPlayers(10);

        int rank = 1;
        for (Map<String, Object> player : topPlayers) {
            String name = (String) player.get("name");
            int totalGames = (int) player.get("totalGames");
            int wins = (int) player.get("wins");

            leaderboardTable.getItems().add(new LeaderboardEntry(rank, name, totalGames, wins));
            rank++;
        }
    }

    // Getters

    public String getPlayer1Name() {
        return player1NameField.getText();
    }

    public String getPlayer2Name() {
        return player2NameField.getText();
    }

    public boolean isAISelected() {
        return aiCheckBox.isSelected();
    }

    public int getDifficulty() {
        return difficultyDropdown.getSelectionModel().getSelectedIndex() + 1;
    }

    // Event handlers

    public void setOnPlayButtonPressed(Runnable handler) {
        this.onPlayButtonPressed = handler;
    }

    public void setOnRulesButtonPressed(Runnable handler) {
        this.onRulesButtonPressed = handler;
    }

    /**
     * Classe interne pour les entrées du leaderboard
     */
    public static class LeaderboardEntry {
        private int rank;
        private String name;
        private int totalGames;
        private int wins;

        public LeaderboardEntry(int rank, String name, int totalGames, int wins) {
            this.rank = rank;
            this.name = name;
            this.totalGames = totalGames;
            this.wins = wins;
        }

        public int getRank() { return rank; }
        public String getName() { return name; }
        public int getTotalGames() { return totalGames; }
        public int getWins() { return wins; }
    }
}