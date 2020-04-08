import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Environment {

    // image file
    File file;
    BufferedImage image;

    // image variables
    int size = 500;
    int amount_squares = 20;

    // Nodes of the graph
    Node[][] V = new Node[amount_squares][amount_squares];
    // Connections of the graph
    ArrayList<Connection> E = new ArrayList<>();

    // Shortest path
    ArrayList<Node> path = new ArrayList<>();

    public Environment(String path) {
        file = new File(path);
    }

    public void loadImage() {
        try {
            image = ImageIO.read(file);
        } catch (Exception e) {
            System.out.println("Error occurred loading image of labyrinth:\n" + e.toString());
            System.exit(1);
        }
    }

    public void loadV() {
        int mul = size / amount_squares;

        // loop through every square
        for (int a = 1; a <= amount_squares; a++) {
            int xMin = a * mul - mul;
            int xMax = a * mul;

            for (int b = 1; b <= amount_squares; b++) {
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

                        if (((rgb & 0x00ffffff) >> 16) == 0) {
                            black = true;
                            break;
                        } else if (rgb != -1 && ((rgb & 0x00ff0000) >> 16) == 255) {
                            red = true;
                            break;
                        } else if (rgb != -1 && ((rgb & 0x000000ff)) != 0) {
                            blue = true;
                            break;
                        }
                    }
                }

                if (black) {
                    // mark not reachable area
                    V[a - 1][b - 1] = new Node("not reachable", a, b, false, false);
                } else if (red) {
                    // mark starting point
                    V[a - 1][b - 1] = new Node("reachable", a, b, true, false);
                } else if (blue) {
                    // mark destination point
                    V[a - 1][b - 1] = new Node("reachable", a, b, false, true);
                } else {
                    // mark reachable area
                    V[a - 1][b - 1] = new Node("reachable", a, b, false, false);
                }
            }
        }
    }

    public void prepareE() {
        int count = 0;
        // connect horizontal
        for (int y = 0; y < amount_squares; y++) {
            for (int x = 1; x < amount_squares; x++) {
                if (V[x - 1][y].value.equals("reachable") && V[x][y].value.equals("reachable")) {
                    E.add(new Connection(1, V[x - 1][y], V[x][y]));
                    E.get(count).partA = V[x - 1][y];
                    E.get(count).partB = V[x][y];
                    count++;
                }
            }
        }
        // connect vertical
        count = 0;
        for (int x = 1; x < amount_squares; x++) {
            for (int y = 0; y < amount_squares; y++) {
                if (V[x - 1][y].value.equals("reachable") && V[x][y].value.equals("reachable")) {
                    E.add(new Connection(1, V[x - 1][y], V[x][y]));
                    E.get(count).partA = V[x - 1][y];
                    E.get(count).partB = V[x][y];
                    count++;
                }
            }
        }
    }

    public void prepareV() {
        for (Connection connection : E) {
            connection.partA.addNeighbor(connection.partB);
            connection.partB.addNeighbor(connection.partA);
        }
    }

    public void printParameter() {
        for (Connection connection : E) {
            System.out.println("A[" + connection.partA.x + "," + connection.partA.y + "] -> B[" + connection.partB.x + "," + connection.partB.y + "]");
            System.out.println("B[" + connection.partB.x + "," + connection.partB.y + "] -> A[" + connection.partA.x + "," + connection.partA.y + "]");
            System.out.println(" ");
        }
    }

    public void bellmanFord(boolean showProcess) {
        // TODO
    }

    public void dijkstra(boolean showProcess) {
        // TODO
    }

    public void drawShortestPath() {
        // TODO
    }
}
