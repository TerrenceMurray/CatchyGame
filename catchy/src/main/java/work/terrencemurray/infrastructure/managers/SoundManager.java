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
    private HashMap<String, String> clipPaths;
    private HashMap<String, String[]> variations;
    private Random random;

    private SoundManager() {
        clips = new HashMap<String, Clip>();
        clipPaths = new HashMap<String, String>();
        variations = new HashMap<String, String[]>();
        random = new Random();

        // Background music (kept as persistent clip for looping)
        Clip clip = loadClip("/sounds/freesound_community-jungle-6432.wav");
        clips.put("background", clip);

        // Game start
        clipPaths.put("gameStart", "/sounds/freesound_community-game-start-6104.wav");

        // Game over
        clipPaths.put("gameOver", "/sounds/freesound_community-game-over-arcade-6435.wav");

        // Anvil hit variations
        clipPaths.put("anvilHit1", "/sounds/freesound_community-anvil-hit-1-103967.wav");
        clipPaths.put("anvilHit2", "/sounds/freesound_community-anvil-hit-2-14845.wav");
        variations.put("anvilHit", new String[]{"anvilHit1", "anvilHit2"});

        // Banana collect variations
        clipPaths.put("bananaCollect1", "/sounds/freesound_community-eating-sound-effect-36186.wav");
        clipPaths.put("bananaCollect2", "/sounds/freesounds123-crunchy-bite-2-340948.wav");
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

        // For looping clips (BGM), reuse the persistent clip
        if (looping) {
            Clip clip = getClip(title);
            if (clip != null) {
                clip.stop();
                clip.setFramePosition(0);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            return;
        }

        // For SFX, create a fresh clip so overlapping plays work
        String path = clipPaths.get(title);
        if (path != null) {
            Clip clip = loadClip(path);
            if (clip != null) {
                clip.addLineListener(event -> {
                    if (event.getType() == javax.sound.sampled.LineEvent.Type.STOP) {
                        clip.close();
                    }
                });
                clip.start();
            }
        }
    }

    public void stopClip(String title) {
        Clip clip = getClip(title);
        if (clip != null) {
            clip.stop();
        }
    }
}
