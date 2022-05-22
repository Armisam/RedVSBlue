package redvsblue.state;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Represents the state of the Game.
 */
public class GameState implements Cloneable {

    /**
     * The height of the board.
     */
    public static final int BOARD_HEIGHT = 5;

    /**
     * The width of the board.
     */
    public static final int BOARD_WIDTH = 4;

    /**
     * The indexes of the red pucks.
     */
    public static final int[] RED_PUCKS = {0, 1, 2, 3};

    /**
     * The indexes of the blue pucks.
     */
    public static final int[] BLUE_PUCKS = {4, 5, 6, 7};

    private Position[] positions;

    /**
     * Creates a {@code GameState} object that corresponds to the original
     * initial state of the game.
     */
    public GameState() {
        this(new Position(0, 1),
                new Position(0, 3),
                new Position(4, 0),
                new Position(4, 2),
                new Position(0, 0),
                new Position(0, 2),
                new Position(4, 1),
                new Position(4, 3)
        );
    }

    /**
     * Creates a {@code GameState} object initializing the positions of the
     * pucks with the positions specified. The constructor expects an array of
     * eight {@code Position} objects or eight{@code Position} objects.
     *
     * @param positions the initial positions of the pucks
     */
    public GameState(Position... positions) {
        checkPositions(positions);
        this.positions = deepClone(positions);
    }

    private void checkPositions(Position[] positions) {
        if (positions.length != 8) {
            throw new IllegalArgumentException();
        }
        for (var position : positions) {
            if (!isOnBoard(position)) {
                throw new IllegalArgumentException();
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i != j && positions[i].equals(positions[j])) {
                    throw new IllegalArgumentException();
                }
            }
        }
    }

    /**
     * {@return a copy of the position of the puck specified}
     *
     * @param n the number of a puck
     */
    public Position getPosition(int n) {
        return positions[n].clone();
    }

    /**
     * {@return whether the game is won}
     */
    public boolean isWon() {
        Position[] redPucks = new Position[4];
        Position[] bluePucks = new Position[4];
        for (int i = 0; i < 8; i++) {
            if (i < 4) {
                redPucks[i] = positions[i];
            } else {
                bluePucks[i - 4] = positions[i];
            }
        }

        if (onSameCol(redPucks) || onSameCol(bluePucks) || onSameRow(redPucks) || onSameRow(bluePucks) || onSameDiag(redPucks) || onSameDiag(bluePucks)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean onSameCol(Position[] playerPuckPositions) {
        for (int i = 0; i < 4; i++) {
            int sameColCounter = 1;
            for (int j = 0; j < 4; j++) {
                if (i != j && playerPuckPositions[i].col() == playerPuckPositions[j].col() && playerPuckPositions[i].row() > 0 && playerPuckPositions[i].row() < BOARD_HEIGHT - 1 && (playerPuckPositions[j].row() == playerPuckPositions[i].row() - 1 || playerPuckPositions[j].row() == playerPuckPositions[i].row() + 1)) {
                    sameColCounter++;
                } else if (i != j && playerPuckPositions[i].col() == playerPuckPositions[j].col() && playerPuckPositions[i].row() == 0 && (playerPuckPositions[j].row() == playerPuckPositions[i].row() + 1 || playerPuckPositions[j].row() == playerPuckPositions[i].row() + 2)) {
                    sameColCounter++;
                } else if (i != j && playerPuckPositions[i].col() == playerPuckPositions[j].col() && playerPuckPositions[i].row() == BOARD_HEIGHT - 1 && (playerPuckPositions[j].row() == playerPuckPositions[i].row() - 1 || playerPuckPositions[j].row() == playerPuckPositions[i].row() - 2)) {
                    sameColCounter++;
                }
            }

            if (sameColCounter > 2) {
                return true;
            }
        }

        return false;
    }

    private boolean onSameRow(Position[] playerPuckPositions) {
        for (int i = 0; i < 4; i++) {
            int sameRowCounter = 1;
            for (int j = 0; j < 4; j++) {
                if (i != j && playerPuckPositions[i].row() == playerPuckPositions[j].row() && playerPuckPositions[i].col() > 0 && playerPuckPositions[i].col() < BOARD_WIDTH - 1 && (playerPuckPositions[j].col() == playerPuckPositions[i].col() - 1 || playerPuckPositions[j].col() == playerPuckPositions[i].col() + 1)) {
                    sameRowCounter++;
                } else if (i != j && playerPuckPositions[i].row() == playerPuckPositions[j].row() && playerPuckPositions[i].col() == 0 && (playerPuckPositions[j].col() == playerPuckPositions[i].col() + 1 || playerPuckPositions[j].col() == playerPuckPositions[i].col() + 2)) {
                    sameRowCounter++;
                } else if (i != j && playerPuckPositions[i].row() == playerPuckPositions[j].row() && playerPuckPositions[i].col() == BOARD_WIDTH - 1 && (playerPuckPositions[j].col() == playerPuckPositions[i].col() - 1 || playerPuckPositions[j].col() == playerPuckPositions[i].col() - 2)) {
                    sameRowCounter++;
                }
            }

            if (sameRowCounter > 2) {
                return true;
            }
        }

        return false;
    }

    private boolean onSameDiag(Position[] playerPuckPositions) {
        for (int i = 0; i < 4; i++) {
            int sameLeftDiagCounter = 1;
            int sameRightDiagCounter = 1;
            for (int j = 0; j < 4; j++) {
                if (i != j && playerPuckPositions[i].row() > 0 && playerPuckPositions[i].col() > 0 && playerPuckPositions[i].row() < BOARD_HEIGHT - 1 && playerPuckPositions[i].col() < BOARD_WIDTH - 1 && (playerPuckPositions[j].row() == playerPuckPositions[i].row() - 1 && playerPuckPositions[j].col() == playerPuckPositions[i].col() - 1 || playerPuckPositions[j].row() == playerPuckPositions[i].row() + 1 && playerPuckPositions[j].col() == playerPuckPositions[i].col() + 1)) {
                    sameRightDiagCounter++;
                } else if (i != j && playerPuckPositions[i].row() > 0 && playerPuckPositions[i].col() > 0 && playerPuckPositions[i].row() < BOARD_HEIGHT - 1 && playerPuckPositions[i].col() < BOARD_WIDTH - 1 && (playerPuckPositions[j].row() == playerPuckPositions[i].row() - 1 && playerPuckPositions[j].col() == playerPuckPositions[i].col() + 1 || playerPuckPositions[j].row() == playerPuckPositions[i].row() + 1 && playerPuckPositions[j].col() == playerPuckPositions[i].col() - 1)) {
                    sameLeftDiagCounter++;
                } else if (i != j && playerPuckPositions[i].row() == 0 && playerPuckPositions[i].col() == 0 &&  (playerPuckPositions[j].row() == playerPuckPositions[i].row() + 1 && playerPuckPositions[j].col() == playerPuckPositions[i].col() + 1 || playerPuckPositions[j].row() == playerPuckPositions[i].row() + 2 && playerPuckPositions[j].col() == playerPuckPositions[i].col() + 2)) {
                    sameRightDiagCounter++;
                }
                else if (i != j && playerPuckPositions[i].row() == BOARD_HEIGHT - 1 && playerPuckPositions[i].col() == BOARD_WIDTH - 1 &&  (playerPuckPositions[j].row() == playerPuckPositions[i].row() - 1 && playerPuckPositions[j].col() == playerPuckPositions[i].col() - 1 || playerPuckPositions[j].row() == playerPuckPositions[i].row() - 2 && playerPuckPositions[j].col() == playerPuckPositions[i].col() - 2)) {
                    sameRightDiagCounter++;
                } else if (i != j && playerPuckPositions[i].row() == 0 && playerPuckPositions[i].col() == BOARD_WIDTH - 1 &&  (playerPuckPositions[j].row() == playerPuckPositions[i].row() + 1 && playerPuckPositions[j].col() == playerPuckPositions[i].col() - 1 || playerPuckPositions[j].row() == playerPuckPositions[i].row() + 2 && playerPuckPositions[j].col() == playerPuckPositions[i].col() - 2)) {
                    sameLeftDiagCounter++;
                }
                else if (i != j && playerPuckPositions[i].row() == BOARD_HEIGHT - 1 && playerPuckPositions[i].col() == 0 && playerPuckPositions[i].col() == BOARD_WIDTH - 1 &&  (playerPuckPositions[j].row() == playerPuckPositions[i].row() - 1 && playerPuckPositions[j].col() == playerPuckPositions[i].col() + 1 || playerPuckPositions[j].row() == playerPuckPositions[i].row() - 2 && playerPuckPositions[j].col() == playerPuckPositions[i].col() + 2)) {
                    sameLeftDiagCounter++;
                }
            }

            if (sameLeftDiagCounter > 2 || sameRightDiagCounter > 2) {
                return true;
            }
        }

        return false;
    }

    /**
     * {@return whether the block can be moved to the direction specified}
     *
     * @param direction a direction to which the selected puck is intended to be moved
     * @param PUCK the selected puck
     */
    public boolean canMove(Direction direction, int PUCK) {
        return switch (direction) {
            case UP -> canMoveUp(PUCK);
            case RIGHT -> canMoveRight(PUCK);
            case DOWN -> canMoveDown(PUCK);
            case LEFT -> canMoveLeft(PUCK);
        };
    }

    private boolean canMoveUp(int PUCK) {
        return positions[PUCK].row() > 0 && isEmpty(positions[PUCK].getUp());
    }

    private boolean canMoveRight(int PUCK) {
        return positions[PUCK].col() < BOARD_WIDTH - 1 && isEmpty(positions[PUCK].getRight());
    }

    private boolean canMoveDown(int PUCK) {
        return positions[PUCK].row() < BOARD_HEIGHT - 1 && isEmpty(positions[PUCK].getDown());
    }

    private boolean canMoveLeft(int PUCK) {
        return positions[PUCK].col() > 0 && isEmpty(positions[PUCK].getLeft());
    }

    /**
     * Moves the selected puck to the direction specified.
     *
     * @param direction the direction to which the selected puck is moved
     * @param PUCK the selected puck
     */
    public void move(Direction direction, int PUCK) {
        switch (direction) {
            case UP -> moveUp(PUCK);
            case RIGHT -> moveRight(PUCK);
            case DOWN -> moveDown(PUCK);
            case LEFT -> moveLeft(PUCK);
        }
    }

    private void moveUp(int PUCK) {
        positions[PUCK] = new Position(positions[PUCK].row() - 1, positions[PUCK].col());
    }

    private void moveRight(int PUCK) {
        positions[PUCK] = new Position(positions[PUCK].row(), positions[PUCK].col() + 1);
    }

    private void moveDown(int PUCK) {
        positions[PUCK] = new Position(positions[PUCK].row() + 1, positions[PUCK].col());
    }

    private void moveLeft(int PUCK) {
        positions[PUCK] = new Position(positions[PUCK].row(), positions[PUCK].col() - 1);
    }


    /**
     * {@return the set of directions to which the block can be moved}
     * @return
     */
    public EnumSet<Direction> getLegalMoves(int PUCK) {
        var legalMoves = EnumSet.noneOf(Direction.class);
        for (var direction : Direction.values()) {
            if (canMove(direction, PUCK)) {
                legalMoves.add(direction);
            }
        }
        return legalMoves;
    }

    private boolean isOnBoard(Position position) {
        return position.row() >= 0 && position.row() < BOARD_HEIGHT &&
                position.col() >= 0 && position.col() < BOARD_WIDTH;
    }

    private boolean isEmpty(Position position) {
        for (var p : positions) {
            if (p.equals(position)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        return (o instanceof GameState gs) && Arrays.equals(positions, gs.positions);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(positions);
    }

    @Override
    public GameState clone() {
        GameState copy;
        try {
            copy = (GameState) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
        copy.positions = deepClone(positions);
        return copy;
    }

    @Override
    public String toString() {
        var sj = new StringJoiner(",", "[", "]");
        for (var position : positions) {
            sj.add(position.toString());
        }
        return sj.toString();
    }

    private static Position[] deepClone(Position[] a) {
        Position[] copy = a.clone();
        for (var i = 0; i < a.length; i++) {
            copy[i] = a[i].clone();
        }
        return copy;
    }
}