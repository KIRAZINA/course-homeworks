package app.client;

import app.factory.CarFactory;
import app.factory.PlaneFactory;
import app.factory.TransportFactory;
import app.transport.Transport;

/**
 * Client class demonstrates the Factory Method design pattern.
 *
 * The client code works only with the abstract factory (TransportFactory)
 * and the abstract product (Transport) interfaces. It does not depend on
 * concrete classes like Car, Plane, CarFactory, or PlaneFactory.
 */
public class Client {

    /**
     * Runs the demonstration of the Factory Method pattern.
     * Creates different types of transport using their respective factories
     * and invokes their behavior without knowing the concrete implementations.
     */
    public void run() {
        // Create car using CarFactory
        TransportFactory carFactory = new CarFactory();
        Transport car = carFactory.createTransport();
        car.move();

        // Create plane using PlaneFactory
        TransportFactory planeFactory = new PlaneFactory();
        Transport plane = planeFactory.createTransport();
        plane.move();

        System.out.println("\n--- Factory Method Pattern executed successfully ---");
    }

    /**
     * Main method to start the demonstration.
     */
    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }
}