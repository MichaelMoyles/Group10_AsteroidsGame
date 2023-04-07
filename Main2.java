package com.example.asteroidgame;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main2 extends Application {
    Scene gameScene, pauseScene;
    static double stageWidth, stageHeight;
    // Add a list of bullets
    private final List<Bullet> bullets = new ArrayList<>();
    private final List<Asteroid> asteroids = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Group 10 Asteroids Game");
        primaryStage.setMaximized(true);
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        stageWidth = screenSize.getWidth();
        stageHeight = screenSize.getHeight();
        primaryStage.setWidth(stageWidth);
        primaryStage.setHeight(stageHeight);

        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            stageWidth = (double) newVal;
            primaryStage.setWidth(stageWidth);
        });

        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
            stageHeight = (double) newVal;
            primaryStage.setHeight(stageHeight);
        });

        //Game Scene
        Button pause = new Button("Pause");

        Pane gamePane = new Pane();
        gamePane.setStyle("-fx-background-color: black;");
        pause.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
        pause.setOnAction(e -> primaryStage.setScene(pauseScene));
        gamePane.getChildren().addAll(pause);
        gameScene= new Scene(gamePane, stageWidth, stageHeight);

        // we create int positions X and Y that we will use to create our ship.
        // when we create a class for ship we call in an x and y position,
        // by default these positions are going to be dead in the center.

        int playerX, playerY;
        playerX = (int) (stageWidth/2);
        playerY = (int)(stageHeight/2);

        Polygon polygon;
        // create an instance of Asteroid class
        for (int i = 0; i < 10; i++) {
            double size = Math.random() * 20 + 60; // random size between 60 and 80
            double speed = Math.random() * 1; // random speed between 1
            int x = (int) (Math.random() * stageWidth + playerX/2);
            int y = (int) (Math.random() * stageHeight + playerY/2);
            Asteroid asteroid = new Asteroid(size, speed, x, y);
            gamePane.getChildren().add(asteroid.getAsteroid());
            asteroids.add(asteroid); // add asteroid to the asteroids array
        }

        // add the asteroid to the game scene
        // Instantiating a Ship called player that we can manipulate and adding it to the game scene.
        Ship player = new Ship (playerX,playerY);
        gamePane.getChildren().add(player.getCharacter());

        //Pause Scene
        Label pauseSceneTitle = new Label("Pause Menu");
        //Will have to make each of these scenes
        Button resume = new Button("Resume");
        Button mainMenu = new Button("Main Menu");
        Button closeGame = new Button("Close Game");
        Button restartGame = new Button("Restart Game");

        // Some simple functionality for the buttons
        // resume will return back to the primary scene (gameScene)
        // closeGame will close the application/stage for the game
        // restartGame will restart the application ... not yet built
        // mainMenu will bring you back to the starting screen... not yet built

        resume.setOnAction(e -> primaryStage.setScene(gameScene));
        closeGame.setOnAction(event -> primaryStage.close());
        restartGame.setOnAction(event ->   {
            player.resetPosition();
            primaryStage.setScene(gameScene);
            primaryStage.show();
            for (int i = 0; i < 10; i++) {
                double size = Math.random() * 20 + 60; // random size between 60 and 80
                double speed = Math.random() * 1; // random speed between 1
                int x = (int) (Math.random() * stageWidth + playerX/2);
                int y = (int) (Math.random() * stageHeight + playerY/2);
                Asteroid asteroid = new Asteroid(size, speed, x, y);
                gamePane.getChildren().add(asteroid.getAsteroid());
                asteroids.add(asteroid); // add asteroid to the asteroids array
            }
        });

        //Potential option for scene
        GridPane gridPauseScene = new GridPane();
        GridPane.setConstraints(pauseSceneTitle, 0, 0);
        GridPane.setConstraints(resume,0, 1);
        GridPane.setConstraints(mainMenu, 0, 2);
        GridPane.setConstraints(closeGame, 0, 3);
        GridPane.setConstraints(restartGame, 0, 5);
        gridPauseScene.getChildren().addAll(pauseSceneTitle, resume, mainMenu, closeGame, restartGame);
        pauseScene = new Scene(gridPauseScene, stageWidth, stageHeight);

        mainMenu.setOnAction(e -> new MainMenu(primaryStage,gameScene));

        //Will have to be changed to main menu when implemented
        primaryStage.setScene(gameScene);

        new MainMenu(primaryStage, gameScene);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                player.move();
                asteroids.forEach(Asteroid::move);
                bullets.forEach(bullet -> {
                    bullet.move();
                    asteroids.stream()
                            .filter(asteroid -> asteroid.collide(bullet))
                            .forEach(asteroid -> {
                                asteroid.increaseSpeedOnDestruction();
                                gamePane.getChildren().removeAll(asteroid.getAsteroid(), bullet);
                                asteroids.remove(asteroid);
                                bullets.remove(bullet);
                            });
                });
                bullets.removeIf(bullet -> {
                    if (!bullet.isAlive()) {
                        gamePane.getChildren().remove(bullet);
                        return true;
                    }
                    return false;
                });
            }
        };

        timer.start();

        gameScene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case LEFT:
                    player.turnLeft();
                    break;
                case RIGHT:
                    player.turnRight();
                    break;
                case UP:
                    player.accelerate();
                    break;
                case DOWN:
                    player.decelerate();
                    break;
                case Z: // Update case for z key
                    Bullet bullet = player.shoot();
                    if (bullet != null) {
                        bullets.add(bullet);
                        gamePane.getChildren().add(bullet);
                    }
                break;
            }
        });
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}