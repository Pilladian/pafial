import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class Environment {

    // image file
    File file;
    BufferedImage image;

    // image variables
    int size = 500;
    int amount_squares = 5;

    // Nodes of the graph
    ArrayList<Node> V = new ArrayList<>();
    Node[][] nodes = new Node[amount_squares][amount_squares];
    // Connections of the graph
    ArrayList<Connection> E = new ArrayList<>();

    // Start + End
    Node start;
    Node destination;

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

            for (int b = 1; b <= amount_squares; b++) {
                int yMin = b * mul - mul;

                // set boolean
                boolean black = false;
                boolean red = false;
                boolean blue = false;

                // check for painted pixel in square
                int rgb = image.getRGB(xMin, yMin);
                if ((rgb & 0x00ffffff) == 0) {
                    black = true;
                }
                if (rgb != -1 && ((rgb & 0x00ff0000) >> 16) == 255) {
                    red = true;
                }
                if (rgb == -16776961) {
                    blue = true;
                }

                if (black) {
                    // mark not reachable area
                    nodes[a - 1][b - 1] = new Node("not reachable", a - 1, b - 1, false, false);
                    V.add(nodes[a - 1][b - 1]);
                } else if (red) {
                    // mark starting point
                    nodes[a - 1][b - 1] = new Node("reachable", a - 1, b - 1, true, false);
                    V.add(nodes[a - 1][b - 1]);
                } else if (blue) {
                    // mark destination point
                    nodes[a - 1][b - 1] = new Node("reachable", a - 1, b - 1, false, true);
                    V.add(nodes[a - 1][b - 1]);
                } else {
                    // mark reachable area
                    nodes[a - 1][b - 1] = new Node("reachable", a - 1, b - 1, false, false);
                    V.add(nodes[a - 1][b - 1]);
                }
            }
        }
    }

    public void prepareE() {
        // horizontal connections
        for (int y = 0; y < amount_squares; y++) {
            for (int x = 1; x < amount_squares; x++) {
                if (nodes[x - 1][y].value.equals("reachable") && nodes[x][y].value.equals("reachable")) {
                    E.add(new Connection(1, nodes[x - 1][y], nodes[x][y]));
                }
            }
        }
        // vertical connections
        for (int x = 0; x < amount_squares; x++) {
            for (int y = 1; y < amount_squares; y++) {
                if (nodes[x][y - 1].value.equals("reachable") && nodes[x][y].value.equals("reachable")) {
                    E.add(new Connection(1, nodes[x][y - 1], nodes[x][y]));
                }
            }
        }
    }

    public void prepareV() {
        for (Connection connection : E) {
            // Nodes
            connection.partA.addNeighbor(connection.partB);
            connection.partB.addNeighbor(connection.partA);
        }
        for (Node v : V) {
            if (v.startingPoint) {
                v.distance = 0;
                start = v;
            } else if (v.destination) {
                destination = v;
            }
        }
    }

    public void printParameter(boolean connections, boolean nodes) {
        if (connections) {
            System.out.println(E.size());
            for (Connection connection : E) {
                System.out.println("A[" + connection.partA.x + "," + connection.partA.y + "] -> B[" + connection.partB.x + "," + connection.partB.y + "]");
                System.out.println("B[" + connection.partB.x + "," + connection.partB.y + "] -> A[" + connection.partA.x + "," + connection.partA.y + "]");
                System.out.println(" ");
            }
        }
        if (nodes) {
            for (Node node : V) {
                if (node.startingPoint) {
                    System.out.println("[S][" + node.x + "," + node.y + "] " + node.distance);
                } else if (node.destination) {
                    System.out.println("[D][" + node.x + "," + node.y + "] " + node.distance);
                } else {
                    System.out.println("[ ][" + node.x + "," + node.y + "] " + node.distance);
                }
                System.out.println(" ");
            }
        }
    }

    public void bellmanFord() {
        bellmanFordHelperFunction(start);

        if (destination.previous != null) {
            System.out.println("Path found: " + path.toString());
        }

    }

    public void bellmanFordHelperFunction(Node node) {
        for (Node out : node.neighbors) {
            if (node.distance + 1 < out.distance) {
                out.distance = node.distance + 1;
                out.previous = node;
            }
            if (!node.destination) {
                bellmanFordHelperFunction(out);
            }
        }
    }

    private void getPath() {
        destination.Path(path);
    }

    public void dijkstra(boolean showProcess) {
        // TODO
    }

    public void drawShortestPath() {
        // TODO
    }
}
