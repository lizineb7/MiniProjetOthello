package com.othello.ui;

import com.othello.controller.GameController;
import com.othello.controller.MenuController;
import com.othello.model.Player;
import com.othello.ui.screens.GameScreen;
import com.othello.ui.screens.SetupScreen;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GameWindow {

    private Stage stage;
    private MenuController menuController;
    private GameController gameController;

    private Scene setupScene;
    private Scene gameScene;

    private double xOffset = 0;
    private double yOffset = 0;

    public GameWindow(Stage stage) {
        this.stage = stage;
        this.menuController = new MenuController(stage);
        this.gameController = new GameController(stage);

        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("🎮 OTHELLO GAME");

        // Let the Scene handle default dimension rendering maps
        stage.setMinWidth(900);
        stage.setMinHeight(600);

        stage.setResizable(true);
    }

    public void initialize() {
        showSetupScreen();
    }

    public void showSetupScreen() {
        SetupScreen setupScreen = new SetupScreen();
        if (setupScreen.getLeaderboardTable() != null) {
            setupScreen.getLeaderboardTable().getItems().clear();

            java.util.List<java.util.Map<String, Object>> topPlayersData = com.othello.model.DatabaseDAO.getTopPlayers(10);
            int rank = 1;
            for (java.util.Map<String, Object> playerData : topPlayersData) {
                String name = (String) playerData.get("name");
                int totalGames = (Integer) playerData.get("totalGames");
                int wins = (Integer) playerData.get("wins");
                setupScreen.getLeaderboardTable().getItems().add(new SetupScreen.LeaderboardEntry(rank++, name, totalGames, wins));
            }
        }

        makeWindowDraggable(setupScreen, stage);

        // Wire Up Custom Active Native Actions
        if (setupScreen.getCloseButton() != null) {
            setupScreen.getCloseButton().setOnAction(e -> {
                javafx.application.Platform.exit();
                System.exit(0);
            });
        }
        if (setupScreen.getMinimizeButton() != null) {
            setupScreen.getMinimizeButton().setOnAction(e -> stage.setIconified(true));
        }

        if (setupScreen.getMaximizeButton() != null) {
            setupScreen.getMaximizeButton().setOnAction(e -> {
                if (stage.isMaximized()) {
                    stage.setMaximized(false);
                    setupScreen.getMaximizeButton().setText("🗖");
                } else {
                    stage.setMaximized(true);
                    setupScreen.getMaximizeButton().setText("🗗");
                }
            });
        }

        setupScreen.setOnPlayButtonPressed(() -> {
            String player1Name = setupScreen.getPlayer1Name();
            String player2Name = setupScreen.getPlayer2Name();
            boolean useAI = setupScreen.isAISelected();
            int difficulty = setupScreen.getDifficulty();

            Object[] players = menuController.createPlayers(player1Name, player2Name, useAI, difficulty);
            Player player1 = (Player) players[0];
            Player player2 = (Player) players[1];

            showGameScreen(player1, player2);
        });

        setupScreen.setOnRulesButtonPressed(UIUtils::showRulesDialog);

        setupScene = new Scene(setupScreen, 1280, 720);
        stage.setScene(setupScene);
        stage.centerOnScreen();
    }

    public void showGameScreen(Player player1, Player player2) {
        GameScreen gameScreen = new GameScreen();
        makeWindowDraggable(gameScreen, stage);

        if (gameScreen.getCloseButton() != null) {
            gameScreen.getCloseButton().setOnAction(e -> {
                javafx.application.Platform.exit();
                System.exit(0);
            });
        }
        if (gameScreen.getMinimizeButton() != null) {
            gameScreen.getMinimizeButton().setOnAction(e -> stage.setIconified(true));
        }
        if (gameScreen.getMaximizeButton() != null) {
            gameScreen.getMaximizeButton().setOnAction(e -> {
                if (stage.isMaximized()) {
                    stage.setMaximized(false);
                    gameScreen.getMaximizeButton().setText("🗖");
                } else {
                    stage.setMaximized(true);
                    gameScreen.getMaximizeButton().setText("🗗");
                }
            });
        }

        gameScreen.setOnBackButtonPressed(this::showSetupScreen);
        gameScreen.setOnMenuButtonPressed(this::showSetupScreen);

        // Restarts the game state clean
        gameScreen.getRestartButton().setOnAction(e -> {
            gameController.startNewGame(player1, player2,
                    gameScreen.getBoardView(), gameScreen.getScoreboardView());
        });

        gameController.startNewGame(player1, player2,
                gameScreen.getBoardView(), gameScreen.getScoreboardView());

        gameScene = new Scene(gameScreen, 1280, 720);
        stage.setScene(gameScene);
    }

    private void makeWindowDraggable(javafx.scene.layout.Pane pane, Stage stage) {
        pane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        pane.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    public Stage getStage() {
        return stage;
    }
}