module com.othello {

    // Required modules
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;

    // Exported packages
    exports com.othello;
    exports com.othello.controller;
    exports com.othello.model;
    exports com.othello.view;

    // Opened packages for reflection
    opens com.othello to javafx.fxml;
    opens com.othello.controller to javafx.fxml;
    opens com.othello.model to javafx.fxml;
    opens com.othello.view to javafx.fxml;
}