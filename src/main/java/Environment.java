import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Environment {

    // image file
    File file;
    BufferedImage image;

    // image variables
    int size = 500;
    int amount_squares;

    // Nodes of the graph
    ArrayList<Node> V = new ArrayList<>();
    Node[][] nodes;
    // Connections of the graph
    ArrayList<Connection> E = new ArrayList<>();

    // Start + End
    Node start;
    Node destination;

    // neg. cycles
    boolean neg;

    // Shortest path
    ArrayList<Node> path = new ArrayList<>();

    public Environment(String path, int amount) {
        file = new File(path);
        amount_squares = amount;
        nodes  = new Node[amount_squares][amount_squares];
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
                if (rgb != -1 && (rgb & 0x000000ff) == 255) {
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

    public void BellmanFord() {
        for (int a = 0; a < V.size() - 1; a++) {
            relax();
        }
        relax();
        if (neg) {
            System.out.println("Negative Cycle was found.");
        }
    }

    public void relax() {
        neg = false;
        for (Connection connection : E) {
            Node a = connection.partB;
            Node b = connection.partA;
            if (a.distance + connection.value < b.distance) {
                b.distance = a.distance + connection.value;
                b.previous = a;
                neg = true;
            } else if (b.distance + connection.value < a.distance) {
                a.distance = b.distance + connection.value;
                a.previous = b;
                neg = true;
            }
        }
    }

    private int getWeight(Node a, Node b) {
        for (Connection e : E) {
            if ((a.equals(e.partA) && b.equals(e.partB)) || (b.equals(e.partA) && a.equals(e.partB))) {
                return e.value;
            }
        }
        return 1000;
    }

    public void getPath() {
        destination.Path(path);
        path.add(destination);
    }

    public void drawShortestPath() {
        int mul = size / amount_squares;
        for (Node node : path) {
            int xMin = (node.x + 1) * mul - mul;
            int xMax = (node.x + 1) * mul;
            int yMin = (node.y + 1) * mul - mul;
            int yMax = (node.y + 1) * mul;
            for (int a = xMin; a < xMax; a++) {
                for (int b = yMin; b < yMax; b++) {
                    image.setRGB(a, b, 0x0000ff00);
                }
            }
        }
        System.out.println("Path found: " + pathToString());
        try {
            ImageIO.write(image, "png", new File("/home/us3r/Downloads/test.png"));
        } catch (IOException ox) {
            ox.printStackTrace();
        }
    }

    public String pathToString() {
        StringBuilder stringPath = new StringBuilder();
        for (Node node : path) {
            stringPath.append(node.toString()).append(" ");
        }
        return stringPath.toString();
    }
}
