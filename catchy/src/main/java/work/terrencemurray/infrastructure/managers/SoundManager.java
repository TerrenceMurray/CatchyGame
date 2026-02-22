package work.terrencemurray.infrastructure.managers;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Random;

public class SoundManager {

    private static SoundManager instance = null;

    private HashMap<String, Clip> clips;
    private HashMap<String, String[]> variations;
    private Random random;

    private SoundManager() {
        clips = new HashMap<String, Clip>();
        variations = new HashMap<String, String[]>();
        random = new Random();

        // Background music
        Clip clip = loadClip("/sounds/freesound_community-jungle-6432.wav");
        clips.put("background", clip);

        // Game start
        clip = loadClip("/sounds/freesound_community-game-start-6104.wav");
        clips.put("gameStart", clip);

        // Game over
        clip = loadClip("/sounds/freesound_community-game-over-arcade-6435.wav");
        clips.put("gameOver", clip);

        // Anvil hit variations
        clip = loadClip("/sounds/freesound_community-anvil-hit-1-103967.wav");
        clips.put("anvilHit1", clip);
        clip = loadClip("/sounds/freesound_community-anvil-hit-2-14845.wav");
        clips.put("anvilHit2", clip);
        variations.put("anvilHit", new String[]{"anvilHit1", "anvilHit2"});

        // Banana collect variations
        clip = loadClip("/sounds/freesound_community-eating-sound-effect-36186.wav");
        clips.put("bananaCollect1", clip);
        clip = loadClip("/sounds/freesounds123-crunchy-bite-2-340948.wav");
        clips.put("bananaCollect2", clip);
        variations.put("bananaCollect", new String[]{"bananaCollect1", "bananaCollect2"});
    }

    public static SoundManager getInstance() {
        if (instance == null)
            instance = new SoundManager();

        return instance;
    }

    public Clip loadClip(String resourcePath) {
        AudioInputStream audioIn;
        Clip clip = null;

        try {
            InputStream is = getClass().getResourceAsStream(resourcePath);
            audioIn = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
            clip = AudioSystem.getClip();
            clip.open(audioIn);
        } catch (Exception e) {
            System.out.println("Error opening sound file: " + resourcePath + " - " + e);
        }
        return clip;
    }

    public Clip getClip(String title) {
        return clips.get(title);
    }

    public void playClip(String title, boolean looping) {
        // Check if title is a variation group
        if (variations.containsKey(title)) {
            String[] keys = variations.get(title);
            title = keys[random.nextInt(keys.length)];
        }

        Clip clip = getClip(title);
        if (clip != null) {
            clip.setFramePosition(0);
            if (looping)
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            else
                clip.start();
        }
    }

    public void stopClip(String title) {
        Clip clip = getClip(title);
        if (clip != null) {
            clip.stop();
        }
    }
}
