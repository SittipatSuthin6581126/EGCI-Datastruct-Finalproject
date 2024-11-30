import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.*;

public class KnightGraph {

    public static class Point {
        private final int x;
        private final int y;
        private final String direction;

        public Point(int x, int y, String direction) {
            this.x = x;
            this.y = y;
            this.direction = direction;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public Point add(Point other) {
            return new Point(this.x + other.x, this.y + other.y, "Result");
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }

    public SimpleGraph<String, DefaultEdge> createGraph(HashMap<String, Point> pointCoordinateMap, int n, Set<Board.Position> bombs) {
        SimpleGraph<String, DefaultEdge> knightPathGraph = new SimpleGraph<>(DefaultEdge.class);

        // Possible knight moves
        Map<String, Point> knightMoves = Map.of(
                "UpRight", new Point(-2, 1, "UpRight"),
                "UpLeft", new Point(-2, -1, "UpLeft"),
                "RightUp", new Point(-1, 2, "RightUp"),
                "RightDown", new Point(1, 2, "RightDown"),
                "DownRight", new Point(2, 1, "DownRight"),
                "DownLeft", new Point(2, -1, "DownLeft"),
                "LeftUp", new Point(-1, -2, "LeftUp"),
                "LeftDown", new Point(1, -2, "LeftDown")
        );

        // Add vertices excluding bomb cells
        for (String pointName : pointCoordinateMap.keySet()) {
            Point currentPoint = pointCoordinateMap.get(pointName);
            if (!isBomb(currentPoint, bombs)) {
                knightPathGraph.addVertex(pointName);
            }
        }

        // Add edges for valid knight moves
        for (String pointName : pointCoordinateMap.keySet()) {
            Point currentPoint = pointCoordinateMap.get(pointName);

            if (isBomb(currentPoint, bombs)) {
                continue; // Skip bomb cells
            }

            for (Map.Entry<String, Point> move : knightMoves.entrySet()) {
                Point destinationPoint = currentPoint.add(move.getValue());

                if (isValid(destinationPoint, n) && !isBomb(destinationPoint, bombs)) {
                    String destinationName = getVertexName(destinationPoint);
                    knightPathGraph.addEdge(pointName, destinationName);
                }
            }
        }

        return knightPathGraph;
    }

    public static List<String> findShortestPath(Graph<String, DefaultEdge> graph, String start, String end) {
        Queue<List<String>> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        queue.add(Collections.singletonList(start));
        visited.add(start);

        while (!queue.isEmpty()) {
            List<String> path = queue.poll();
            String current = path.get(path.size() - 1);

            if (current.equals(end)) return path;

            for (DefaultEdge edge : graph.edgesOf(current)) {
                String neighbor = graph.getEdgeTarget(edge).equals(current)
                        ? graph.getEdgeSource(edge) : graph.getEdgeTarget(edge);

                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    List<String> newPath = new ArrayList<>(path);
                    newPath.add(neighbor);
                    queue.add(newPath);
                }
            }
        }

        return null;
    }

    private boolean isValid(Point point, int n) {
        return point.getX() >= 0 && point.getX() < n && point.getY() >= 0 && point.getY() < n;
    }

    private boolean isBomb(Point point, Set<Board.Position> bombs) {
        return bombs.contains(new Board.Position(point.getX(), point.getY()));
    }

    private String getVertexName(Point point) {
        return "P" + point.getX() + "_" + point.getY();
    }
}