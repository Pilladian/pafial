import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Environment {

    // image file
    File file;
    BufferedImage image;

    // image variables
    int size = 500;
    int amount_squares = 20;

    // Nodes of the graph
    Node[][] V;
    // Connections of the graph
    Connection[] E = new Connection[2*amount_squares*(amount_squares - 1)];

    public Environment(String path) {
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

    public void prepareV() {
        int mul = size / amount_squares;

        // loop through every square
        for (int a = 0; a < amount_squares; a++) {
            int xMin = a * mul - mul;
            int xMax = a * mul;

            for (int b = 0; b < amount_squares; b++) {
                int yMin = b * mul - mul;
                int yMax = b * mul;

                // set boolean
                boolean black = false;
                boolean red = false;
                boolean blue = false;

                // check for painted pixel in square
                for (int x = xMin; x < xMax; x++) {
                    for (int y = yMin; y < yMax; y++) {
                        int rgb = image.getRGB(x, y);
                        if (rgb == 0) {
                            black = true;
                            break;
                        } else if (((rgb & 0x00ff0000) >> 16) != 0) {
                            red = true;
                            break;
                        } else if (((rgb & 0x000000ff)) != 0) {
                            blue = true;
                            break;
                        }
                    }
                }

                if (black) {
                    // mark not reachable area
                    V[a][b] = new Node("not reachable", a, b, false, false);
                } else if (red) {
                    // mark starting point
                    V[a][b] = new Node("reachable", a, b, true, false);
                } else if (blue) {
                    // mark destination point
                    V[a][b] = new Node("reachable", a, b, false, true);
                } else {
                    // mark reachable area
                    V[a][b] = new Node("reachable", a, b, false, false);
                }
            }
        }
    }

    public void prepareE() {
        int count = 0;
        for (int y = 0; y < amount_squares; y++) {
            for (int x = 1; x < amount_squares; x++) {
                E[count] = new Connection(V[x-1][y], V[x][y]);
            }
        }
    }
}
