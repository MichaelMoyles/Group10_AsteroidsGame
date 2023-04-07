package asteroidsGame;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;
import javafx.animation.AnimationTimer;
import javafx.scene.control.ListView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    //two scene objects being used displayed in primary stage
    Scene gameScene, pauseScene;

    //This gets the stagewidth and height. Change according to screen size
    static double stageWidth, stageHeight;
    // Add a list of bullets
    private final List<Bullet> bullets = new ArrayList<>();


    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Group 10 Asteroids Game");
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        stageWidth = screenSize.getWidth();
        stageHeight = screenSize.getHeight();
        System.out.println(stageWidth/2);
        primaryStage.setWidth(stageWidth);
        primaryStage.setHeight(stageHeight);

        //These are listeners. Depend on if its resized of not
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
         //So we are setting it to have a black colour
        Pane gamePane = new Pane();
        gamePane.setStyle("-fx-background-color: black;");
        pause.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE); //Creates the minimum size of the button//So this method is used to handle the pause button will call the scene change object
        gamePane.getChildren().addAll(pause);
        Scene gameScene = new Scene(gamePane, stageWidth, stageHeight);

        final Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        spacer.setMinSize(10, 1);
        pause.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
        pause.setOnAction(e -> primaryStage.setScene(pauseScene));

        // we create int positions X and Y that we will use to create our ship.
        // when we create a class for ship we call in an x and y position,
        // by default these positions are going to be dead in the center.

        int playerX, playerY;
        playerX = (int)(stageWidth/2);
        playerY = (int)(stageHeight/2);

        // Instantiating a Player called player that we can manipulate and adding it to the game scene.
        Player player = new Player(playerX,playerY);
        gamePane.getChildren().add(player.getCharacter());

        int alienX, alienY;
        alienX = (int)(stageWidth/4);
        alienY = (int)(stageHeight/4);
        BaseShip alien = new Alien(alienX, alienY);
        gamePane.getChildren().add(alien.getCharacter());

        // Create the VBox layout container just to center everything
        VBox buttonContainer = new VBox();
        buttonContainer.setSpacing(10); // Set the spacing between buttons
        //Pause Scene
        Label pauseSceneTitle = new Label("Pause Menu");

        //Will have to make each of these scenes
        Button resume = new Button("Resume");
        Button mainMenu = new Button("Main Menu");
        Button closeGame = new Button("Close Game");
        Button restartGame = new Button("Restart Game");

        buttonContainer.setSpacing(10);
        buttonContainer.getChildren().addAll(pauseSceneTitle,mainMenu,resume,closeGame,restartGame);

        buttonContainer.setAlignment(Pos.CENTER); // Center the VBox
        //Center it within the VBbox

//        pauseScene.setRoot(buttonContainer); // Set the VBox as the root of the scene
        Pane pausePane = new Pane();
        pausePane.setStyle("-fx-background-color: black;");
        pausePane.getChildren().addAll(buttonContainer);
        buttonContainer.setStyle("-fx-background-color: black;");
        pauseSceneTitle.setTextFill(Color.WHITE);
        buttonContainer.setStyle("-fx-background-color: black;");
     //This is not resizable
//        buttonContainer.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> {
//            buttonContainer.setLayoutX((pausePane.getWidth() - newVal.getWidth()) / 2);
//            buttonContainer.setLayoutY((pausePane.getHeight() - newVal.getHeight()) / 2);
//        });
        //This will be changed everytime the page is resized.
        buttonContainer.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> {
            double x = (pausePane.getWidth() - newVal.getWidth()) / 2;
            double y = (pausePane.getHeight() - newVal.getHeight()) / 2;
            buttonContainer.relocate(x, y);
        });
        pausePane.widthProperty().addListener((obs, oldVal, newVal) -> {
            double x = (pausePane.getWidth() -buttonContainer.getLayoutBounds().getWidth()) / 2;
            double y = (pausePane.getHeight() - buttonContainer.getLayoutBounds().getHeight()) / 2;
            buttonContainer.relocate(x, y);
        });
        //This is for resizable to get height
        pausePane.heightProperty().addListener((obs, oldVal, newVal) -> {
            double x = (pausePane.getWidth() - buttonContainer.getLayoutBounds().getWidth()) / 2;
            double y = (pausePane.getHeight() - buttonContainer.getLayoutBounds().getHeight()) / 2;
            buttonContainer.relocate(x, y);
        });
        pauseScene = new Scene(pausePane, screenSize.getWidth(), screenSize.getHeight());
     //   Scene pauseScene = new Scene(buttonContainer, stageWidth, stageHeight);

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
        });
        pause.setOnAction(e -> primaryStage.setScene(pauseScene));

        //Potential option for scene
//        GridPane gridPauseScene = new GridPane();
//        GridPane.setConstraints(pauseSceneTitle, 0, 0);
//        GridPane.setConstraints(resume,0, 1);
//        GridPane.setConstraints(mainMenu, 0, 2);
//        GridPane.setConstraints(closeGame, 0, 3);
//        GridPane.setConstraints(restartGame, 0, 5);
     //   gridPauseScene.getChildren().addAll(pauseSceneTitle, resume, mainMenu, closeGame, restartGame);
      //  pauseScene = new Scene(gridPauseScene, stageWidth, stageHeight);

        mainMenu.setOnAction(e -> new MainMenu(primaryStage,gameScene));

        //Will have to be changed to main menu when implemented
        primaryStage.setScene(gameScene);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                player.move();
                alien.move();

                // Add bullet movement handling
                //move method is called on each Bullet object in the bullets list
                bullets.forEach(Bullet::move);
                bullets.removeIf(bullet -> {
                    if (!bullet.isAlive()) {
                        gamePane.getChildren().remove(bullet);
                        return true;
                    }
                    return false;
                });
                // update screen to reflect new position
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
