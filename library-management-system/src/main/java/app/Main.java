package app;

/**
 * Main class to demonstrate the functionality of the Library and Book classes.
 * This class serves as a simple console demonstration.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=== Library System Demo ===\n");

        // Create a new library
        Library library = new Library();

        // Create some books
        Book book1 = new Book("1984", "George Orwell");
        Book book2 = new Book("To Kill a Mockingbird", "Harper Lee");
        Book book3 = new Book("The Great Gatsby", "F. Scott Fitzgerald");

        System.out.println("Adding books to the library...");

        // Adding books
        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);

        System.out.println("Books successfully added.");
        System.out.println("Current number of books: " + library.getBookCount() + "\n");

        // Display all books
        System.out.println("Books in the library:");
        for (Book book : library.getBooks()) {
            System.out.println("  - " + book);
        }
        System.out.println();

        // Demonstrate removing a book
        System.out.println("Removing '" + book2 + "' from the library...");
        boolean removed = library.removeBook(book2);
        System.out.println("Removal successful: " + removed);
        System.out.println("Books left: " + library.getBookCount() + "\n");

        // Show remaining books
        System.out.println("Remaining books:");
        for (Book book : library.getBooks()) {
            System.out.println("  - " + book);
        }
        System.out.println();

        // Demonstrate adding a duplicate book (should throw exception)
        System.out.println("Trying to add a duplicate book (same as book1)...");
        try {
            Book duplicate = new Book("1984", "George Orwell");
            library.addBook(duplicate);
        } catch (IllegalArgumentException e) {
            System.out.println("Expected exception caught: " + e.getMessage());
        }
        System.out.println();

        // Demonstrate null handling
        System.out.println("Trying to add null book...");
        try {
            library.addBook(null);
        } catch (IllegalArgumentException e) {
            System.out.println("Expected exception caught: " + e.getMessage());
        }

        System.out.println("\n=== Demo completed successfully! ===");
    }
}