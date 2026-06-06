package com.othello.view;

import com.othello.model.GameSession;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Affichage du scoreboardet de l'historique des coups (JavaFX)
 */
public class ScoreboardView extends VBox {

    // Couleurs
    private static final Color COLOR_BLACK = Color.web("#2c3e50");
    private static final Color COLOR_WHITE = Color.web("#ecf0f1");
    private static final Color COLOR_GREEN = Color.web("#27ae60");
    private static final Color COLOR_BG = Color.web("#f5f5f5");
    private static final Color COLOR_TEXT_SECONDARY = Color.web("#7f8c8d");

    private GameSession gameSession;
    private TextArea historyTextArea;
    private Label player1Label;
    private Label player2Label;
    private Label currentPlayerLabel;

    public ScoreboardView() {
        setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #7f8c8d; -fx-border-width: 0 0 0 1;");
        setPadding(new Insets(15));
        setSpacing(10);
        setPrefWidth(300);

        initializeComponents();
    }

    /**
     * Initialise les composants
     */
    private void initializeComponents() {
        // Titre
        Label titleLabel = new Label("📊 STATISTIQUES");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        titleLabel.setTextFill(COLOR_BLACK);

        // Joueur 1
        player1Label = createPlayerLabel();

        // Joueur 2
        player2Label = createPlayerLabel();

        // Joueur actuel
        Label currentLabel = new Label("Tour actuel :");
        currentLabel.setFont(Font.font("Arial", 11));
        currentLabel.setTextFill(COLOR_TEXT_SECONDARY);

        currentPlayerLabel = new Label("?");
        currentPlayerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        currentPlayerLabel.setTextFill(COLOR_BLACK);

        // Séparateur
        Separator separator = new Separator();

        // Historique
        Label historyTitle = new Label("📜 HISTORIQUE");
        historyTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        historyTitle.setTextFill(COLOR_BLACK);

        // Texte de l'historique
        historyTextArea = new TextArea();
        historyTextArea.setEditable(false);
        historyTextArea.setWrapText(true);
        historyTextArea.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 10; -fx-control-inner-background: white;");
        historyTextArea.setPrefHeight(250);

        // Ajoute tous les éléments
        getChildren().addAll(
                titleLabel,
                player1Label,
                player2Label,
                currentLabel,
                currentPlayerLabel,
                separator,
                historyTitle,
                historyTextArea
        );
    }

    /**
     * Crée un label pour afficher les infos d'un joueur
     */
    private Label createPlayerLabel() {
        Label label = new Label();
        label.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        label.setTextFill(COLOR_BLACK);
        return label;
    }

    /**
     * Met à jour l'affichage
     */
    public void updateDisplay(GameSession gameSession) {
        this.gameSession = gameSession;

        // Met à jour les scores
        String player1Info = String.format("⚫ %s: %d pions",
                gameSession.getPlayer1().getName(),
                gameSession.getPlayer1().getScore());
        player1Label.setText(player1Info);

        String player2Info = String.format("⚪ %s: %d pions",
                gameSession.getPlayer2().getName(),
                gameSession.getPlayer2().getScore());
        player2Label.setText(player2Info);

        // Joueur actuel
        String currentInfo = gameSession.getCurrentPlayer().getName() +
                " (" + gameSession.getCurrentPlayer().getColorName() + ")";
        currentPlayerLabel.setText(currentInfo);

        // Historique
        historyTextArea.setText(gameSession.getMoveHistory());
        historyTextArea.setScrollTop(Double.MAX_VALUE);
    }
}