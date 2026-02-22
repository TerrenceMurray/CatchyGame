package work.terrencemurray.player;

import java.awt.event.KeyListener;

import work.terrencemurray.items.Item;
import work.terrencemurray.items.decorators.ItemWithBoxCollider;
import work.terrencemurray.managers.ImageManager;
import work.terrencemurray.position.Point2D;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;

public class Player extends Item implements KeyListener {

    private static final Color VINE_MAIN = new Color(55, 110, 35);
    private static final Color VINE_HIGHLIGHT = new Color(75, 140, 50);
    private static final Color VINE_SHADOW = new Color(35, 75, 20);
    private static final Color LEAF_COLOR = new Color(65, 145, 45);
    private static final Color LEAF_DARK = new Color(45, 105, 30);
    private static final BasicStroke THIN = new BasicStroke(1.5f);

    private static final int GRAB_DURATION = 14;
    private static final int FRAME_WIDTH = 70;
    private static final int FRAME_HEIGHT = 70;
    private static final int IDLE_FRAMES = 8;
    private static final int GRAB_FRAME = 8;
    private static final int ANIMATION_SPEED = 13;

    private final Image monkeySheet;

    private int movementSpeed;

    private int width;
    private int height;
    private final ItemWithBoxCollider collider;

    private float targetY;
    private boolean movingUp;
    private boolean movingDown;

    private int animationTick;
    private int grabTimer;

    public Player() {
        super(10, 200);

        this.width = 50;
        this.height = 55;
        this.movementSpeed = 8;
        this.targetY = 200;
        this.collider = new ItemWithBoxCollider(this, this.width, this.height);
        this.animationTick = 0;
        this.grabTimer = 0;
        this.monkeySheet = ImageManager.loadImage("/images/monkey.png");
    }

    public ItemWithBoxCollider getCollider() {
        return this.collider;
    }

    public void onCatch() {
        this.grabTimer = GRAB_DURATION;
    }

    private float lerp(float start, float end, float t) {
        return start + (end - start) * t;
    }

    public void update(int panelWidth, int panelHeight) {
        if (movingUp) targetY -= movementSpeed;
        if (movingDown) targetY += movementSpeed;

        targetY = Math.max(0, Math.min(targetY, panelHeight - height));

        float currentY = this.currentPosition.getY();
        float newY = this.lerp(currentY, targetY, 0.25f);
        this.currentPosition.setY(Math.round(newY));
        this.currentPosition.setX(10);

        animationTick++;
        if (grabTimer > 0) grabTimer--;
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
        int bob = (int) (3 * Math.sin(0.08 * animationTick));

        int vineX = x + 6;
        renderVine(g2, vineX);

        // Draw monkey from sprite sheet
        int frame = grabTimer > 0 ? GRAB_FRAME : (animationTick / ANIMATION_SPEED) % IDLE_FRAMES;
        int sx = frame * FRAME_WIDTH;

        // Offset by -5 to align with padding used during generation
        int drawX = x - 5;
        int drawY = y + bob - 5;

        g2.drawImage(monkeySheet,
            drawX, drawY, drawX + FRAME_WIDTH, drawY + FRAME_HEIGHT,
            sx, 0, sx + FRAME_WIDTH, FRAME_HEIGHT,
            null);

        this.collider.renderDebug(g);
    }

    private void renderVine(Graphics2D g2, int vineX) {
        // Vine shadow
        g2.setStroke(new BasicStroke(7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(VINE_SHADOW);
        g2.drawLine(vineX + 1, -10, vineX + 1, 2000);

        // Vine body
        g2.setStroke(new BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(VINE_MAIN);
        g2.drawLine(vineX, -10, vineX, 2000);

        // Vine highlight
        g2.setStroke(new BasicStroke(1.5f));
        g2.setColor(VINE_HIGHLIGHT);
        g2.drawLine(vineX - 1, -10, vineX - 1, 2000);

        // Leaves
        for (int ly = 15; ly < 2000; ly += 55) {
            renderLeaf(g2, vineX - 8, ly, false);
            renderLeaf(g2, vineX + 3, ly + 28, true);
        }
    }

    private void renderLeaf(Graphics2D g2, int lx, int ly, boolean flip) {
        g2.setColor(LEAF_DARK);
        g2.fillOval(lx + (flip ? 0 : -8), ly, 12, 6);
        g2.setColor(LEAF_COLOR);
        g2.fillOval(lx + (flip ? 1 : -7), ly, 10, 5);
        // Leaf vein
        g2.setColor(VINE_SHADOW);
        g2.setStroke(THIN);
        g2.drawLine(lx + (flip ? 2 : -2), ly + 3, lx + (flip ? 10 : -6), ly + 2);
    }

    public void keyPressed(KeyEvent event) {
        int keyCode = event.getKeyCode();

        if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) {
            movingUp = true;
        } else if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {
            movingDown = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) {
            movingUp = false;
        } else if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {
            movingDown = false;
        }
    }

    public void keyTyped(KeyEvent e) {}
}
