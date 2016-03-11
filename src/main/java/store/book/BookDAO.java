package store.book;

import java.sql.Connection;
import java.util.List;

/**
 * Data Access Object for Book entity
 */
public interface BookDAO {

    /**
     * Add new Book to the database.
     * @param book book to insert to the database, its id must be null or exception will be thrown
     */
    void insert(Book book);

    /**
     * Add new Book to the database in the provided connection context.
     * @param book
     * @param connection
     */
    void insert(Book book, Connection connection);

    /**
     * Get the Book entity by its id
     * @param bookId id of the book
     * @return Book entity or null if it does not exist
     */
    Book get(int bookId);

    /**
     * Get all books in the database
     * @return List of books
     */
    List<Book> getAll();

    /**
     * Update the book provided
     * @param book book to update, its id must not be null or exception will be thrown
     */
    void update(Book book);

    /**
     * Delete the book from the database
     * @param bookId id of the book to delete, if it does not exist, nothing will happen
     */
    void delete(int bookId);
}
