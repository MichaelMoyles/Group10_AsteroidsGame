package asteroidsGame.scenes;

import asteroidsGame.constants.AppConstants;
import asteroidsGame.controllers.HighScoreRecorder;
import asteroidsGame.flyingobjects.Asteroid;
import asteroidsGame.flyingobjects.Player;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import static asteroidsGame.Main.timer;
import static asteroidsGame.flyingobjects.Alien.removeAliens;
import static asteroidsGame.scenes.GamePlayScene.*;

public class GameOverScene extends Scene {
    private static VBox gameOverVbox = new VBox(10);

    public GameOverScene(Player player, Stage primaryStage) {
        super(gameOverVbox);
        Label gameover = new Label("Game Over");
        Font font = Font.font("Lucida Sans Unicode", FontWeight.BOLD, 150);
        gameover.setFont(font);
        gameover.setTextFill(AppConstants.AppColor.SHAPE.getColor());

        Label HScore=new Label("Your Points are "+ points);
        Font font1 = Font.font("Lucida Sans Unicode", FontWeight.BOLD, 60);
        HScore.setFont(font1);
        HScore.setTextFill(AppConstants.AppColor.SHAPE.getColor());

        TextField name = new TextField();
        name.setText("Players name");
        name.setPrefHeight(25);
        name.setPrefWidth(50);
        name.setEditable(true);
        //This creates a button to submit the name to the leaderboard
        Button submitButton = new Button("Submit");

        gameOverVbox.getChildren().addAll(gameover,HScore, name, submitButton);
        gameOverVbox.setAlignment(Pos.CENTER); // Center the VBox
        gameOverVbox.setStyle(AppConstants.ButtonStyle.BACKGROUND.getStyle());

        submitButton.setOnAction(event -> {
            // Record and save player scores
            HScore.setText("Points are "+ points.get());
            System.out.println(name.getText());
            HighScoreRecorder.getRecorder().addHighScore(name.getText(), points);
            primaryStage.setScene(MainMenuScene.mainPageScene);
            player.resetPosition();
            points.set(0);
            levels.set(1);
            player.setLives(3);
            removeAliens();
            Asteroid.initAsteroids(levels.get());
            timer.stop();
            timerLabel.setText("Time: 0s");
        });
    }
}
