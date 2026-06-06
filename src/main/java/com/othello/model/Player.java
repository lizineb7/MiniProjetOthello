package com.othello.model;
/**
 * Représente un joueur du jeu Othello
 */

public class Player {

    public static final int TYPE_HUMAN = 1;
    public static final int TYPE_AI = 2;

    private String name;
    private int color;        // BLACK ou WHITE
    private int type;         // HUMAN ou AI
    private int score;        // Nombre de pions
    private int difficulty;   // 1 = Facile, 2 = Moyen, 3 = Difficile (pour IA)

    /**
     * Constructeur pour un joueur humain
     */
    public Player(String name, int color) {
        this.name = name;
        this.color = color;
        this.type = TYPE_HUMAN;
        this.score = 0;
        this.difficulty = 1;
    }

    /**
     * Constructeur pour un joueur IA
     */
    public Player(String name, int color, int type, int difficulty) {
        this.name = name;
        this.color = color;
        this.type = type;
        this.score = 0;
        this.difficulty = difficulty;
    }

    // Getters et Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public int getType() {
        return type;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public boolean isAI() {
        return type == TYPE_AI;
    }

    public boolean isHuman() {
        return type == TYPE_HUMAN;
    }

    public String getColorName() {
        return color == Board.BLACK ? "Noir" : "Blanc";
    }

    @Override
    public String toString() {
        return name + " (" + getColorName() + ") - Score: " + score;
    }
}