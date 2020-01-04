package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;

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
        newGameButton.setOnAction(e -> board.doNewGame());

        resignButton = new Button("Resign");
        resignButton.relocate(370,200);
        resignButton.setManaged(false);
        resignButton.resize(100,30);
        resignButton.setOnAction(e->board.doResign());

        message = new Label("Click \"New Game\" to begin");
        message.relocate(20,370);
        message.setTextFill(Color.rgb(100,255,100));
        message.setFont(Font.font(null, FontWeight.BOLD, 18));

        board = new CheckersBoard();
        board.relocate(20,20);
        board.drawBoard();
        board.setOnMousePressed(e -> board.mousePressed(e));

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
        int fromRow, fromCol;
        int toRow, toCol;

        CheckersMove(int r1, int c1, int r2, int c2) {
            fromRow = r1;
            fromCol = c1;
            toRow = r2;
            toCol = c2;
        }

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

        CheckersMove[] getLegalMoves(int player) {

            if (player != RED && player != BLACK) {
                return null;
            }

            int playerKing;
            if (player == RED) {
                playerKing = RED_KING;
            } else {
                playerKing = BLACK_KING;
            }

            ArrayList<CheckersMove> moves = new ArrayList<CheckersMove>();
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (board[row][col] == player || board[row][col] == playerKing) {
                        if (canJump(player, row, col, row+1, col+1, row+2, col+2))
                            moves.add(new CheckersMove(row, col, row+2, col+2));
                        if (canJump(player, row, col, row-1, col+1, row-2, col+2))
                            moves.add(new CheckersMove(row, col, row-2, col+2));
                        if (canJump(player, row, col, row+1, col-1, row+2, col-2))
                            moves.add(new CheckersMove(row, col, row+2, col-2));
                        if (canJump(player, row, col, row-1, col-1, row-2, col-2))
                            moves.add(new CheckersMove(row, col, row-2, col-2));
                    }
                }
            }
            if (moves.size() == 0) {
                for (int row = 0; row < 8; row++) {
                    for (int col = 0; col < 8; col++) {
                        if (board[row][col] == player || board[row][col] == playerKing) {
                            if (canMove(player,row,col,row+1,col+1))
                                moves.add(new CheckersMove(row,col,row+1,col+1));
                            if (canMove(player,row,col,row-1,col+1))
                                moves.add(new CheckersMove(row,col,row-1,col+1));
                            if (canMove(player,row,col,row+1,col-1))
                                moves.add(new CheckersMove(row,col,row+1,col-1));
                            if (canMove(player,row,col,row-1,col-1))
                                moves.add(new CheckersMove(row,col,row-1,col-1));
                        }
                    }
                }
            }
            if (moves.size() == 0)
                return null;
            else {
                CheckersMove[] moveArray = new CheckersMove[moves.size()];
                for (int i = 0; i < moves.size(); i++)
                    moveArray[i] = moves.get(i);
                return moveArray;
            }

        }

        private boolean canMove(int player, int r1, int c1, int r2, int c2) {
            if (r2 < 0 || r2 >= 8 || c2 < 0 || c2 >= 8)
                return false;  // (r2,c2) is off the board.

            if (board[r2][c2] != EMPTY)
                return false;  // (r2,c2) already contains a piece.

            if (player == RED) {
                if (board[r1][c1] == RED && r2 > r1)
                    return false;  // Regular red piece can only move up.
                return true;  // The move is legal.
            }
            else {
                if (board[r1][c1] == BLACK && r2 < r1)
                    return false;  // Regular black piece can only move down.
                return true;  // The move is legal.
            }
        }

        private boolean canJump(int player, int r1, int c1, int r2, int c2, int r3, int c3) {
            if (r3 < 0 || r3 >= 8 || c3 < 0 || c3 >= 8)
                return false;  // (r3,c3) is off the board.

            if (board[r3][c3] != EMPTY)
                return false;  // (r3,c3) already contains a piece.

            if (player == RED) {
                if (board[r1][c1] == RED && r3 > r1)
                    return false;  // Regular red piece can only move up.
                if (board[r2][c2] != BLACK && board[r2][c2] != BLACK_KING)
                    return false;  // There is no black piece to jump.
                return true;  // The jump is legal.
            }
            else {
                if (board[r1][c1] == BLACK && r3 < r1)
                    return false;  // Regular black piece can only move downn.
                if (board[r2][c2] != RED && board[r2][c2] != RED_KING)
                    return false;  // There is no red piece to jump.
                return true;  // The jump is legal.
            }
        }

    }


    private class CheckersBoard extends Canvas {

        CheckersData board;

        boolean gameInProgress;

        int currentPlayer;

        CheckersMove[] legalMoves;

        int selectedRow, selectedCol;


        CheckersBoard() {
            super(324,324);
            board = new CheckersData();
            doNewGame();
        }

        private void doNewGame() {
            if (gameInProgress == true) {
                message.setText("Finnish the current game first!");
                return;
            }

            board.setUpGame();
            currentPlayer = CheckersData.RED;
            gameInProgress = true;
            message.setText("Red: Make your move.");
            legalMoves = board.getLegalMoves(CheckersData.RED);
            selectedRow = -1;
            newGameButton.setDisable(true);
            resignButton.setDisable(false);
            drawBoard();
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
            if (gameInProgress) {
                /* First, draw a 4-pixel cyan border around the pieces that can be moved. */
                g.setStroke(Color.CYAN);
                g.setLineWidth(4);
                for (int i = 0; i < legalMoves.length; i++) {
                    g.strokeRect(4 + legalMoves[i].fromCol*40, 4 + legalMoves[i].fromRow*40, 36, 36);
                }
                /* If a piece is selected for moving (i.e. if selectedRow >= 0), then
                    draw a yellow border around that piece and draw green borders
                    around each square that that piece can be moved to. */
                if (selectedRow >= 0) {
                    g.setStroke(Color.YELLOW);
                    g.setLineWidth(4);
                    g.strokeRect(4 + selectedCol*40, 4 + selectedRow*40, 36, 36);
                    g.setStroke(Color.LIME);
                    g.setLineWidth(4);
                    for (int i = 0; i < legalMoves.length; i++) {
                        if (legalMoves[i].fromCol == selectedCol && legalMoves[i].fromRow == selectedRow) {
                            g.strokeRect(4 + legalMoves[i].toCol*40, 4 + legalMoves[i].toRow*40, 36, 36);
                        }
                    }
                }
            }

        }

        public void doResign() {
            if (gameInProgress == false) {
                message.setText("There is no game in progress!");
                return;
            }
            if (currentPlayer == CheckersData.RED) {
                gameOver("RED resigns. BLACK wins.");
            } else {
                gameOver("BLACK resigns. RED wins.");
            }
        }

        private void gameOver(String s) {
            message.setText(s);
            newGameButton.setDisable(false);
            resignButton.setDisable(true);
            gameInProgress = false;
        }

        public void mousePressed(MouseEvent e) {
            if (gameInProgress == false) {
                message.setText("Click \"New Game\" to start a new game.");
            } else {
                int col = (int) ((e.getX() - 2) / 40);
                int row = (int) ((e.getY() - 2) / 40);
                if (col >= 0 && col < 8 && row >= 0 && row < 8) {
                    doClickSquare(row, col);
                }
            }
        }

        private void doClickSquare(int row, int col) {

        }
    }

}