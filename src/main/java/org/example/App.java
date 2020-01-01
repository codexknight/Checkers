package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    private Button newGameButton;
    private Button resignButton;

    private Label message;


    @Override
    public void start(Stage stage) {

        newGameButton = new Button("New Game");
        newGameButton.relocate(370,120);
        newGameButton.setManaged(false);
        newGameButton.resize(100,30);

        resignButton = new Button("Resign");
        resignButton.relocate(370,200);
        resignButton.setManaged(false);
        resignButton.resize(100,30);

        message = new Label("Click \"New Game\" to begin");
        message.relocate(20,370);
        message.setTextFill(Color.rgb(100,255,100));
        message.setFont(Font.font(null, FontWeight.BOLD, 18));

        Pane root = new Pane();
        root.setPrefWidth(500);
        root.setPrefHeight(420);

        root.getChildren().addAll(newGameButton, resignButton, message);
        root.setStyle("-fx-background-color:darkgreen;" +
                "-fx-border-color:darkred; -fx-border-width:3px");
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Checkers!");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}