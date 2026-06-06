package com.othello.controller;

import com.othello.model.Board;
import com.othello.model.GameSession;
import com.othello.model.Player;
import com.othello.model.DatabaseDAO;
import com.othello.view.GameBoardView;
import com.othello.view.ScoreboardView;

// JavaFX Imports
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import java.util.Optional;

/**
 * Contrôleur principal du jeu
 * Fait le lien entre Model et View
 */
public class GameController {

    private GameSession gameSession;
    private GameBoardView boardView;
    private ScoreboardView scoreboardView;
    private AIController aiController;
    private Stage parentStage; // Replaced JFrame with Stage

    private boolean gameRunning = false;
    private boolean waitingForAI = false;

    // Updated constructor
    public GameController(Stage parentStage) {
        this.parentStage = parentStage;
    }

    /**
     * Démarre une nouvelle partie
     */
    public void startNewGame(Player player1, Player player2,
                             GameBoardView boardView, ScoreboardView scoreboardView) {
        this.gameSession = new GameSession(player1, player2);
        this.boardView = boardView;
        this.scoreboardView = scoreboardView;
        this.aiController = new AIController(gameSession);
        this.gameRunning = true;
        this.waitingForAI = false;

        // Configure les listeners
        boardView.setGameSession(gameSession);
        boardView.setOnCellClickListener(this::handleCellClick);

        // Affiche les coups valides
        updateValidMoves();
        updateScoreboard();

        // Si le premier joueur est l'IA
        if (player1.isAI()) {
            playAIMove();
        }
    }

    /**
     * Gère un clic sur une case de la grille
     */
    private void handleCellClick(int row, int col) {
        if (!gameRunning || waitingForAI) {
            return;
        }

        // Le joueur actuel doit être humain
        if (gameSession.getCurrentPlayer().isAI()) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(parentStage);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("C'est au tour de l'IA de jouer");
            alert.showAndWait();
            return;
        }

        // Essaie de jouer le coup
        if (gameSession.playMove(row, col)) {
            boardView.refreshBoard();
            updateValidMoves();
            updateScoreboard();

            // Vérifie si la partie est finie
            if (gameSession.isGameFinished()) {
                endGame();
                return;
            }

            // Passe au joueur suivant
            if (gameSession.getCurrentPlayer().isAI()) {
                playAIMove();
            }
        } else {
            showToast("Coup invalide!");
        }
    }

    /**
     * Joue un coup pour l'IA
     */
    private void playAIMove() {
        waitingForAI = true;

        // Lance l'IA dans un thread séparé pour ne pas bloquer l'UI
        new Thread(() -> {
            try {
                Thread.sleep(1000);  // Simule du temps de réflexion

                int[] move = aiController.findBestMove(
                        gameSession.getCurrentPlayer().getDifficulty());

                if (move != null) {
                    // CRITICAL: Replaced SwingUtilities.invokeLater with Platform.runLater
                    Platform.runLater(() -> {
                        if (gameSession.playMove(move[0], move[1])) {
                            boardView.refreshBoard();
                            updateValidMoves();
                            updateScoreboard();

                            if (gameSession.isGameFinished()) {
                                endGame();
                                return;
                            }

                            // Si le prochain joueur est aussi l'IA
                            if (gameSession.getCurrentPlayer().isAI()) {
                                playAIMove();
                            } else {
                                waitingForAI = false;
                            }
                        }
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Met à jour l'affichage des coups valides
     */
    private void updateValidMoves() {
        boolean[][] validMoves = gameSession.getBoard()
                .getValidMoves(gameSession.getCurrentPlayer().getColor());
        boardView.setValidMoves(validMoves);
    }

    /**
     * Met à jour le scoreboard et l'historique
     */
    private void updateScoreboard() {
        scoreboardView.updateDisplay(gameSession);
    }

    /**
     * Termine la partie
     */
    private void endGame() {
        gameRunning = false;
        gameSession.endGame();

        // Sauvegarde la partie
        DatabaseDAO.saveGame(gameSession);

        // Affiche le résultat
        String message;
        if (gameSession.getWinner() == null) {
            message = String.format("Égalité! %d - %d",
                    gameSession.getPlayer1().getScore(),
                    gameSession.getPlayer2().getScore());
        } else {
            message = String.format("🏆 %s a remporté la partie! %d - %d",
                    gameSession.getWinner().getName(),
                    gameSession.getWinner().getScore(),
                    gameSession.getWinner() == gameSession.getPlayer1() ?
                            gameSession.getPlayer2().getScore() :
                            gameSession.getPlayer1().getScore());
        }

        // Replaced JOptionPane.showOptionDialog with a custom JavaFX Alert
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initOwner(parentStage);
        alert.setTitle("Partie Terminée");
        alert.setHeaderText(null);
        alert.setContentText(message + "\n\nVoulez-vous jouer une autre partie?");

        // Setup Custom buttons (Oui / Non) instead of OK/Cancel
        ButtonType buttonOui = new ButtonType("Oui");
        ButtonType buttonNon = new ButtonType("Non");
        alert.getButtonTypes().setAll(buttonOui, buttonNon);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonOui) {
            // À implémenter : retour au menu ou relance
            // parentStage is your active JavaFX Stage window context here
        }
    }

    /**
     * Affiche une notification temporaire
     */
    private void showToast(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.initOwner(parentStage);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public GameSession getGameSession() {
        return gameSession;
    }

    public boolean isGameRunning() {
        return gameRunning;
    }

    public void undoLastMove() {
        if (gameSession != null && gameSession.getTotalMoves() > 0) {
            showToast("Undo non encore implémenté");
        }
    }

    public void restartGame(Player player1, Player player2) {
        startNewGame(player1, player2, boardView, scoreboardView);
    }
}
