package com.othello.ui;

public class Styles {
    public static final String SURFACE_BACKGROUND =
            "-fx-background-color: " + Constants.BACKGROUND_HEX + ";";

    public static final String HEADER_STYLE =
            "-fx-background-color: " + Constants.SURFACE_HEX + ";" +
                    "-fx-border-color: #e0e0e0;" +
                    "-fx-border-width: 0 0 1 0;";

    public static final String BOTTOM_NAV_STYLE =
            "-fx-background-color: " + Constants.SURFACE_HEX + ";" +
                    "-fx-border-color: #e0e0e0;" +
                    "-fx-border-width: 1 0 0 0;";

    public static final String CARD_STYLE =
            "-fx-background-color: " + Constants.SURFACE_HEX + ";" +
                    "-fx-background-radius: 12;" +
                    "-fx-border-radius: 12;" +
                    "-fx-border-color: #e0e0e0;" +
                    "-fx-border-width: 1;" +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.03), 10, 0, 0, 4);";

    public static final String LABEL_TITLE =
            "-fx-font-family: '" + Constants.FONT_FAMILY + "';" +
                    "-fx-font-size: " + Constants.TITLE_SIZE + ";" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: " + Constants.PRIMARY_HEX + ";";

    public static final String LABEL_SECTION_HEADING =
            "-fx-font-family: '" + Constants.FONT_FAMILY + "';" +
                    "-fx-font-size: " + Constants.SECTION_HEADING_SIZE + ";" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #1a1a1a;";

    public static final String LABEL_FIELD =
            "-fx-font-family: '" + Constants.FONT_FAMILY + "';" +
                    "-fx-font-size: " + Constants.BODY_MD_SIZE + ";" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #495057;";

    public static final String LABEL_SECONDARY =
            "-fx-font-family: '" + Constants.FONT_FAMILY + "';" +
                    "-fx-font-size: " + Constants.BODY_MD_SIZE + ";" +
                    "-fx-text-fill: #6c757d;";

    public static final String TEXT_INPUT =
            "-fx-background-color: #f1f3f5;" +
                    "-fx-background-radius: 8;" +
                    "-fx-border-radius: 8;" +
                    "-fx-border-color: #ced4da;" +
                    "-fx-border-width: 1;" +
                    "-fx-padding: 10;" +
                    "-fx-font-family: '" + Constants.FONT_FAMILY + "';" +
                    "-fx-font-size: " + Constants.BODY_MD_SIZE + ";";

    public static final String COMBOBOX =
            "-fx-background-color: #f1f3f5;" +
                    "-fx-background-radius: 8;" +
                    "-fx-border-radius: 8;" +
                    "-fx-border-color: #ced4da;" +
                    "-fx-border-width: 1;" +
                    "-fx-padding: 4 8;";

    public static final String BUTTON_PRIMARY =
            "-fx-background-color: " + Constants.PRIMARY_HEX + ";" +
                    "-fx-text-fill: white;" +
                    "-fx-font-family: '" + Constants.FONT_FAMILY + "';" +
                    "-fx-font-size: " + Constants.BODY_MD_SIZE + ";" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-radius: 8;" +
                    "-fx-padding: 10 24;" +
                    "-fx-cursor: hand;";

    public static final String BUTTON_PRIMARY_HOVER =
            "-fx-background-color: #1b5e20;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-family: '" + Constants.FONT_FAMILY + "';" +
                    "-fx-font-size: " + Constants.BODY_MD_SIZE + ";" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-radius: 8;" +
                    "-fx-padding: 10 24;" +
                    "-fx-cursor: hand;";

    public static final String BUTTON_SECONDARY =
            "-fx-background-color: transparent;" +
                    "-fx-text-fill: #495057;" +
                    "-fx-border-color: #ced4da;" +
                    "-fx-border-width: 1;" +
                    "-fx-border-radius: 8;" +
                    "-fx-background-radius: 8;" +
                    "-fx-font-family: '" + Constants.FONT_FAMILY + "';" +
                    "-fx-font-size: " + Constants.BODY_MD_SIZE + ";" +
                    "-fx-padding: 10 24;" +
                    "-fx-cursor: hand;";

    public static final String BUTTON_SECONDARY_HOVER =
            "-fx-background-color: #e9ecef;" +
                    "-fx-text-fill: #495057;" +
                    "-fx-border-color: #adb5bd;" +
                    "-fx-border-width: 1;" +
                    "-fx-border-radius: 8;" +
                    "-fx-background-radius: 8;" +
                    "-fx-font-family: '" + Constants.FONT_FAMILY + "';" +
                    "-fx-font-size: " + Constants.BODY_MD_SIZE + ";" +
                    "-fx-padding: 10 24;" +
                    "-fx-cursor: hand;";
}