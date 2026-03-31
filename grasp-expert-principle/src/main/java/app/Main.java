package app;

/**
 * Main class to demonstrate the GRASP Expert principle.
 */
public class Main {
    public static void main(String[] args) {
        User user = new User("Alice");
        Address address = new Address("123 Main St", "Dnipro", "Ukraine");

        user.setAddress(address);

        // Demonstration of Expert principle: User manages its own address
        System.out.println(user);
    }
}