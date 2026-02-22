package work.terrencemurray.ui;

import javax.swing.JPanel;
import javax.swing.Timer;

import work.terrencemurray.items.decorators.ItemWithCollectable.ItemType;
import work.terrencemurray.managers.ImageManager;
import work.terrencemurray.managers.ItemManager;
import work.terrencemurray.managers.SoundManager;
import work.terrencemurray.player.Player;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.Vector;

public class GamePanel extends JPanel {

    private static final int SCORE_PER_BANANA = 1;
    private static final int BAT_DAMAGE = 5;
    private static final int MAX_HEALTH = 100;

    // HUD layout
    private static final int HUD_MARGIN = 12;
    private static final int BAR_WIDTH = 120;
    private static final int BAR_HEIGHT = 14;
    private static final int BANANA_ICON_SIZE = 20;

    private final ItemManager itemManager;
    private final Player player;
    private final Timer gameTimer;
    private final SoundManager soundManager = SoundManager.getInstance();
    private final Image backgroundImage;
    private final Image bananaIcon;

    private int score = 0;
    private int health = MAX_HEALTH;
    private boolean gameOver = false;

    public GamePanel() {
        this.setBackground(new Color(50, 50, 50));

        backgroundImage = ImageManager.loadImage("/images/shreya-das-ntcc-final-year-cartoon-bg.png");
        bananaIcon = ImageManager.loadImage("/images/fruit_banana.png");
        itemManager = new ItemManager();
        player = new Player();

        gameTimer = new Timer(16, e -> {
            update();
            repaint();
        });
    }

    public void start() {
        soundManager.playClip("gameStart", false);
        soundManager.playClip("background", true);
        itemManager.spawnItems(getWidth(), getHeight());
        gameTimer.start();
    }

    public void stop() {
        gameTimer.stop();
        gameOver = true;
        soundManager.stopClip("background");
        soundManager.playClip("gameOver", false);
    }

    private void update() {
        Vector<ItemType> collisions = itemManager.update(
            player.getCollider(), getWidth(), getHeight()
        );

        for (ItemType type : collisions) {
            player.onCatch();
            if (type == ItemType.BANANA) {
                score += SCORE_PER_BANANA;
                soundManager.playClip("bananaCollect", false);
            } else if (type == ItemType.BAT) {
                health = Math.max(0, health - BAT_DAMAGE);
                soundManager.playClip("batHit", false);
                if (health <= 0) {
                    stop();
                    return;
                }
            }
        }

        player.update(getWidth(), getHeight());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        }

        itemManager.render(g2);
        player.render(g2);

        renderHUD(g2);

        if (gameOver) {
            renderGameOverOverlay(g2);
        }
    }

    private void renderHUD(Graphics2D g2) {
        renderHealthBar(g2);
        renderBananaCount(g2);
    }

    private void renderHealthBar(Graphics2D g2) {
        int x = HUD_MARGIN;
        int y = HUD_MARGIN;

        // Background
        g2.setColor(new Color(0, 0, 0, 100));
        g2.fillRoundRect(x, y, BAR_WIDTH, BAR_HEIGHT, 8, 8);

        // Fill
        double pct = (double) health / MAX_HEALTH;
        int fillWidth = (int) ((BAR_WIDTH - 2) * pct);
        if (fillWidth > 0) {
            Color top, bottom;
            if (pct > 0.5) {
                top = new Color(70, 210, 70);
                bottom = new Color(40, 160, 40);
            } else if (pct > 0.25) {
                top = new Color(255, 190, 30);
                bottom = new Color(210, 140, 10);
            } else {
                top = new Color(230, 55, 55);
                bottom = new Color(170, 30, 30);
            }

            GradientPaint fill = new GradientPaint(0, y + 1, top, 0, y + BAR_HEIGHT - 1, bottom);
            g2.setPaint(fill);
            g2.fillRoundRect(x + 1, y + 1, fillWidth, BAR_HEIGHT - 2, 6, 6);

            // Glass shine
            g2.setColor(new Color(255, 255, 255, 50));
            g2.fillRoundRect(x + 2, y + 2, fillWidth - 2, (BAR_HEIGHT - 4) / 2, 4, 4);
        }

        // Border
        g2.setColor(new Color(255, 255, 255, 80));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(x, y, BAR_WIDTH - 1, BAR_HEIGHT - 1, 8, 8);
    }

    private void renderBananaCount(Graphics2D g2) {
        Font countFont = new Font("Arial", Font.BOLD, 16);
        g2.setFont(countFont);
        FontMetrics fm = g2.getFontMetrics();
        String text = "x " + score;
        int textWidth = fm.stringWidth(text);

        int totalWidth = BANANA_ICON_SIZE + 4 + textWidth;
        int x = getWidth() - HUD_MARGIN - totalWidth;
        int y = HUD_MARGIN;

        // Banana icon
        if (bananaIcon != null) {
            g2.drawImage(bananaIcon, x, y, BANANA_ICON_SIZE, BANANA_ICON_SIZE, null);
        }

        // Count text with drop shadow
        int textX = x + BANANA_ICON_SIZE + 4;
        int textY = y + fm.getAscent() + (BANANA_ICON_SIZE - fm.getHeight()) / 2;

        g2.setColor(new Color(0, 0, 0, 120));
        g2.drawString(text, textX + 1, textY + 1);
        g2.setColor(Color.WHITE);
        g2.drawString(text, textX, textY);
    }

    private void renderGameOverOverlay(Graphics2D g2) {
        // Dark overlay
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, getWidth(), getHeight());

        int cy = getHeight() / 2;

        // "GAME OVER" title
        Font titleFont = new Font("Arial", Font.BOLD, 48);
        g2.setFont(titleFont);
        FontMetrics fm = g2.getFontMetrics();
        String title = "GAME OVER";
        int titleX = (getWidth() - fm.stringWidth(title)) / 2;
        int titleY = cy - 20;

        // Drop shadow
        g2.setColor(new Color(0, 0, 0, 150));
        g2.drawString(title, titleX + 3, titleY + 3);

        // Main title
        g2.setColor(new Color(230, 50, 50));
        g2.drawString(title, titleX, titleY);

        // Score with banana icon
        Font scoreFont = new Font("Arial", Font.BOLD, 22);
        g2.setFont(scoreFont);
        fm = g2.getFontMetrics();
        String scoreText = "x " + score;
        int iconSize = 24;
        int totalWidth = iconSize + 6 + fm.stringWidth(scoreText);
        int scoreX = (getWidth() - totalWidth) / 2;

        if (bananaIcon != null) {
            g2.drawImage(bananaIcon, scoreX, cy + 10, iconSize, iconSize, null);
        }

        int textX = scoreX + iconSize + 6;
        int textY = cy + 10 + fm.getAscent() + (iconSize - fm.getHeight()) / 2;

        g2.setColor(new Color(0, 0, 0, 100));
        g2.drawString(scoreText, textX + 2, textY + 2);
        g2.setColor(new Color(255, 220, 60));
        g2.drawString(scoreText, textX, textY);
    }

    public void setDebugging(boolean flag) {
        itemManager.setDebugging(flag);
        player.getCollider().setDebugging(flag);
    }

    public Player getPlayer() {
        return player;
    }
}
