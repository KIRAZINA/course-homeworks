package app.transport;

/**
 * Car is a concrete implementation of Transport.
 */
public class Car implements Transport {
    @Override
    public void move() {
        System.out.println("The car is driving on the road.");
    }
}
