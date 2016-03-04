import org.junit.Test;
import store.Model.Book;
import store.DAO.BookJdbcDAO;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class BookDAOTest extends DAOTestBase {

    private static final BookJdbcDAO bookJdbcDAO = new BookJdbcDAO(database);

    @Test
    public void insertShouldInsertNewBookInDBAndReturnBookWithAssignedId() throws Exception {

        final Book book1 = Book.create("Игра в бисер", "Герман Гессе", 450);
        final Book book2 = Book.create("Игра в классики", "Хулио Кортасар", 320);

        bookJdbcDAO.insert(book1);
        bookJdbcDAO.insert(book2);

        final Book book1FromDB = bookJdbcDAO.get(book1.getId());
        assertEquals(book1, book1FromDB);

        final Book book2FromDB = bookJdbcDAO.get(book2.getId());
        assertEquals(book2, book2FromDB);
    }

    @Test(expected = IllegalArgumentException.class)
    public void insertShouldThrowIllegalArgumentExceptionIfBookHasId() throws Exception {
        final Book book = Book.existing(1, "Игра в бисер", "Герман Гессе", 350);
        bookJdbcDAO.insert(book);
    }

    @Test
    public void getShouldReturnBook() throws Exception {

        final Book book = Book.create("Игра в бисер", "Герман Гессе", 540);
        bookJdbcDAO.insert(book);

        final Book bookFromDB = bookJdbcDAO.get(book.getId());

        assertEquals(book, bookFromDB);
    }

    @Test
    public void getShouldReturnEmptyOptionalIfNoBookWithSuchId() throws Exception {
        final int nonExistentBookId = 666;
        final Book bookFromDB = bookJdbcDAO.get(nonExistentBookId);
        assertNull(bookFromDB);
    }

    @Test
    public void getAllShouldReturnAllBooks() throws Exception {

        assertTrue(bookJdbcDAO.getAll().isEmpty());

        final Book book1 = Book.create("Игра в бисер", "Герма Гессе", 450);
        final Book book2 = Book.create("Игра в классики", "Хулио Кортасар", 320);

        bookJdbcDAO.insert(book1);
        bookJdbcDAO.insert(book2);

        final List<Book> booksFromDB = bookJdbcDAO.getAll();

        assertEquals(new LinkedList<>(Arrays.asList(book1, book2)), booksFromDB);
    }

    @Test
    public void updateShouldUpdateBook() throws Exception {

        final Book book = Book.create("Игра в бисер", "Герма", 450);
        bookJdbcDAO.insert(book);
        book.setAuthor("Герман Гессе");

        bookJdbcDAO.update(book);

        final Book bookFromDB = bookJdbcDAO.get(book.getId());
        assertEquals(book, bookFromDB);
    }

    @Test
    public void deleteShouldDeleteBookById() throws Exception {
        final Book book1 = Book.create("Игра в бисер", "Герма Гессе", 450);
        final Book book2 = Book.create("Игра в классики", "Хулио Кортасар", 320);

        bookJdbcDAO.insert(book1);
        bookJdbcDAO.insert(book2);

        bookJdbcDAO.delete(book1.getId());

        assertNull(bookJdbcDAO.get(book1.getId()));
        assertNotNull(bookJdbcDAO.get(book2.getId()));
    }
}
