package app.factory;

import app.transport.Transport;

/**
 * Abstract factory class that declares the factory method.
 */
public abstract class TransportFactory {
    public abstract Transport createTransport();
}
