package app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Scanner;

/**
 * Console-based entry point for managing the shopping cart.
 * Coordinates user input with domain services.
 * Business logic remains in Product/Cart/Repository classes.
 */
@Component
public class CartConsoleRunner implements CommandLineRunner {
    private final ApplicationContext context;
    private final ProductRepository productRepository;

    public CartConsoleRunner(ApplicationContext context, ProductRepository productRepository) {
        this.context = context;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        System.out.println("Welcome to Cart Management Service!");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            displayMainMenu();
            String choice = scanner.nextLine().trim();

            if ("2".equals(choice)) {
                System.out.println("Exiting application. Goodbye!");
                break;
            }

            if ("1".equals(choice)) {
                // Fetching a new Cart bean from the Spring context.
                // Due to @Scope("prototype"), this ALWAYS returns a fresh instance.
                Cart currentCart = context.getBean(Cart.class);
                handleCartSession(scanner, currentCart);
            } else {
                System.out.println("⚠️ Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private void displayMainMenu() {
        System.out.println("\n=== Main Menu ===");
        System.out.println("1. Start new shopping session (New Cart)");
        System.out.println("2. Exit");
        System.out.print("Enter choice: ");
    }

    private void handleCartSession(Scanner scanner, Cart cart) {
        while (true) {
            displayAvailableProducts();
            displayCartContents(cart);
            displayCartActions();

            String action = scanner.nextLine().trim();
            if ("0".equals(action)) {
                break; // Return to main menu
            }

            Long productId = readProductId(scanner);
            if (productId == null) {
                continue;
            }

            executeCartAction(action, productId, cart);
        }
    }

    private void displayAvailableProducts() {
        System.out.println("\n--- Available Products ---");
        productRepository.findAll().forEach(System.out::println);
    }

    private void displayCartContents(Cart cart) {
        System.out.println("\n--- Your Cart ---");
        if (cart.getItems().isEmpty()) {
            System.out.println("🛒 Cart is empty.");
        } else {
            cart.getItems().forEach(System.out::println);
            System.out.printf("💰 Total: $%.2f (Items: %d)%n",
                    cart.getTotalPrice(), cart.getItemCount());
        }
    }

    private void displayCartActions() {
        System.out.println("\n=== Cart Actions ===");
        System.out.println("5.1 Add product by ID");
        System.out.println("5.2 Remove product by ID");
        System.out.println("0. Back to Main Menu");
        System.out.print("Enter action: ");
    }

    private Long readProductId(Scanner scanner) {
        System.out.print("Enter Product ID: ");
        try {
            return Long.parseLong(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("⚠️ Invalid ID format. Please enter a valid number.");
            return null;
        }
    }

    private void executeCartAction(String action, Long productId, Cart cart) {
        switch (action) {
            case "5.1" -> {
                // Lookup product in repository FIRST, then add to cart
                // This keeps Cart independent of persistence logic
                productRepository.findById(productId)
                        .map(cart::addProduct)
                        .ifPresentOrElse(
                                added -> System.out.println(added ? "✅ Product added to cart." : "❌ Failed to add product."),
                                () -> System.out.println("❌ Product not found.")
                        );
            }
            case "5.2" -> {
                boolean removed = cart.removeProduct(productId);
                System.out.println(removed ? "✅ Product removed from cart." : "❌ Product not in cart.");
            }
            default -> System.out.println("⚠️ Invalid action. Use 5.1, 5.2, or 0.");
        }
    }
}