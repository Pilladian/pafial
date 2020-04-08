import java.util.ArrayList;

public class Node {

    String value;
    int x;
    int y;
    boolean startingPoint;
    boolean destination;

    Node previous = null;
    int distance = 1000000;

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
}
