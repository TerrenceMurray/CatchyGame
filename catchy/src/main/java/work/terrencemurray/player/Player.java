package work.terrencemurray.player;

import java.awt.event.KeyListener;

import work.terrencemurray.items.Item;
import work.terrencemurray.items.decorators.ItemWithBoxCollider;
import work.terrencemurray.position.Point2D;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;

public class Player extends Item implements KeyListener {

    private int movementSpeed;

    private int width;
    private int height;
    private final ItemWithBoxCollider collider;

    private float targetX;
    private boolean movingLeft;
    private boolean movingRight;

    public Player() {
        super(250, 350);

        this.width = 100;
        this.height = 25;
        this.movementSpeed = 8;
        this.targetX = 250;
        this.collider = new ItemWithBoxCollider(this, this.width, this.height);
    }

    public ItemWithBoxCollider getCollider() {
        return this.collider;
    }

    private float lerp(float start, float end, float t) {
        return start + (end - start) * t;
    }

    public void update(int panelWidth, int panelHeight) {
        if (movingLeft) targetX -= movementSpeed;
        if (movingRight) targetX += movementSpeed;

        targetX = Math.max(0, Math.min(targetX, panelWidth - width));

        float currentX = this.currentPosition.getX();
        float newX = this.lerp(currentX, targetX, 0.25f);
        this.currentPosition.setX(Math.round(newX));
        this.currentPosition.setY(panelHeight - height - 20);
    }

    public Point2D<Integer> getCenter() {
        int centerX = this.currentPosition.getX() + this.width / 2;
        int centerY = this.currentPosition.getY() + this.height / 2;
        return new Point2D<>(centerX, centerY);
    }

    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int x = this.getCurrentPosition().getX();
        int y = this.currentPosition.getY();

        // Basket body with gradient
        GradientPaint gradient = new GradientPaint(x, y, new Color(139, 90, 43), x, y + height, new Color(101, 67, 33));
        g2.setPaint(gradient);
        g2.fillRoundRect(x, y, this.width, this.height, 12, 12);

        // Rim highlight
        g2.setColor(new Color(185, 135, 80));
        g2.fillRoundRect(x + 2, y, this.width - 4, 6, 6, 6);

        // Outline
        g2.setColor(new Color(70, 45, 20));
        g2.drawRoundRect(x, y, this.width, this.height, 12, 12);

        this.collider.renderDebug(g);
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
