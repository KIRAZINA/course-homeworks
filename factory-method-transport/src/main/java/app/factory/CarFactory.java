package app.factory;

import app.transport.Car;
import app.transport.Transport;

/**
 * CarFactory creates Car objects.
 */
public class CarFactory extends TransportFactory {
    @Override
    public Transport createTransport() {
        return new Car();
    }
}
