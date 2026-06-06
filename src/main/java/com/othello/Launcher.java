package com.othello;

import javafx.application.Application;

public class Launcher {
    public static void main(String[] args) {
        // FIXED: Point to Main.class instead of HelloApplication.class
        Application.launch(Main.class, args);
    }
}