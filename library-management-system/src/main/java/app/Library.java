package app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a library that stores unique books.
 */
public class Library {
    private final List<Book> books = new ArrayList<>();

    public Library() {
        // explicit constructor for clarity
    }

    /**
     * Adds a book to the library.
     * @param book Book to add (must not be null).
     * @throws IllegalArgumentException if book is null or already exists.
     */
    public void addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        if (books.contains(book)) {
            throw new IllegalArgumentException("Book already exists in the library");
        }
        books.add(book);
    }

    /**
     * Removes a book from the library.
     * @param book Book to remove (must not be null).
     * @return true if removed, false if not found.
     */
    public boolean removeBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        return books.remove(book); // relies on equals()
    }

    /**
     * Returns an unmodifiable list of books.
     * @return List of books.
     */
    public List<Book> getBooks() {
        return Collections.unmodifiableList(books);
    }

    /**
     * Returns the number of books in the library.
     * @return Book count.
     */
    public int getBookCount() {
        return books.size();
    }
}
