package com.othello.ui.screens;

import com.othello.ui.Constants;
import com.othello.ui.Styles;
import com.othello.view.GameBoardView;
import com.othello.view.ScoreboardView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Game Screen
 */
public class GameScreen extends BorderPane {

    private GameBoardView boardView;
    private ScoreboardView scoreboardView;

    private Button closeButton;
    private Button maximizeButton;
    private Button minimizeButton;
    private Button backArrowButton;
    private Button restartBtn;

    private Runnable onBackButtonPressed;
    private Runnable onMenuButtonPressed;

    public GameScreen() {
        setStyle(Styles.SURFACE_BACKGROUND);

        // Use BorderPane for proper layout management
        setTop(createHeader());
        setCenter(createCenterContent());
        setBottom(createBottomNav());
    }

    // ============================================================
    // HEADER
    // ============================================================

    private VBox createHeader() {
        VBox header = new VBox();
        header.setStyle(Styles.HEADER_STYLE);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPrefHeight(70);

        HBox headerContent = new HBox();
        headerContent.setAlignment(Pos.CENTER_LEFT);
        headerContent.setSpacing(Constants.SPACING_16);
        headerContent.setPadding(new Insets(Constants.SPACING_16, Constants.SPACING_24, Constants.SPACING_16, Constants.SPACING_24));

        // Back Arrow Button to return to the previous screen
        backArrowButton = new Button("←");
        backArrowButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #1a1a1a; -fx-font-size: 20px; -fx-cursor: hand; -fx-font-weight: bold; -fx-padding: 0 10 0 0;");
        backArrowButton.setOnAction(e -> { if (onBackButtonPressed != null) onBackButtonPressed.run(); });
        backArrowButton.setOnMouseEntered(e -> backArrowButton.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Constants.PRIMARY_HEX + "; -fx-font-size: 20px; -fx-cursor: hand; -fx-font-weight: bold; -fx-padding: 0 10 0 0;"));
        backArrowButton.setOnMouseExited(e -> backArrowButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #1a1a1a; -fx-font-size: 20px; -fx-cursor: hand; -fx-font-weight: bold; -fx-padding: 0 10 0 0;"));

        Label title = new Label("OTHELLO GAME");
        title.setStyle(Styles.LABEL_TITLE);

        javafx.scene.layout.Region spacer = new javafx.scene.layout.Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox windowControls = new HBox(12);
        windowControls.setAlignment(Pos.CENTER_RIGHT);

        minimizeButton = new Button("—");
        minimizeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #5f6368; -fx-font-size: 18px; -fx-cursor: hand; -fx-font-weight: bold; -fx-padding: 5;");

        // Maximize button to complete the window management buttons
        maximizeButton = new Button("🗖");
        maximizeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #5f6368; -fx-font-size: 14px; -fx-cursor: hand; -fx-padding: 5;");

        closeButton = new Button("✕");
        closeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #5f6368; -fx-font-size: 18px; -fx-cursor: hand; -fx-font-weight: bold; -fx-padding: 5;");

        closeButton.setOnMouseEntered(e -> closeButton.setStyle("-fx-background-color: #e81123; -fx-text-fill: white; -fx-font-size: 18px; -fx-cursor: hand; -fx-padding: 5;"));
        closeButton.setOnMouseExited(e -> closeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #5f6368; -fx-font-size: 18px; -fx-cursor: hand; -fx-padding: 5;"));

        // Put the maximize button in the center slot
        windowControls.getChildren().addAll(minimizeButton, maximizeButton, closeButton);

        // Inserted backArrowButton right before the title
        headerContent.getChildren().addAll(backArrowButton, title, spacer, windowControls);
        header.getChildren().add(headerContent);
        return header;
    }

    // ============================================================
    // CENTER CONTENT
    // ============================================================

    private StackPane createCenterContent() {
        StackPane centerWrapper = new StackPane();
        centerWrapper.setStyle(Styles.SURFACE_BACKGROUND);
        centerWrapper.setAlignment(Pos.CENTER);

        HBox content = new HBox();
        content.setSpacing(Constants.SPACING_32);
        content.setPadding(new Insets(24));
        content.setAlignment(Pos.CENTER);

        // Board - RESPONSIVE
        boardView = new GameBoardView();
        boardView.setMaxWidth(Double.MAX_VALUE);  // ← Can grow infinitely
        boardView.setMaxHeight(Double.MAX_VALUE); // ← Can grow infinitely
        HBox.setHgrow(boardView, Priority.ALWAYS); // ← GROW with window

        // Sidebar - RESPONSIVE
        scoreboardView = new ScoreboardView();
        scoreboardView.setStyle(Styles.CARD_STYLE);
        scoreboardView.setMaxWidth(Double.MAX_VALUE);  // ← Can grow
        scoreboardView.setMaxHeight(Double.MAX_VALUE); // ← Can grow
        HBox.setHgrow(scoreboardView, Priority.ALWAYS); // ← GROW with window

        content.getChildren().addAll(boardView, scoreboardView);
        HBox.setHgrow(boardView, Priority.ALWAYS);    // Board grows
        HBox.setHgrow(scoreboardView, Priority.ALWAYS); // Sidebar grows

        centerWrapper.getChildren().add(content);
        return centerWrapper;
    }

    // ============================================================
    // FOOTER
    // ============================================================

    private HBox createBottomNav() {
        HBox nav = new HBox();
        nav.setStyle(Styles.BOTTOM_NAV_STYLE);
        nav.setAlignment(Pos.CENTER);
        nav.setSpacing(Constants.SPACING_12);
        nav.setPadding(new Insets(Constants.SPACING_12));
        nav.setPrefHeight(60);  // footer height

        restartBtn = createNavButton("⟲ Restart");

        Button menuBtn = createNavButton("☰ Menu");
        menuBtn.setOnAction(e -> { if (onMenuButtonPressed != null) onMenuButtonPressed.run(); });

        nav.getChildren().addAll(restartBtn, menuBtn);
        return nav;
    }

    private Button createNavButton(String text) {
        Button btn = new Button(text);
        String normalStyle = "-fx-padding: 8; -fx-background-color: transparent; -fx-text-fill: #1a1a1a; -fx-font-family: '" + Constants.FONT_FAMILY + "'; -fx-font-size: " + Constants.BODY_MD_SIZE + "; -fx-border-radius: 12; -fx-cursor: hand;";
        btn.setStyle(normalStyle);

        btn.setOnMouseEntered(e -> btn.setStyle("-fx-padding: 8; -fx-background-color: #e8f5e9; -fx-text-fill: #1a1a1a; -fx-font-family: '" + Constants.FONT_FAMILY + "'; -fx-font-size: " + Constants.BODY_MD_SIZE + "; -fx-border-radius: 12;"));
        btn.setOnMouseExited(e -> btn.setStyle(normalStyle));
        return btn;
    }

    // ============================================================
    // GETTERS
    // ============================================================

    public GameBoardView getBoardView() { return boardView; }
    public ScoreboardView getScoreboardView() { return scoreboardView; }
    public Button getCloseButton() { return closeButton; }
    public Button getMaximizeButton() { return maximizeButton; }
    public Button getMinimizeButton() { return minimizeButton; }
    public Button getRestartButton() { return restartBtn; }

    // ============================================================
    // EVENT HANDLERS
    // ============================================================

    public void setOnBackButtonPressed(Runnable handler) { this.onBackButtonPressed = handler; }
    public void setOnMenuButtonPressed(Runnable handler) { this.onMenuButtonPressed = handler; }
}