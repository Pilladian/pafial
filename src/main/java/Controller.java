public class Controller {

    public static void main(String[] args) {
        // argument handling
        if (args.length != 1) {
            help();
            System.exit(1);
        }
        // get path of labyrinth
        String path = args[0];

        // create labyrinth
        Environment env = new Environment(path);
        env.loadImage();

        // prepare data for solver
        env.loadV();
        env.prepareE();
        env.prepareV();

        // find path from starting point to destination (possible with live imaging of the process)
        env.printParameter(false, true);

        // draw shortest way trough the labyrinth
        env.bellmanFord();
    }

    private static void help() {
        System.out.println("Correct usage:\n\tjava -jar pafial.jar </path/to/labyrinth.png>");
        System.out.println("\nDefault configuration of image files and pafial.jar");
        System.out.println("\tSize of Image-file = 500x500 pixel");
        System.out.println("\tSize of Image-file = 20x20 squares");
    }
}
