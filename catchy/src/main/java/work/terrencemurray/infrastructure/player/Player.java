package work.terrencemurray.infrastructure.player;

import java.awt.event.KeyListener;

import work.terrencemurray.infrastructure.items.Item;
import work.terrencemurray.infrastructure.position.Point2D;
import work.terrencemurray.infrastructure.ui.HealthBar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class Player extends Item implements KeyListener {
    
    class Direction {
        public static final int LEFT = -1;
        public static final int RIGHT = +1;
    }

    protected HealthBar healthBar;
    protected int movementSpeed;

    private int width;
    private int height;

    private float targetX;
    private boolean movingLeft;
    private boolean movingRight;

    public Player() {
        super(250, 350);

        this.width = 100;
        this.height = 25;
        this.movementSpeed = 8;
        this.targetX = 250;
    }

    private float lerp(float start, float end, float t) {
        return start + (end - start) * t;
    }

    public void update() {
        if (movingLeft) targetX -= movementSpeed;
        if (movingRight) targetX += movementSpeed;

        float currentX = this.currentPosition.getX();
        float newX = this.lerp(currentX, targetX, 0.25f);
        this.currentPosition.setX(Math.round(newX));
    }

    public Point2D<Integer> getCenter() {
        int centerX = this.currentPosition.getX() + this.width / 2;
        int centerY = this.currentPosition.getY() + this.height / 2;
        return new Point2D<>(centerX, centerY);
    }

    public void render(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(this.getCurrentPosition().getX(), this.currentPosition.getY(), this.width, this.height);
    }

    public void keyPressed(KeyEvent event) {
        int keyCode = event.getKeyCode();

        if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) {
            movingLeft = true;
        } else if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {
            movingRight = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) {
            movingLeft = false;
        } else if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {
            movingRight = false;
        }
    }

    public void keyTyped(KeyEvent e) {}
}
