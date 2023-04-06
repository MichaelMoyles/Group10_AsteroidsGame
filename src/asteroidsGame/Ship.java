package com.example.asteroidgame;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import static com.example.asteroidgame.Main.stageHeight;
import static com.example.asteroidgame.Main.stageWidth;

public class Ship {
    int defaultPlayerX; // the default x-coordinate of the player's ship
    int defaultPlayerY; // the default y-coordinate of the player's ship
    Point2D defaultPlayerSpeed; // the default speed of the player's ship
    private Polygon player; // the shape of the player's ship
    private Point2D movement; // the current movement vector of the player's ship
    private long lastBulletTime; // Add a field to store the last bullet time
    private static final long SHOOT_CD = 250 * 1000000; // 250 ms
    public Ship(int x, int y) {

        // constructor to create the player's ship
        // takes x and y coordinates as parameters which determines where the ship is loaded into our scene.

        this.defaultPlayerX = x; // we remember the default value for X and Y so that we can reuse these values when restarting the game.
        this.defaultPlayerY = y;

        // likewise we do the same for our speed and set it to (0,0).
        // This means that when we restart the game we do not restart the ship with the same velocity as last recorded in our previous game.
        this.defaultPlayerSpeed = new Point2D(0, 0);

        // create the polygon shape for the player's ship
        this.player = new Polygon(-10, -10, 15, 0, -10, 10);
        this.player.setFill(Color.WHITE);
        this.player.setTranslateX(x);
        this.player.setTranslateY(y);

        // the initial rotation of the ship is set to -90 degrees. This means that our triangle ship is facing up.
        this.player.setRotate(-90);
        this.movement = new Point2D(0, 0); // initial movement vector is set to (0,0). Our ship is not going anywhere just yet.
        //Initial bullet firing time
        this.lastBulletTime = 0;
    }
    public Polygon getCharacter() {
        return player; // returns the shape of the player's ship to the scene we call it on
    }
    public  void resetPosition(){
        // resets the player's ship to its default position and speed
        this.player.setTranslateX(defaultPlayerX);
        this.player.setTranslateY(defaultPlayerY);
        this.player.setRotate(-90);
        this.movement = this.defaultPlayerSpeed;
    }

    public void turnLeft() {
        // turns the player's ship to the left by 45 degrees
        this.player.setRotate(this.player.getRotate() - 45);
    }

    public void turnRight() {
        // turns the player's ship to the right by 45 degrees
        this.player.setRotate(this.player.getRotate() + 45);
    }

    public void accelerate(){
        // accelerates the player's ship in the direction it is facing
        double acceleration = 0.25; // the rate of acceleration
        double maxSpeed = 10.0; // the maximum speed the ship can travel
        double direction = Math.toRadians(this.player.getRotate()); // the current direction of the ship in radians
        double changeX = Math.cos(direction) * acceleration;
        double changeY = Math.sin(direction) * acceleration;
        movement = movement.add(changeX, changeY); // add the acceleration vector to the movement vector
        double speed = movement.magnitude(); // calculate the speed of the ship
        if (speed > maxSpeed) {
            // normalize() returns a vector with the same direction as movement, but with a magnitude (or length) of 1.0.
            // This is useful when you want to maintain the direction of a vector but adjust its length.
            // multiply() is a method on Point2D that multiplies the x and y components of the vector by a value.
            // In this case, we're multiplying the normalized vector (with a magnitude of 1.0) by maxSpeed, which gives us a vector with the same direction as movement but a magnitude of maxSpeed.
            // We're then updating the movement vector to be the new vector we just calculated in which has a magnitude of at most maxSpeed.
            movement = movement.normalize().multiply(maxSpeed);
        }
    }

    public void decelerate() {
        // decelerates the player's ship by a certain amount
        // movement.multiply(0.12) scales the movement vector by a factor of 0.12.
        // movement.subtract(...) subtracts the scaled vector from the original movement vector.
        // This has the effect of reducing the length of the movement vector by 12%.
        movement = movement.subtract(movement.multiply(0.15));
    }

    public void move() {
        // moves the player's ship based on its current movement vector
        this.player.setTranslateX(this.player.getTranslateX() + this.movement.getX());
        this.player.setTranslateY(this.player.getTranslateY() + this.movement.getY());

        System.out.println(this.player.getTranslateX());

        // wrap the player's ship around the screen if it goes off the edges
        if (this.player.getTranslateX() < 0) {
            this.player.setTranslateX(stageWidth);
        }
        if (this.player.getTranslateX() > stageWidth) {
            this.player.setTranslateX(0);
        }
        if (this.player.getTranslateY() < 0) {
            this.player.setTranslateY(stageHeight);
        }
        if (this.player.getTranslateY() > stageHeight) {
            this.player.setTranslateY(0);
        }
    }

    public Bullet shoot() {
        long currentTime = System.nanoTime();
        if (currentTime - lastBulletTime < SHOOT_CD) {
            return null; // If the CD is not enough, don't create a bullet
        }

        lastBulletTime = currentTime;

        //    Get the position and direction of the bullet
        double bulletX = player.getTranslateX();
        double bulletY = player.getTranslateY() + player.getBoundsInLocal().getHeight() / 2;
        double bulletDirection = player.getRotate();

        Bullet bullet = new Bullet(bulletX, bulletY, bulletDirection);
        return bullet;
    }
}