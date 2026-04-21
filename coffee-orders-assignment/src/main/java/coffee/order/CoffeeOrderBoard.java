package coffee.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * CoffeeOrderBoard simulates a queue of coffee orders in a coffee shop.
 */
public class CoffeeOrderBoard {
    private static final Logger logger = LoggerFactory.getLogger(CoffeeOrderBoard.class);

    private final LinkedList<Order> orders = new LinkedList<>();
    private int nextOrderNumber = 1;

    /**
     * Adds a new order to the queue.
     * @param customerName name of the customer
     * @return created Order
     */
    public Order add(String customerName) {
        if (customerName == null || customerName.isBlank()) {
            throw new IllegalArgumentException("Customer name cannot be null or blank");
        }

        Order order = new Order(nextOrderNumber++, customerName);
        orders.add(order);

        logger.info("Added new order: {}", order);
        return order;
    }

    /**
     * Delivers the next order in the queue.
     * @return delivered order
     * @throws IllegalStateException if queue is empty
     */
    public Order deliver() {
        if (orders.isEmpty()) {
            IllegalStateException ex = new IllegalStateException("No orders in queue");
            logger.warn("Attempt to deliver from empty queue");
            throw ex;
        }
        Order order = orders.removeFirst();
        logger.info("Delivered order: {}", order);
        return order;
    }

    /**
     * Delivers an order by its number.
     * @param orderNumber number of the order to deliver
     * @return delivered order
     * @throws NoSuchElementException if order not found
     */
    public Order deliver(int orderNumber) {
        Iterator<Order> iterator = orders.iterator();

        while (iterator.hasNext()) {
            Order order = iterator.next();
            if (order.getOrderNumber() == orderNumber) {
                iterator.remove();
                logger.info("Delivered order by number: {}", order);
                return order;
            }
        }

        NoSuchElementException ex = new NoSuchElementException("Order not found: " + orderNumber);
        logger.error("Order not found: {}", orderNumber, ex);
        throw ex;
    }

    /**
     * Prints the current state of the queue.
     */
    public void draw() {
        System.out.println("Num | Name");
        for (Order order : orders) {
            System.out.printf("%d | %s%n", order.getOrderNumber(), order.getCustomerName());
        }
        logger.debug("Queue drawn with {} orders", orders.size());
    }
}
