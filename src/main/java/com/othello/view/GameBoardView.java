package com.othello.view;

import com.othello.model.Board;
import com.othello.model.GameSession;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import com.othello.ui.Constants;
import java.util.function.BiConsumer;

public class GameBoardView extends StackPane {

    private static final Color COLOR_BLACK = Color.web("#1a1a1a");
    private static final Color COLOR_WHITE = Color.web("#ffffff");
    private static final Color COLOR_ORANGE = Color.web("#f39c12");
    private static final Color COLOR_TEXT_SECONDARY = Color.web("#7f8c8d");

    private Canvas canvas;
    private GameSession gameSession;
    private Board board;

    private int cellSize = 55;
    private int boardStartX = 35;
    private int boardStartY = 35;

    private boolean[][] validMoves;
    private BiConsumer<Integer, Integer> onCellClick;

    public GameBoardView() {
        setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: transparent;");
        setPadding(new Insets(0));

        int totalDimension = (cellSize * Board.SIZE) + (boardStartX * 2);
        canvas = new Canvas(totalDimension, totalDimension);
        getChildren().add(canvas);

        canvas.setOnMouseClicked(this::handleBoardClick);
        canvas.setOnMouseMoved(this::handleMouseMove);
    }

    private void handleBoardClick(MouseEvent e) {
        int col = (int) ((e.getX() - boardStartX) / cellSize);
        int row = (int) ((e.getY() - boardStartY) / cellSize);

        if (col >= 0 && col < Board.SIZE && row >= 0 && row < Board.SIZE) {
            if (validMoves != null && validMoves[row][col]) {
                if (onCellClick != null) {
                    onCellClick.accept(row, col);
                }
            }
        }
    }

    private void handleMouseMove(MouseEvent e) {
        int col = (int) ((e.getX() - boardStartX) / cellSize);
        int row = (int) ((e.getY() - boardStartY) / cellSize);

        if (col >= 0 && col < Board.SIZE && row >= 0 && row < Board.SIZE) {
            if (validMoves != null && validMoves[row][col]) {
                getScene().setCursor(javafx.scene.Cursor.HAND);
                return;
            }
        }
        if (getScene() != null) {
            getScene().setCursor(javafx.scene.Cursor.DEFAULT);
        }
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        redraw();
    }

    public void redraw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        drawLabels(gc);
        drawBoard(gc);

        if (board != null) {
            drawPieces(gc);
        }

        if (validMoves != null) {
            drawValidMoves(gc);
        }
    }

    private void drawLabels(GraphicsContext gc) {
        gc.setFill(COLOR_TEXT_SECONDARY);
        gc.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 12));

        for (int i = 0; i < Board.SIZE; i++) {
            char letter = (char) ('A' + i);
            int x = boardStartX + i * cellSize + cellSize / 2;
            int y = boardStartY - 12;
            gc.fillText(String.valueOf(letter), x - 4, y);
        }

        for (int i = 0; i < Board.SIZE; i++) {
            int number = i + 1;
            int x = boardStartX - 18;
            int y = boardStartY + i * cellSize + cellSize / 2 + 4;
            gc.fillText(String.valueOf(number), x, y);
        }
    }

    private void drawBoard(GraphicsContext gc) {
        int boardSize = cellSize * Board.SIZE;

        gc.setFill(Color.web(Constants.BOARD_GREEN_HEX));
        gc.fillRect(boardStartX, boardStartY, boardSize, boardSize);

        gc.setStroke(Color.web(Constants.BOARD_FRAME_HEX));
        gc.setLineWidth(1.5);

        for (int i = 0; i <= Board.SIZE; i++) {
            int y = boardStartY + i * cellSize;
            gc.strokeLine(boardStartX, y, boardStartX + boardSize, y);

            int x = boardStartX + i * cellSize;
            gc.strokeLine(x, boardStartY, x, boardStartX + boardSize);
        }
    }

    private void drawPieces(GraphicsContext gc) {
        int pieceRadius = cellSize / 2 - 4;

        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                int piece = board.getPiece(i, j);

                if (piece != Board.EMPTY) {
                    int centerX = boardStartX + j * cellSize + cellSize / 2;
                    int centerY = boardStartY + i * cellSize + cellSize / 2;

                    if (piece == Board.BLACK) {
                        gc.setFill(COLOR_BLACK);
                        gc.fillOval(centerX - pieceRadius, centerY - pieceRadius, pieceRadius * 2, pieceRadius * 2);
                    } else {
                        gc.setFill(COLOR_WHITE);
                        gc.fillOval(centerX - pieceRadius, centerY - pieceRadius, pieceRadius * 2, pieceRadius * 2);
                        gc.setStroke(COLOR_BLACK);
                        gc.setLineWidth(1.5);
                        gc.strokeOval(centerX - pieceRadius, centerY - pieceRadius, pieceRadius * 2, pieceRadius * 2);
                    }
                }
            }
        }

        if (board.getLastMoveRow() >= 0 && board.getLastMoveCol() >= 0) {
            int lastRow = board.getLastMoveRow();
            int lastCol = board.getLastMoveCol();
            int x = boardStartX + lastCol * cellSize;
            int y = boardStartY + lastRow * cellSize;

            gc.setFill(new Color(243.0/255, 156.0/255, 18.0/255, 0.35));
            gc.fillRect(x + 1.5, y + 1.5, cellSize - 3, cellSize - 3);
        }
    }

    private void drawValidMoves(GraphicsContext gc) {
        gc.setFill(COLOR_ORANGE);
        int dotRadius = 5;

        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                if (validMoves[i][j]) {
                    int centerX = boardStartX + j * cellSize + cellSize / 2;
                    int centerY = boardStartY + i * cellSize + cellSize / 2;
                    gc.fillOval(centerX - dotRadius, centerY - dotRadius, dotRadius * 2, dotRadius * 2);
                }
            }
        }
    }

    public void setGameSession(GameSession gameSession) {
        this.gameSession = gameSession;
        this.board = gameSession.getBoard();
        redraw();
    }
    public void setValidMoves(boolean[][] validMoves) { this.validMoves = validMoves; redraw(); }
    public void setOnCellClickListener(BiConsumer<Integer, Integer> listener) { this.onCellClick = listener; }
    public void refreshBoard() { redraw(); }
}