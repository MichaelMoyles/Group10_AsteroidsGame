package asteroidsGame;

import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

public class Bullet extends Rectangle {
    private Point2D velocity; // Bullet velocity
    private boolean isAlive = true; // Are the bullets still alive

    public Bullet(double x, double y, double direction) {
        setWidth(5); // Bullet width
        setHeight(10); // Bullet height
        setTranslateX(x); // x-coordinate of the bullet
        setTranslateY(y); // y-coordinate of the bullet

        double speed = 2; // Bullet speed
        double changeX = Math.cos(Math.toRadians(direction)) * speed;
        double changeY = Math.sin(Math.toRadians(direction)) * speed;
        velocity = new Point2D(changeX, changeY);
    }

    public void move() {
        if (isAlive) {
            // Update bullet position
            setTranslateX(getTranslateX() + velocity.getX());
            setTranslateY(getTranslateY() + velocity.getY());

            // Determining if a bullet is out of bounds
            if (getTranslateX() < 0 || getTranslateX() > Main.stageWidth || getTranslateY() < 0 || getTranslateY() > Main.stageHeight) {
                isAlive = false;
            }
        }
    }

    public boolean isAlive() {
        return isAlive;
    }
}

