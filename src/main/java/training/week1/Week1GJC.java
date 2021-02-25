package training.week1;

import lombok.Data;

import java.util.*;

public class Week1GJC implements Week1Worker {
    List<Position> routeRecords = new ArrayList<>();
    Set<Position> positionSet = new HashSet<>();

    @Override
    public int[][] a(int cols, int rows) {
        Position.setMaxXY(cols, rows);
        routeRecords = new ArrayList<>();
        positionSet = new HashSet<>();
        Direction direction = Direction.RIGHT;
        Position currentPostion = Position.getPostion(0, 0);
        addRoute(currentPostion);

        while (routeRecords.size() < cols * rows) {
            Position expectedPosition = moveFront(currentPostion, direction);
            if (expectedPosition != null) {
                currentPostion = expectedPosition;
                addRoute(currentPostion);
            } else {
                direction = turnRight(direction);
            }
            /*System.out.print(currentPostion);
            System.out.println("    " + direction + "   " + positionSet.size());*/
        }
        //System.out.println(routeRecords);
        System.out.println("gjc-");
        return parseOutput(routeRecords, cols, rows);
    }

    private int[][] parseOutput(List<Position> routeRecords, int cols, int rows) {
        HashMap<Position, Integer> map = new HashMap<>();
        for (Position position : routeRecords) {
            map.put(position, routeRecords.indexOf(position));
        }
        int[][] array = new int[cols][rows];
        for (int r = 0; r < rows; r ++) {
            for (int c = 0; c < cols; c ++) {
                array[c][r] = map.get(Position.getPostion(c, r));
            }
        }
        return array;
    }

    private void addRoute(Position position) {
        routeRecords.add(position);
        positionSet.add(position);
    }
    @Override
    public int[][] b(int n, int m) {
        return new int[0][];
    }

    @Override
    public int[][] c(int n, int m) {
        return new int[0][];
    }


    private Position moveFront(Position lastPosition, Direction direction) {
        Position position;
        switch (direction) {
            case UP:
                position = Position.getPostion(lastPosition.x - 1, lastPosition.y);
                break;
            case DOWN:
                position = Position.getPostion(lastPosition.x + 1, lastPosition.y);
                break;
            case LEFT: position = Position.getPostion(lastPosition.x, lastPosition.y - 1);
                break;
            case RIGHT:
                position = Position.getPostion(lastPosition.x, lastPosition.y + 1);
                break;
            default:
                position =  null;
        }
        if (positionSet.contains(position)) {
            return null;
        } else {
            return position;
        }
    }

    private Direction turnLeft(Direction direction) {
        switch (direction) {
            case UP: return Direction.LEFT;
            case DOWN: return Direction.RIGHT;
            case LEFT: return Direction.DOWN;
            case RIGHT: return Direction.UP;
            default: return null;
        }
    }
    private Direction turnRight(Direction direction) {
        switch (direction) {
            case UP: return Direction.RIGHT;
            case DOWN: return Direction.LEFT;
            case LEFT: return Direction.UP;
            case RIGHT: return Direction.DOWN;
            default: return null;
        }
    }

    @Data
    private static class Position {
        static int maxX, maxY;
        int x, y;

        static Position getPostion(int x, int y) {
            if (x > maxX || x < 0 || y > maxY || y < 0) {
                return null;
            } else {
                return new Position(x, y);
            }
        }

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
        static void setMaxXY(int sizeX, int sizeY) {
            maxX = sizeX - 1;
            maxY = sizeY - 1;
        }
        @Override
        public String toString() {
            return "(x=" + x + ",y=" + y + ",maxX=" + maxX + ",maxY=" + maxY + ")";
        }
    }

    private enum Direction {
        UP(0),
        DOWN(1),
        LEFT(2),
        RIGHT(3);
        int val;
        Direction(int val) {
            this.val = val;
        }
    }

    public static void main(String[] args) {
        Week1GJC w = new  Week1GJC();
        w.a(3,3);
    }
}
