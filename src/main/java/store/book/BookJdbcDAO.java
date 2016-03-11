package store.book;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

import static java.util.Objects.requireNonNull;

/**
 * Pure JDBC implementation of a BookDAO
 */
public class BookJdbcDAO implements BookDAO {

    private final DataSource dataSource;
    private static final Logger log = LoggerFactory.getLogger(BookDAO.class);

    @Inject
    public BookJdbcDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insert(Book book) {
        if (book.getId() != null) {
            throw new IllegalArgumentException("Can not save " + book + " with already assigned id");
        }

        try (final Connection connection = dataSource.getConnection()) {
            persistBook(book, connection);
        } catch (final SQLException e) {
            throw new RuntimeException("failed to persist " + book, e);
        }

        log.info("New book has been saved: id={}, title={}", book.getId(), book.getTitle());
    }

    public void insert(Book book, Connection connection) {
        if (book.getId() != null) {
            throw new IllegalArgumentException("Can not save " + book + " with already assigned id");
        }

        try {
            persistBook(book, connection);
        } catch (final SQLException e) {
            throw new RuntimeException("failed to persist " + book, e);
        }
    }

    private void persistBook(Book book, Connection connection) throws SQLException {
        final String query = "INSERT INTO books (title, author, pages_count) VALUES (?, ?, ?)";
        try (final PreparedStatement statement =
                     connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setInt(3, book.getPagesCount());

            statement.executeUpdate();

            try (final ResultSet generatedKeys = statement.getGeneratedKeys()) {
                generatedKeys.next();
                book.setId(generatedKeys.getInt(1));
            }
        }
    }

    public Book get(int bookId) {
        try (final Connection connection = dataSource.getConnection()) {
            final String query = "SELECT title, author, pages_count FROM books WHERE id = ?";
            try (final PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, bookId);

                statement.executeQuery();

                try (final ResultSet resultSet = statement.getResultSet()) {
                    if (resultSet.next()) {
                        Book book = Book.existing(bookId, resultSet.getString(1), resultSet.getString(2), resultSet.getInt(3));
                        return book;
                    } else {
                        return null;
                    }
                }
            }
        } catch (final SQLException e) {
            throw new RuntimeException("failed to get book with id " + bookId, e);
        }
    }

    public List<Book> getAll() {
        try (final Connection connection = dataSource.getConnection()) {
            final String query = "SELECT id, title, author, pages_count FROM books";
            try (final Statement statement = connection.createStatement()) {
                statement.executeQuery(query);

                try (final ResultSet resultSet = statement.getResultSet()) {
                    List<Book> result = new LinkedList<>();

                    while (resultSet.next()) {
                        Book book = Book.existing(
                                resultSet.getInt(1),
                                resultSet.getString(2),
                                resultSet.getString(3),
                                resultSet.getInt(4));
                        result.add(book);
                    }

                    return result;
                }
            }
        } catch (final SQLException e) {
            throw new RuntimeException("failed to get all books", e);
        }
    }

    public void update(Book book) {
        if (book.getId() == null) {
            throw new IllegalArgumentException("Can not update " + book + " without already assigned id");
        }

        try (final Connection connection = dataSource.getConnection()) {
            final String query = "UPDATE books SET title = ?, author = ?, pages_count = ? WHERE id = ?";
            try (final PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, book.getTitle());
                statement.setString(2, book.getAuthor());
                statement.setInt(3, book.getPagesCount());
                statement.setInt(4, book.getId());

                statement.executeUpdate();
            }
        } catch (final SQLException e) {
            throw new RuntimeException("failed to persist " + book, e);
        }
    }

    public void delete(int bookId) {
        try (final Connection connection = dataSource.getConnection()) {
            final String query = "DELETE FROM books WHERE id = ?";
            try (final PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, bookId);
                statement.executeUpdate();
            }
        } catch (final SQLException e) {
            throw new RuntimeException("failed to get book with id " + bookId, e);
        }
    }
}
