import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Labyrinth {

    File file;
    BufferedImage image;
    int[][] labyrinth;
    boolean solved = false;

    public Labyrinth(String path) {
        file = new File(path);
    }

    public void load() {
        try {
            image = ImageIO.read(file);
        } catch (Exception e) {
            System.out.println("Error occurred loading image of labyrinth:\n" + e.toString());
            System.exit(1);
        }
    }
}
