package work.terrencemurray.managers;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.net.URL;

public class ImageManager {

    public static Image loadImage(String resourcePath) {
        URL url = ImageManager.class.getResource(resourcePath);
        return new ImageIcon(url).getImage();
    }
}
