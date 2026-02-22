package work.terrencemurray.infrastructure.managers;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.net.URL;

public class ImageManager {

    public ImageManager() {
    }

    public static Image loadImage(String resourcePath) {
        URL url = ImageManager.class.getResource(resourcePath);
        return new ImageIcon(url).getImage();
    }
}
