package com.othello;

import com.othello.ui.GameWindow;
import javafx.application.Application;
import javafx.stage.Stage;
import com.othello.model.DatabaseDAO;

/**
 * Main application launcher entry point
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        DatabaseDAO.initializeDatabase();
        GameWindow gameWindow = new GameWindow(primaryStage);
        gameWindow.initialize();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}