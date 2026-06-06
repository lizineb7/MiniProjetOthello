package com.othello.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Gère l'état actuel d'une partie de Othello
 */
public class GameSession {

    public static final int STATUS_PLAYING = 1;
    public static final int STATUS_PAUSED = 2;
    public static final int STATUS_FINISHED = 3;

    private Board board;
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private int status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int totalMoves;
    private StringBuilder moveHistory;
    private Player winner;

    /**
     * Initialise une nouvelle partie
     */
    public GameSession(Player player1, Player player2) {
        this.board = new Board();
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = player1;  // Le joueur avec les noirs commence
        this.status = STATUS_PLAYING;
        this.startTime = LocalDateTime.now();
        this.totalMoves = 0;
        this.moveHistory = new StringBuilder();
        this.winner = null;

        // Initialise les scores
        updateScores();
    }

    /**
     * Met à jour les scores basés sur l'état actuel du plateau
     */
    public void updateScores() {
        player1.setScore(board.countPieces(player1.getColor()));
        player2.setScore(board.countPieces(player2.getColor()));
    }

    /**
     * Joue un coup
     * @return true si le coup est valide et effectué
     */
    public boolean playMove(int row, int col) {
        if (status != STATUS_PLAYING) {
            return false;
        }

        int capturedCount = board.placePiece(row, col, currentPlayer.getColor());

        if (capturedCount == -1) {
            return false;  // Coup invalide
        }

        // Enregistre le coup
        addToHistory(row, col, capturedCount);
        totalMoves++;
        updateScores();

        // Vérifie s'il y a des coups disponibles pour le prochain joueur
        Player nextPlayer = (currentPlayer == player1) ? player2 : player1;

        if (!board.hasValidMoves(nextPlayer.getColor())) {
            // Le prochain joueur ne peut pas jouer, revient au joueur actuel
            if (!board.hasValidMoves(currentPlayer.getColor())) {
                // Aucun joueur ne peut jouer → fin de partie
                endGame();
                return true;
            }
            // Le joueur actuel peut rejouer
            return true;
        }

        // Change de joueur
        currentPlayer = nextPlayer;
        return true;
    }

    /**
     * Passe le tour au joueur suivant
     */
    public boolean skipTurn() {
        Player nextPlayer = (currentPlayer == player1) ? player2 : player1;

        if (!board.hasValidMoves(nextPlayer.getColor())) {
            endGame();
            return false;
        }

        currentPlayer = nextPlayer;
        return true;
    }

    /**
     * Termine la partie et désigne le gagnant
     */
    public void endGame() {
        updateScores();
        status = STATUS_FINISHED;
        endTime = LocalDateTime.now();

        int score1 = player1.getScore();
        int score2 = player2.getScore();

        if (score1 > score2) {
            winner = player1;
        } else if (score2 > score1) {
            winner = player2;
        }
        // Sinon : égalité, winner reste null
    }

    /**
     * Ajoute un coup à l'historique
     */
    private void addToHistory(int row, int col, int capturedCount) {
        char colChar = (char) ('A' + col);
        int rowNum = row + 1;
        moveHistory.append(totalMoves + 1)
                .append(". ")
                .append(colChar)
                .append(rowNum)
                .append(" → ")
                .append(currentPlayer.getColorName())
                .append(" (capture ")
                .append(capturedCount)
                .append(")\n");
    }

    // Getters

    public Board getBoard() {
        return board;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public int getStatus() {
        return status;
    }

    public int getTotalMoves() {
        return totalMoves;
    }

    public String getMoveHistory() {
        return moveHistory.toString();
    }

    public Player getWinner() {
        return winner;
    }

    public boolean isGameFinished() {
        return status == STATUS_FINISHED;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getGameDuration() {
        if (endTime == null) {
            return "En cours...";
        }
        long seconds = java.time.temporal.ChronoUnit.SECONDS.between(startTime, endTime);
        long minutes = seconds / 60;
        long secs = seconds % 60;
        return String.format("%d:%02d", minutes, secs);
    }

    @Override
    public String toString() {
        return "GameSession{" +
                "player1=" + player1 +
                ", player2=" + player2 +
                ", currentPlayer=" + currentPlayer +
                ", totalMoves=" + totalMoves +
                '}';
    }
}
