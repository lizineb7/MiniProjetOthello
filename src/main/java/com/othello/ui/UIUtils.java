package com.othello.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class UIUtils {
    public static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public static void showRulesDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Règles d'Othello");
        alert.setHeaderText("Comment jouer ?");
        alert.setContentText("Les noirs commencent. À chaque tour, le joueur doit poser un pion de sa couleur sur une case vide adjacente à un pion adverse, de manière à prendre un ou plusieurs pions adverses en sandwich entre deux pions de sa couleur. Les pions capturés se retournent.");
        alert.showAndWait();
    }
}