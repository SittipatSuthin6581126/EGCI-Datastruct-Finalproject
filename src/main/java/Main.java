//Project2_6581126
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nWelcome to the Knight Moves Puzzle!");
        System.out.println("Note: The Knight must avoid Bombs and stay within the board.");

        boolean playAgain;

        do {
            System.out.println("=========================================");
            System.out.println("           --- Game Setup ---");
            System.out.println("=========================================");
            int boardSize = getBoardSize(scanner);

            // Display cell IDs for user reference
            printCellIds(boardSize);

            Board board = new Board(boardSize);
            board.setupBoard(scanner);

            // Graph creation and pathfinding
            KnightGraph knightGraph = new KnightGraph();
            HashMap<String, KnightGraph.Point> coordinateMap = new HashMap<>();

            for (int row = 0; row < boardSize; row++) {
                for (int col = 0; col < boardSize; col++) {
                    String positionName = "P" + row + "_" + col;
                    coordinateMap.put(positionName, new KnightGraph.Point(row, col, ""));
                }
            }

            Graph<String, DefaultEdge> graph = knightGraph.createGraph(coordinateMap, boardSize, board.getBombs());

            String start = "P" + board.getKnight().getRow() + "_" + board.getKnight().getCol();
            String end = "P" + board.getCastle().getRow() + "_" + board.getCastle().getCol();
            System.out.println("\n=========================================");
            System.out.println("      --- Initial Board Setup ---");
            System.out.println("=========================================");
            System.out.println("Knight starts at: " + start.substring(1) + " (Cell ID: " + positionToId(board.getKnight(), boardSize) + ")");
            System.out.println("Castle is at: " + end.substring(1) + " (Cell ID: " + positionToId(board.getCastle(), boardSize) + ")");
            System.out.println("Bombs are at: " + bombIds(board.getBombs(), boardSize) + "\n");
            printBoardWithEntities(boardSize, board.getKnight(), board.getCastle(), board.getBombs());

            List<String> path = KnightGraph.findShortestPath(graph, start, end);

            if (path != null) {
                System.out.println("\nBest route to Castle = " + (path.size() - 1) + " moves\n");

                // Show initial board state
                System.out.println("Initial --> Knight at " + positionToId(board.getKnight(), boardSize));
                printBoardWithEntities(boardSize, board.getKnight(), board.getCastle(), board.getBombs());

                for (int i = 1; i < path.size(); i++) {
                    String move = path.get(i);
                    Board.Position currentPosition = parsePosition(move);
                    int cellId = positionToId(currentPosition, boardSize);
                    System.out.println("Move " + i + " --> jump to " + cellId + " (" + currentPosition.getRow() + "," + currentPosition.getCol() + ")");
                    printBoardWithEntities(boardSize, currentPosition, board.getCastle(), board.getBombs());
                }
            } else {
                System.out.println("\n--- No Solution ---");
                System.out.println("The Knight cannot reach the Castle given the current board setup.");
            }

            System.out.print("\nDo you want to play again? (y/n): ");
            playAgain = scanner.next().equalsIgnoreCase("y");

        } while (playAgain);

        scanner.close();
        System.out.println("Thank you for playing Knight Moves Puzzle!");
    }

    private static int getBoardSize(Scanner scanner) {
        int size;
        do {
            System.out.print("Enter board size (N >= 5): ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter an integer value.");
                scanner.next(); // Clear invalid input
            }
            size = scanner.nextInt();
            if (size < 5) {
                System.out.println("Invalid board size. Please enter a value >= 5.");
            }
        } while (size < 5);
        return size;
    }

    private static void printCellIds(int size) {
        System.out.println("\nCell IDs (for reference):");
        for (int i = 0; i < size * size; i++) {
            System.out.printf("%6d", i);
            if ((i + 1) % size == 0) System.out.println();
        }
    }

    private static void printBoardWithEntities(int size, Board.Position knight, Board.Position castle, Set<Board.Position> bombs) {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Board.Position current = new Board.Position(row, col);
                int cellId = row * size + col;

                if (current.equals(knight)) {
                    System.out.printf("%4d:K* ", cellId); // Knight
                } else if (current.equals(castle)) {
                    System.out.printf("%4d:C* ", cellId); // Castle
                } else if (bombs.contains(current)) {
                    System.out.printf("%4d:B  ", cellId); // Bomb
                } else {
                    System.out.printf("%4d:   ", cellId); // Empty
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private static Board.Position parsePosition(String point) {
        String[] parts = point.substring(1).split("_");
        return new Board.Position(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }

    private static int positionToId(Board.Position position, int size) {
        return position.getRow() * size + position.getCol();
    }

    private static String bombIds(Set<Board.Position> bombs, int size) {
        List<Integer> bombIds = new ArrayList<>();
        for (Board.Position bomb : bombs) {
            bombIds.add(positionToId(bomb, size));
        }
        return bombIds.toString();
    }
}