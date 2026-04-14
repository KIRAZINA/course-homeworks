package app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/**
 * Unit tests for Library class.
 */
public class LibraryTest {

    @Test
    void testAddBook() {
        Library library = new Library();
        Book book = new Book("1984", "George Orwell");
        library.addBook(book);
        assertEquals(1, library.getBookCount());
        assertTrue(library.getBooks().contains(book));
    }

    @Test
    void testAddNullBookThrowsException() {
        Library library = new Library();
        assertThrows(IllegalArgumentException.class, () -> library.addBook(null));
    }

    @Test
    void testAddDuplicateBookThrowsException() {
        Library library = new Library();
        Book book1 = new Book("1984", "George Orwell");
        Book book2 = new Book("1984", "George Orwell");
        library.addBook(book1);
        assertThrows(IllegalArgumentException.class, () -> library.addBook(book2));
    }

    @Test
    void testRemoveBookByEqualButDifferentInstance() {
        Library library = new Library();
        Book original = new Book("1984", "George Orwell");
        library.addBook(original);

        Book sameBook = new Book("1984", "George Orwell"); // different instance
        assertTrue(library.removeBook(sameBook));
        assertEquals(0, library.getBookCount());
    }

    @Test
    void testRemoveNonExistingBook() {
        Library library = new Library();
        Book book = new Book("1984", "George Orwell");
        assertFalse(library.removeBook(book));
    }

    @Test
    void testRemoveNullBookThrowsException() {
        Library library = new Library();
        assertThrows(IllegalArgumentException.class, () -> library.removeBook(null));
    }

    @Test
    void testGetBooksIsUnmodifiable() {
        Library library = new Library();
        Book book = new Book("1984", "George Orwell");
        library.addBook(book);
        List<Book> books = library.getBooks();
        assertThrows(UnsupportedOperationException.class,
                () -> books.add(new Book("Brave New World", "Aldous Huxley")));
    }

    @Test
    void testBookEqualsAndHashCode() {
        Book book1 = new Book("1984", "George Orwell");
        Book book2 = new Book("1984", "George Orwell");
        assertEquals(book1, book2);
        assertEquals(book1.hashCode(), book2.hashCode());
    }

    @Test
    void testBookToString() {
        Book book = new Book("1984", "George Orwell");
        assertTrue(book.toString().contains("1984"));
        assertTrue(book.toString().contains("George Orwell"));
    }

    @Test
    void testGetBooksReturnsDefensiveCopy() {
        Library library = new Library();
        Book book = new Book("1984", "George Orwell");
        library.addBook(book);

        List<Book> returnedBooks = library.getBooks();

        assertThrows(UnsupportedOperationException.class,
                () -> returnedBooks.add(new Book("Test", "Author")));

        assertEquals(1, library.getBookCount());
    }
}
