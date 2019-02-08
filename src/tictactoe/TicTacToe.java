/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author ArnavJalan
 */
public class TicTacToe extends Application {
    
    private boolean stillPlayable = true;
    private boolean xTurn = true;
    private GridTile[][] gameBoard = new GridTile[3][3];
    private List<Combination> combinations = new ArrayList<>();
    
    private Pane root = new Pane();
    
    private Parent createContent() {
        root.setPrefSize(600, 600);
        /*
        Media media = new Media(Paths.get("Wander.mp3").toUri().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        
        root.getChildren().add(mediaPlayer);
        */
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                GridTile tile = new GridTile();
                tile.setTranslateX(j * 200);
                tile.setTranslateY(i * 200);
                
                root.getChildren().add(tile);
                gameBoard[j][i] = tile;
            }
        }
        
        // 3 rows on the game board
        for (int y = 0; y < 3; y++) {
            combinations.add(new Combination(gameBoard[0][y], gameBoard[1][y], gameBoard[2][y]));
        }
        
        // 3 columns on the game board
        for (int x = 0; x < 3; x++) {
            combinations.add(new Combination(gameBoard[x][0], gameBoard[x][1], gameBoard[x][2]));
        }
        
        // 2 diagnols on the game board
        combinations.add(new Combination(gameBoard[0][0], gameBoard[1][1], gameBoard[2][2]));
        combinations.add(new Combination(gameBoard[2][0], gameBoard[1][1], gameBoard[0][2]));
        
        return root;
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }
    
    private void checkGameState() {
        for (Combination combination : combinations) {
            if (combination.gameComplete()) {
                stillPlayable = false;
                winAnimation(combination);
                break;
            }
        }
    }
    
    private void winAnimation(Combination combination) {
        Line line = new Line();
        line.setStartX(combination.tiles[0].getXCenter());
        line.setStartY(combination.tiles[0].getYCenter());
        
        line.setEndX(combination.tiles[0].getXCenter());
        line.setEndY(combination.tiles[0].getYCenter());
        
        root.getChildren().add(line);
        
        Timeline tm = new Timeline();
        tm.getKeyFrames().add(new KeyFrame(Duration.seconds(2),
                new KeyValue(line.endXProperty(), combination.tiles[2].getXCenter()),
                new KeyValue(line.endYProperty(), combination.tiles[2].getYCenter())));
        tm.play();
    }
    
    private class Combination {
        private GridTile[] tiles;
        
        public Combination(GridTile... tiles) {
            this.tiles = tiles;   
        }
        
        public boolean gameComplete() {
            if (tiles[0].getTileText().isEmpty())
                return false;
            
            return tiles[0].getTileText().equals(tiles[1].getTileText())
                    && tiles[0].getTileText().equals(tiles[2].getTileText());
        }
    }    
    
    private class GridTile extends StackPane {
        private Text text = new Text();
        
        public GridTile() {
            Rectangle tile = new Rectangle(200, 200);
            tile.setFill(null);
            tile.setStroke(Color.BLACK);
            
            text.setFont(Font.font(80));
            
            setAlignment(Pos.CENTER);
            getChildren().addAll(tile, text);
            
            // Left mouse click draws X on tile
            // Right mouse click draws O on tile
            setOnMouseClicked(event -> {
                if (!stillPlayable)
                    return;
                
                if (event.getButton() == MouseButton.PRIMARY) {
                    if (!xTurn)
                       return;
                   
                    drawX();
                    xTurn = false;
                    checkGameState();
               }
                else if (event.getButton() == MouseButton.SECONDARY) {
                    if (xTurn)
                       return;
                   
                    drawO();
                    xTurn = true;
                    checkGameState();
               }
            });
        }
        
        public double getXCenter() {
            return getTranslateX() + 100;
        }
        
        public double getYCenter() {
            return getTranslateY() + 100;
        }
        
        public String getTileText() {
            return text.getText();
        }
        
        private void drawX() {
            text.setText("X");
        }
        
        private void drawO() {
            text.setText("O");
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }    
}
