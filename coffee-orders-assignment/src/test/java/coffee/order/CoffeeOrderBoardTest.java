package coffee.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CoffeeOrderBoard.
 */
public class CoffeeOrderBoardTest {

    private CoffeeOrderBoard board;

    @BeforeEach
    void setUp() {
        board = new CoffeeOrderBoard();
    }

    @Test
    void testAddReturnsOrder() {
        Order order = board.add("Alice");
        assertEquals(1, order.getOrderNumber());
        assertEquals("Alice", order.getCustomerName());
    }

    @Test
    void testDeliverFromEmptyQueueThrows() {
        assertThrows(IllegalStateException.class, () -> board.deliver());
    }

    @Test
    void testDeliverNonExistingOrderThrows() {
        board.add("Test");
        assertThrows(NoSuchElementException.class, () -> board.deliver(99));
    }

    @Test
    void testOrderNumbersRemainUniqueAfterRemoval() {
        board.add("A");
        board.add("B");
        board.deliver(); // remove first
        board.add("C");

        assertEquals(2, board.deliver().getOrderNumber());
        assertEquals(3, board.deliver().getOrderNumber());
    }

    @Test
    void testQueueIntegrityAfterDeliverByNumber() {
        board.add("A");
        board.add("B");
        board.add("C");

        board.deliver(2);

        assertEquals(1, board.deliver().getOrderNumber());
        assertEquals(3, board.deliver().getOrderNumber());
    }

    @Test
    void testFifoOrder() {
        board.add("A");
        board.add("B");

        assertEquals("A", board.deliver().getCustomerName());
        assertEquals("B", board.deliver().getCustomerName());
    }
}
