package maze;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static byte height;
    public static byte width;

    public static final String PASS = "  ";
    public static final String WALL = "██";

    public static byte[][] maze = null;

    public static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        String menu;
        do {
            printMenu();
            menu = sc.nextLine();
            switch (menu) {
                case "1" -> generateMaze();
                case "2" -> loadMaze();
                case "3" -> saveMaze();
                case "4" -> printMaze();
                case "0" -> {
                    System.out.println("Bye!");
                    return;
                }
                default -> System.out.println("Incorrect option. Please try again");
            }
        } while (true);
    }

    private static void saveMaze() {
        String input = sc.nextLine();
        byte[] toSave = new byte[height * width + 1];
        toSave[0] = height;
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                toSave[j * width + i + 1] = maze[j][i];
            }
        }
        try {
            Files.write(Paths.get(input), toSave);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadMaze() {
        String input = sc.nextLine();
        byte[] toLoad;
        try {
            toLoad = Files.readAllBytes(Paths.get(input));
        } catch (IOException e) {
            System.out.printf("The file %s does not exist\n", input);
            return;
        }

        if (toLoad.length == 0 || toLoad[0] * toLoad[0] != toLoad.length - 1) {
            System.out.println("Cannot load the maze. It has an invalid format");
            return;
        }
        height = toLoad[0];
        width = height;
        maze = new byte[height][width];

        for (int i = 0; i < height * width; i++) {
            maze[i / width][i % width] = toLoad[i + 1];
        }
    }

    private static void printMenu() {
        System.out.println("=== Menu ===");
        System.out.println("1. Generate a new maze");
        System.out.println("2. Load a maze");
        if (maze != null) {
            System.out.println("3. Save the maze");
            System.out.println("4. Display the maze");
        }
        System.out.println("0. Exit");
    }

    private static void generateMaze() {
        System.out.println("Enter the size of a new maze");
        String input = sc.nextLine();
        height = Byte.parseByte(input);
        width = height;
        maze = new byte[height][width];

        initMaze();
        List<Frontier> frontiers = new ArrayList<>();
        Random random = new Random();
        int y = random.nextInt(height - 2) + 1;
        int x = random.nextInt(width - 2) + 1;
        frontiers.add(new Frontier(y, x, y, x));

        while (!frontiers.isEmpty()) {
            int k = random.nextInt(frontiers.size());
            Frontier current = frontiers.remove(k);

            x = current.x;
            y = current.y;
            int dx = (current.parentX - current.x) / 2;
            int dy = (current.parentY - current.y) / 2;
            for (int l = 0; l < 3; l++) {
                maze[y + dy * l][x + dx * l] = 0;
            }

            //Left
            if (x - 2 > 0 && maze[y][x - 2] == 1) {
                Frontier frontier = new Frontier(y, x - 2, y, x);
                if (!frontiers.contains(frontier)) {
                    frontiers.add(frontier);
                }
            }
            //Right
            if (x + 2 < width - 1 && maze[y][x + 2] == 1) {
                Frontier frontier = new Frontier(y, x + 2, y, x);
                if (!frontiers.contains(frontier)) {
                    frontiers.add(frontier);
                }
            }
            //Up
            if (y - 2 > 0 && maze[y - 2][x] == 1) {
                Frontier frontier = new Frontier(y - 2, x, y, x);
                if (!frontiers.contains(frontier)) {
                    frontiers.add(frontier);
                }
            }
            //Down
            if (y + 2 < height - 1 && maze[y + 2][x] == 1) {
                Frontier frontier = new Frontier(y + 2, x, y, x);
                if (!frontiers.contains(frontier)) {
                    frontiers.add(frontier);
                }
            }
        }
        //In
        for (int j = 1; j < height - 1; j++) {
            if (maze[j][1] == 0) {
                maze[j][0] = 0;
                break;
            }
            if (maze[j][1] == 1 && maze[j][2] == 0) {
                maze[j][0] = 0;
                maze[j][1] = 0;
                break;
            }
        }
        //Out
        for (int j = height - 2; j >= 1; j--) {
            if (maze[j][width - 2] == 0) {
                maze[j][width - 1] = 0;
                break;
            }
            if (maze[j][width - 2] == 1 && maze[j][width - 3] == 0) {
                maze[j][width - 1] = 0;
                maze[j][width - 2] = 0;
                break;
            }
        }
        printMaze();
    }

    private static void initMaze() {
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                maze[j][i] = 1;
            }
        }
    }

    private static void printMaze() {
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                System.out.printf("%s", maze[j][i] == 1 ? WALL : PASS);
            }
            System.out.println();
        }
    }

    static final class Frontier {
        int y, x, parentY, parentX;

        public Frontier(int y, int x, int parentY, int parentX) {
            this.y = y;
            this.x = x;
            this.parentY = parentY;
            this.parentX = parentX;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Frontier frontier)) return false;

            if (y != frontier.y) return false;
            return x == frontier.x;
        }

        @Override
        public int hashCode() {
            int result = y;
            result = 31 * result + x;
            return result;
        }
    }
}
