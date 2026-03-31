package app.factory;

import app.transport.Plane;
import app.transport.Transport;

/**
 * PlaneFactory creates Plane objects.
 */
public class PlaneFactory extends TransportFactory {
    @Override
    public Transport createTransport() {
        return new Plane();
    }
}
