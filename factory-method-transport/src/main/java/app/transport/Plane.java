package app.transport;

/**
 * Plane is a concrete implementation of Transport.
 */
public class Plane implements Transport {
    @Override
    public void move() {
        System.out.println("The plane is flying in the sky.");
    }
}
