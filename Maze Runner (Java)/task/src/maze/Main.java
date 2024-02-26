package maze;

public class Main {
    public static final int HEIGHT = 10;
    public static final int WIDTH = 10;

    public static final String PASS = "  ";
    public static final String WALL = "\u2588\u2588";

    public static final int[][] MAZE = new int[HEIGHT][WIDTH];

    public static void main(String[] args) {
        initMaze();
        printMaze();
    }

    private static void initMaze() {
        for (int i = 0; i < WIDTH; i++) {
            MAZE[0][i] = 1;
            MAZE[HEIGHT - 1][i] = 1;
        }
        for (int i = 0; i < HEIGHT; i++) {
            MAZE[i][0] = 1;
            MAZE[i][WIDTH - 1] = 1;
        }
        MAZE[1][0] = 0;
        MAZE[3][WIDTH - 1] = 0;
    }

    private static void printMaze() {
        for (int j = 0; j < HEIGHT; j++) {
            for (int i = 0; i < WIDTH; i++) {
                System.out.printf("%s", MAZE[j][i] == 1 ? WALL : PASS);
            }
            System.out.println();
        }
    }
}
