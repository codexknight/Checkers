package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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

    CheckersBoard board;


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

        board = new CheckersBoard();
        board.relocate(20,20);
        board.drawBoard();

        Pane root = new Pane();
        root.setPrefWidth(500);
        root.setPrefHeight(420);

        root.getChildren().addAll(board,newGameButton, resignButton, message);
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

    private static class CheckersMove {

    }


    private static class CheckersData {

        int[][] board;

        static final int
                EMPTY = 0,
                RED = 1,
                RED_KING = 2,
                BLACK = 3,
                BLACK_KING = 4;

        CheckersData() {
            board = new int[8][8];
            setUpGame();
        }

        private void setUpGame() {
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    if (r % 2 == c % 2) {
                        if (r < 3) {
                            board[r][c] = BLACK;
                        } else if (r > 4) {
                            board[r][c] = RED;
                        } else {
                            board[r][c] = EMPTY;
                        }

                    } else {
                        board[r][c] = EMPTY;
                    }
                }
            }
        }

        int pieceAt(int row, int col) {
            return board[row][col];
        }

    }


    private class CheckersBoard extends Canvas {

        CheckersData board;

        CheckersBoard() {
            super(324,324);
            board = new CheckersData();

        }

        public void drawBoard() {
            GraphicsContext g = getGraphicsContext2D();

            g.setStroke(Color.DARKRED);
            g.setLineWidth(2);
            g.strokeRect(1, 1, 322, 322);

            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (row % 2 == col % 2) {
                        g.setFill(Color.LIGHTGREY);

                    } else {
                        g.setFill(Color.GRAY);
                    }
                    g.fillRect(2+col*40,2+row*40, 40,40);

                    switch (board.pieceAt(row,col)) {
                        case CheckersData.RED:
                            g.setFill(Color.RED);
                            g.fillOval(8 + col*40, 8 + row*40, 28, 28);
                            break;
                        case CheckersData.BLACK:
                            g.setFill(Color.BLACK);
                            g.fillOval(8 + col*40, 8 + row*40, 28, 28);
                            break;
                        case CheckersData.RED_KING:
                            g.setFill(Color.RED);
                            g.fillOval(8 + col*40, 8 + row*40, 28, 28);
                            g.setFill(Color.WHITE);
                            g.fillText("K", 15 + col*40, 29 + row*40);
                            break;
                        case CheckersData.BLACK_KING:
                            g.setFill(Color.BLACK);
                            g.fillOval(8 + col*40, 8 + row*40, 28, 28);
                            g.setFill(Color.WHITE);
                            g.fillText("K", 15 + col*40, 29 + row*40);
                            break;
                    }

                }
            }

        }
    }

}