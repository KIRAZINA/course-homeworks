package app;

/**
 * User acts as Information Expert for its own address
 * according to GRASP Expert principle.
 * User is responsible for storing and providing access to its address.
 */
public class User {
    private final String name;
    private Address address;   // User is the expert for its address

    public User(String name) {
        this.name = name;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("User: %s, Address: %s", name, address);
    }
}
