//Project2_6581126
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Board {
    private int size;
    private Position knight;
    private Position castle;
    private Set<Position> bombs;

    public Board(int size) {
        this.size = size;
        this.bombs = new HashSet<>();
    }

    public static class Position {
        private int row;
        private int col;

        public Position(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        public boolean isValid(int size) {
            return row >= 0 && col >= 0 && row < size && col < size;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return row == position.row && col == position.col;
        }

        @Override
        public int hashCode() {
            return 31 * row + col;
        }

        @Override
        public String toString() {
            return "(" + row + ", " + col + ")";
        }
    }

    public void setupBoard(Scanner scanner) {
        System.out.print("Enter Knight ID: ");
        knight = idToPosition(scanner.nextInt());

        while (!knight.isValid(size)) {
            System.out.println("Invalid position. Try again.");
            knight = idToPosition(scanner.nextInt());
        }

        System.out.print("Enter Castle ID: ");
        castle = idToPosition(scanner.nextInt());

        while (!castle.isValid(size) || castle.equals(knight)) {
            System.out.println("Invalid position. Try again.");
            castle = idToPosition(scanner.nextInt());
        }

        System.out.print("Enter bomb IDs separated by comma (invalid IDs will be ignored): ");
        scanner.nextLine();
        String[] bombIds = scanner.nextLine().split(",");
        for (String id : bombIds) {
            try {
                Position bomb = idToPosition(Integer.parseInt(id.trim()));
                if (bomb.isValid(size) && !bomb.equals(knight) && !bomb.equals(castle)) {
                    bombs.add(bomb);
                }
            } catch (NumberFormatException ignored) {
            }
        }
    }

    private Position idToPosition(int id) {
        return new Position(id / size, id % size);
    }

    public int getSize() {
        return size;
    }

    public Position getKnight() {
        return knight;
    }

    public Position getCastle() {
        return castle;
    }

    public Set<Position> getBombs() {
        return bombs;
    }
}