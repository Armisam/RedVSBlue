package redvsblue.state;

import org.junit.jupiter.api.Test;
import java.util.EnumSet;
import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {

    GameState state1 = new GameState(); // the original initial state

    GameState state2 = new GameState(new Position(1, 2),
            new Position(1, 0),
            new Position(1, 1),
            new Position(2, 1),
            new Position(2, 2),
            new Position(3, 0),
            new Position(0, 2),
            new Position(2, 3)); // a winning state

    GameState state3 = new GameState(new Position(1, 1),
            new Position(2, 0),
            new Position(3, 1),
            new Position(4, 3),
            new Position(4, 1),
            new Position(2, 2),
            new Position(0, 3),
            new Position(3, 0)); // a no winning state

    @Test
    void testConstructor_invalid() {
        assertThrows(IllegalArgumentException.class, () -> new GameState(new Position(0, 0))); //too few arguments
        assertThrows(IllegalArgumentException.class, () -> new GameState(new Position(0, 0),
                new Position(1, 1),
                new Position(2, 2),
                new Position(3, 3),
                new Position(4, 4),
                new Position(5, 5),
                new Position(6, 6),
                new Position(7, 7)

        )); // position is not on the board
        assertThrows(IllegalArgumentException.class, () -> new GameState(new Position(1, 1),
                new Position(1, 1),
                new Position(1, 1),
                new Position(1, 1),
                new Position(1, 1),
                new Position(1, 1),
                new Position(1, 1),
                new Position(1, 1)
        )); // a position is added more than once
    }

    @Test
    void isWon() {
        assertFalse(state1.isWon());
        assertTrue(state2.isWon());
        assertFalse(state3.isWon());
    }

    @Test
    void canMove_state1() {
        assertFalse(state1.canMove(Direction.UP, 0));
        assertFalse(state1.canMove(Direction.RIGHT,0));
        assertTrue(state1.canMove(Direction.DOWN,0));
        assertFalse(state1.canMove(Direction.LEFT,0));
    }

    @Test
    void canMove_state2() {
        assertFalse(state2.canMove(Direction.UP,0));
        assertTrue(state2.canMove(Direction.RIGHT,0));
        assertFalse(state2.canMove(Direction.DOWN,0));
        assertFalse(state2.canMove(Direction.LEFT,0));
    }

    @Test
    void canMove_state3() {
        assertTrue(state3.canMove(Direction.UP,0));
        assertTrue(state3.canMove(Direction.RIGHT,0));
        assertTrue(state3.canMove(Direction.DOWN,0));
        assertTrue(state3.canMove(Direction.LEFT,0));
    }

    @Test
    void move_state1_right() {
        var copy = state1.clone();
        state1.move(Direction.RIGHT,0);
        assertFalse(state3.equals(copy));
    }

    @Test
    void move_state1_down() {
        var copy = state1.clone();
        state1.move(Direction.DOWN ,0);
        assertFalse(state3.equals(copy));
    }

    @Test
    void move_state3_up() {
        var copy = state3.clone();
        state3.move(Direction.UP,0);
        assertFalse(state3.equals(copy));
    }

    @Test
    void move_state3_right() {
        var copy = state3.clone();
        state3.move(Direction.RIGHT,0);
        assertFalse(state3.equals(copy));
    }

    @Test
    void move_state3_down() {
        var copy = state3.clone();
        state3.move(Direction.DOWN,0);
        assertFalse(state3.equals(copy));
    }

    @Test
    void move_state3_left() {
        var copy = state3.clone();
        state3.move(Direction.LEFT,0);
        assertFalse(state3.equals(copy));
    }

    @Test
    void getLegalMoves() {
        assertEquals(EnumSet.of(Direction.DOWN), state1.getLegalMoves(0));
        assertEquals(EnumSet.of(Direction.RIGHT), state2.getLegalMoves(0));
        assertEquals(EnumSet.allOf(Direction.class), state3.getLegalMoves(0));
    }

    @Test
    void testEquals() {
        assertTrue(state1.equals(state1));

        var clone = state1.clone();
        clone.move(Direction.DOWN,0);
        assertFalse(clone.equals(state1));

        assertFalse(state1.equals(null));
        assertFalse(state1.equals("Hello, World!"));
        assertFalse(state1.equals(state2));
    }

    @Test
    void testHashCode() {
        assertTrue(state1.hashCode() == state1.hashCode());
        assertTrue(state1.hashCode() == state1.clone().hashCode());
    }

    @Test
    void testClone() {
        var clone = state1.clone();
        assertTrue(clone.equals(state1));
        assertNotSame(clone, state1);
    }

    @Test
    void testToString() {
        assertEquals("[(0,1),(0,3),(4,0),(4,2),(0,0),(0,2),(4,1),(4,3)]", state1.toString());
        assertEquals("[(1,2),(1,0),(1,1),(2,1),(2,2),(3,0),(0,2),(2,3)]", state2.toString());
        assertEquals("[(1,1),(2,0),(3,1),(4,3),(4,1),(2,2),(0,3),(3,0)]", state3.toString());
    }

}