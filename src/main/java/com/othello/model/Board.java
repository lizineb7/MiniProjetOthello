package com.othello.model;
/**
 * Gère la grille 8x8 du jeu Othello
 * Représente l'état des cases et gère le retournement des pions
 */

public class Board {

    public static final int SIZE = 8;
    public static final int EMPTY = 0;
    public static final int BLACK = 1;
    public static final int WHITE = 2;

    private int[][] grid;
    private int lastMoveRow = -1;
    private int lastMoveCol = -1;

    /**
     * Initialise la grille avec la position standard d'ouverture
     */
    public Board() {
        grid = new int[SIZE][SIZE];
        initializeBoard();
    }

    /**
     * Position initiale : pions au centre (D4, D5, E4, E5)
     */
    private void initializeBoard() {
        // Réinitialise tout à vide
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                grid[i][j] = EMPTY;
            }
        }

        // Position standard d'ouverture
        // Indices : [ligne][colonne] où (0,0) = A1
        grid[3][3] = WHITE;  // D4
        grid[3][4] = BLACK;  // E4
        grid[4][3] = BLACK;  // D5
        grid[4][4] = WHITE;  // E5
    }

    /**
     * Vérifie si un coup est valide pour un joueur
     * @param row Ligne (0-7)
     * @param col Colonne (0-7)
     * @param player Couleur du joueur (BLACK ou WHITE)
     * @return true si le coup capture au moins un pion adverse
     */
    public boolean isValidMove(int row, int col, int player) {
        // La case doit être vide
        if (grid[row][col] != EMPTY) {
            return false;
        }

        // Vérifie dans les 8 directions si ce coup capture des pions
        int[][] directions = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1},           {0, 1},
                {1, -1},  {1, 0},  {1, 1}
        };

        for (int[] dir : directions) {
            if (canCapture(row, col, player, dir[0], dir[1])) {
                return true;
            }
        }

        return false;
    }

    /**
     * Vérifie si on peut capturer des pions dans une direction donnée
     */
    private boolean canCapture(int row, int col, int player, int dirRow, int dirCol) {
        int opponent = (player == BLACK) ? WHITE : BLACK;
        int nextRow = row + dirRow;
        int nextCol = col + dirCol;

        // Au moins un pion adverse doit être adjacent
        if (!isInBounds(nextRow, nextCol) || grid[nextRow][nextCol] != opponent) {
            return false;
        }

        // Continue jusqu'à trouver un pion du joueur ou une case vide
        nextRow += dirRow;
        nextCol += dirCol;

        while (isInBounds(nextRow, nextCol)) {
            if (grid[nextRow][nextCol] == player) {
                return true;  // Capture possible
            }
            if (grid[nextRow][nextCol] == EMPTY) {
                return false;  // Aucune capture possible
            }
            nextRow += dirRow;
            nextCol += dirCol;
        }

        return false;
    }

    /**
     * Effectue un coup : place un pion et retourne les pions capturés
     * @return Nombre de pions capturés
     */
    public int placePiece(int row, int col, int player) {
        if (!isValidMove(row, col, player)) {
            return -1;  // Coup invalide
        }

        grid[row][col] = player;
        lastMoveRow = row;
        lastMoveCol = col;

        int capturedCount = 0;

        // Retourne les pions dans les 8 directions
        int[][] directions = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1},           {0, 1},
                {1, -1},  {1, 0},  {1, 1}
        };

        for (int[] dir : directions) {
            capturedCount += flipPiecesInDirection(row, col, player, dir[0], dir[1]);
        }

        return capturedCount;
    }

    /**
     * Retourne tous les pions adverses dans une direction donnée
     */
    private int flipPiecesInDirection(int row, int col, int player, int dirRow, int dirCol) {
        int opponent = (player == BLACK) ? WHITE : BLACK;
        int nextRow = row + dirRow;
        int nextCol = col + dirCol;
        int flippedCount = 0;

        // Compte les pions à retourner
        while (isInBounds(nextRow, nextCol) && grid[nextRow][nextCol] == opponent) {
            flippedCount++;
            nextRow += dirRow;
            nextCol += dirCol;
        }

        // S'il n'y a pas de pion du joueur à la fin, aucun retournement
        if (!isInBounds(nextRow, nextCol) || grid[nextRow][nextCol] != player) {
            return 0;
        }

        // Effectue le retournement
        nextRow = row + dirRow;
        nextCol = col + dirCol;
        while (isInBounds(nextRow, nextCol) && grid[nextRow][nextCol] == opponent) {
            grid[nextRow][nextCol] = player;
            nextRow += dirRow;
            nextCol += dirCol;
        }

        return flippedCount;
    }

    /**
     * Obtient toutes les cases valides pour un joueur
     */
    public boolean[][] getValidMoves(int player) {
        boolean[][] validMoves = new boolean[SIZE][SIZE];

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                validMoves[i][j] = isValidMove(i, j, player);
            }
        }

        return validMoves;
    }

    /**
     * Vérifie si un joueur a des coups disponibles
     */
    public boolean hasValidMoves(int player) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (isValidMove(i, j, player)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Compte les pions pour chaque couleur
     */
    public int countPieces(int player) {
        int count = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j] == player) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Obtient la valeur d'une case
     */
    public int getPiece(int row, int col) {
        if (isInBounds(row, col)) {
            return grid[row][col];
        }
        return EMPTY;
    }

    /**
     * Vérifie si les coordonnées sont dans les limites
     */
    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }

    /**
     * Obtient la dernière position jouée
     */
    public int getLastMoveRow() {
        return lastMoveRow;
    }

    public int getLastMoveCol() {
        return lastMoveCol;
    }

    /**
     * Crée une copie profonde de la grille (utile pour l'IA)
     */
    public Board deepCopy() {
        Board copy = new Board();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                copy.grid[i][j] = this.grid[i][j];
            }
        }
        copy.lastMoveRow = this.lastMoveRow;
        copy.lastMoveCol = this.lastMoveCol;
        return copy;
    }

    /**
     * Réinitialise la grille
     */
    public void reset() {
        initializeBoard();
        lastMoveRow = -1;
        lastMoveCol = -1;
    }

    /**
     * Affichage pour déboguer
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("    A   B   C   D   E   F   G   H\n");
        for (int i = 0; i < SIZE; i++) {
            sb.append((i + 1)).append(" ");
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j] == BLACK) {
                    sb.append(" ● ");
                } else if (grid[i][j] == WHITE) {
                    sb.append(" ○ ");
                } else {
                    sb.append(" · ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
