package com.othello.controller;

import com.othello.model.Board;
import com.othello.model.GameSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Contrôleur pour l'IA Othello
 * Implémente différentes stratégies selon le niveau de difficulté
 */
public class AIController {

    private GameSession gameSession;
    private Random random;
    private static final int MAX_DEPTH = 6;  // Profondeur maximale pour minimax

    public AIController(GameSession gameSession) {
        this.gameSession = gameSession;
        this.random = new Random();
    }

    /**
     * Trouve le meilleur coup pour l'IA
     * @param difficulty 1 = Facile, 2 = Moyen, 3 = Difficile
     */
    public int[] findBestMove(int difficulty) {
        Board board = gameSession.getBoard();
        int aiColor = gameSession.getCurrentPlayer().getColor();

        // Récupère tous les coups valides
        List<int[]> validMoves = getValidMoves(board, aiColor);

        if (validMoves.isEmpty()) {
            return null;  // Aucun coup disponible
        }

        switch (difficulty) {
            case 1:  // Facile : coup aléatoire
                return validMoves.get(random.nextInt(validMoves.size()));

            case 2:  // Moyen : stratégie simple
                return findMediumMove(board, validMoves, aiColor);

            case 3:  // Difficile : minimax
                return findDifficultMove(board, validMoves, aiColor);

            default:
                return validMoves.get(random.nextInt(validMoves.size()));
        }
    }

    /**
     * Récupère tous les coups valides
     */
    private List<int[]> getValidMoves(Board board, int color) {
        List<int[]> moves = new ArrayList<>();

        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                if (board.isValidMove(i, j, color)) {
                    moves.add(new int[]{i, j});
                }
            }
        }

        return moves;
    }

    /**
     * Stratégie moyenne : préfère les coins et les bordures
     */
    private int[] findMediumMove(Board board, List<int[]> validMoves, int aiColor) {
        int[] bestMove = null;
        int bestScore = -1;

        for (int[] move : validMoves) {
            int score = evaluatePosition(move[0], move[1]);

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        return bestMove != null ? bestMove : validMoves.get(0);
    }

    /**
     * Évalue la valeur stratégique d'une position
     * Les coins sont les plus importants, puis les bordures
     */
    private int evaluatePosition(int row, int col) {
        // Matrice de poids (stratégie classique)
        int[][] weights = {
                {100, -20,  10,   5,   5,  10, -20, 100},
                {-20, -50,  -2,  -2,  -2,  -2, -50, -20},
                { 10,  -2,   5,   1,   1,   5,  -2,  10},
                {  5,  -2,   1,   1,   1,   1,  -2,   5},
                {  5,  -2,   1,   1,   1,   1,  -2,   5},
                { 10,  -2,   5,   1,   1,   5,  -2,  10},
                {-20, -50,  -2,  -2,  -2,  -2, -50, -20},
                {100, -20,  10,   5,   5,  10, -20, 100}
        };

        return weights[row][col];
    }

    /**
     * Stratégie difficile : algorithme minimax avec élagage alpha-bêta
     */
    private int[] findDifficultMove(Board board, List<int[]> validMoves, int aiColor) {
        int[] bestMove = validMoves.get(0);
        int bestScore = Integer.MIN_VALUE;

        for (int[] move : validMoves) {
            Board copy = board.deepCopy();
            copy.placePiece(move[0], move[1], aiColor);

            int score = minimax(copy, MAX_DEPTH, false, aiColor,
                    Integer.MIN_VALUE, Integer.MAX_VALUE);

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        return bestMove;
    }

    /**
     * Algorithme minimax avec élagage alpha-bêta
     */
    private int minimax(Board board, int depth, boolean isMaximizing,
                        int aiColor, int alpha, int beta) {
        if (depth == 0 || isGameOver(board)) {
            return evaluateBoard(board, aiColor);
        }

        int opponent = (aiColor == Board.BLACK) ? Board.WHITE : Board.BLACK;
        int currentColor = isMaximizing ? aiColor : opponent;
        List<int[]> moves = getValidMoves(board, currentColor);

        if (moves.isEmpty()) {
            // Le joueur ne peut pas jouer, vérifie l'autre couleur
            int otherColor = (currentColor == Board.BLACK) ? Board.WHITE : Board.BLACK;
            if (!getValidMoves(board, otherColor).isEmpty()) {
                // L'autre joueur peut jouer
                return minimax(board, depth - 1, !isMaximizing, aiColor, alpha, beta);
            } else {
                // Personne ne peut jouer, fin du jeu
                return evaluateBoard(board, aiColor);
            }
        }

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (int[] move : moves) {
                Board copy = board.deepCopy();
                copy.placePiece(move[0], move[1], currentColor);
                int eval = minimax(copy, depth - 1, false, aiColor, alpha, beta);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) break;  // Élagage
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int[] move : moves) {
                Board copy = board.deepCopy();
                copy.placePiece(move[0], move[1], currentColor);
                int eval = minimax(copy, depth - 1, true, aiColor, alpha, beta);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) break;  // Élagage
            }
            return minEval;
        }
    }

    /**
     * Évalue l'état du plateau
     */
    private int evaluateBoard(Board board, int aiColor) {
        int aiCount = board.countPieces(aiColor);
        int opponentColor = (aiColor == Board.BLACK) ? Board.WHITE : Board.BLACK;
        int opponentCount = board.countPieces(opponentColor);

        return (aiCount - opponentCount) * 100;
    }

    /**
     * Vérifie si la partie est terminée
     */
    private boolean isGameOver(Board board) {
        return !getValidMoves(board, Board.BLACK).isEmpty() ||
                !getValidMoves(board, Board.WHITE).isEmpty();
    }
}