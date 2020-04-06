public class Node {

    String value;
    int x;
    int y;
    boolean startingPoint;
    boolean destination;

    public Node(String v, int xCoordinate, int yCoordinate, boolean sp, boolean dst) {
        value = v;
        x = xCoordinate;
        y = yCoordinate;
        startingPoint = sp;
        destination = dst;
    }
}
