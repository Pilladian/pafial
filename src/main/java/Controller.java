public class Controller {

    public static void main(String[] args) {
        // argument handling
        if (args.length != 1) {
            help();
            System.exit(1);
        }
        // get path of labyrinth
        String path = args[0];

        // read in labyrinth

        // prepare data for solver

        // solve labyrinth (possible with live imaging of the process)

        // draw shortest way trough the labyrinth
    }

    private static void help() {
        System.out.println("Correct usage:\n\tjava -jar labso.jar </path/to/labyrinth.png>");
        System.out.println("\nDefault configuration of image files and labso.jar");
        System.out.println("\tSize of Image-file = 500x500 pixel");
        System.out.println("\tSize of Labyrinth = 25x25 squares");
    }
}
