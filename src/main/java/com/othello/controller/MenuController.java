package com.othello.controller;

import com.othello.model.Player;
import com.othello.model.Board;
import com.othello.view.MainMenuView;

// JavaFX Imports
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Contrôleur du menu principal
 */
public class MenuController {

    private Stage mainStage; // Replaced JFrame with Stage
    private MainMenuView menuView;
    private GameController gameController;


    public Object[] createPlayers(String player1Name, String player2Name, boolean useAI, int difficulty) {
        // Player 1 is always Black
        Player player1 = new Player(player1Name, Board.BLACK);

        // Player 2 is White (either human or AI)
        Player player2;
        if (useAI) {
            // Assuming your Player model constructor matches: (Name, Color, Type, Difficulty)
            player2 = new Player("IA", Board.WHITE, Player.TYPE_AI, difficulty);
        } else {
            player2 = new Player(player2Name, Board.WHITE);
        }

        // Return them wrapped in an Object array as the UI expects
        return new Object[] { player1, player2 };
    }

    // Updated constructor to accept JavaFX Stage
    public MenuController(Stage mainStage) {
        this.mainStage = mainStage;
    }

    /**
     * Démarre une nouvelle partie
     */
    public void startGame(String player1Name, String player2Name,
                          boolean useAI, int difficulty) {
        // Crée les joueurs
        Player player1 = new Player(player1Name, Board.BLACK);

        Player player2;
        if (useAI) {
            player2 = new Player("IA", Board.WHITE, Player.TYPE_AI, difficulty);
        } else {
            player2 = new Player(player2Name, Board.WHITE);
        }

        // Lance la partie
        // À implémenter : transition vers l'écran de jeu

        // Replaced JOptionPane with JavaFX Alert
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.initOwner(mainStage); // Tells JavaFX which window this alert belongs to
        alert.setTitle("Partie Démarrée");
        alert.setHeaderText(null);
        alert.setContentText("Partie lancée: " + player1Name + " vs " + player2Name +
                (useAI ? " (IA - Niveau " + difficulty + ")" : ""));
        alert.showAndWait();
    }

    public void showRules() {
        // Replaced JOptionPane with JavaFX Alert
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.initOwner(mainStage);
        alert.setTitle("Règles du Jeu");
        alert.setHeaderText(null);
        alert.setContentText("Voir la boîte de dialogue des règles");
        alert.showAndWait();
    }
}
