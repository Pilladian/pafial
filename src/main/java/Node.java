import java.util.ArrayList;

public class Node {

    String value;
    int x;
    int y;
    boolean startingPoint;
    boolean destination;
    boolean visited = false;

    Node previous = null;
    int distance = 1000;

    ArrayList<Node> neighbors = new ArrayList<>();

    public Node(String v, int xCoordinate, int yCoordinate, boolean sp, boolean dst) {
        value = v;
        x = xCoordinate;
        y = yCoordinate;
        startingPoint = sp;
        destination = dst;
    }

    public void addNeighbor(Node node) {
        neighbors.add(node);
    }

    public void Path(ArrayList<Node> path) {
        if (!startingPoint) {
            path.add(previous);
            previous.Path(path);
        }
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "]";
    }
}
