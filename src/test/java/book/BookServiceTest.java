package book;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import store.book.BookJdbcDAO;
import store.task.TaskHibernateDAO;
import store.book.Book;
import store.book.BookService;
import store.book.BookServiceImpl;
import store.task.TaskService;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.*;


public class BookServiceTest {
    private TaskService taskService;
    private SessionFactory sessionFactory;
    private BookJdbcDAO bookDAO;
    private BookService service;

    @Before
    public void setUpDBTestBaseClass() {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate_test.cfg.xml");
        StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        sessionFactory = configuration.buildSessionFactory(ssrb.build());
        taskService = new TaskService(sessionFactory, new TaskHibernateDAO(sessionFactory));
        Transaction tran = taskService.beginTransaction().get();
        Connection connection = taskService.getConnection();
        try {
            connection.createStatement().execute("DROP TABLE IF EXISTS public.Books");
            connection.createStatement().execute("DROP TABLE IF EXISTS public.Tasks");
            connection.createStatement().execute("CREATE TABLE public.Tasks ( id SERIAL PRIMARY KEY, name TEXT, description TEXT, create_date TIMESTAMP, completed BOOLEAN)");
            connection.createStatement().execute("CREATE TABLE public.Books ( id SERIAL PRIMARY KEY, title TEXT, author TEXT, pages_count INT)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tran.commit();
        bookDAO = new BookJdbcDAO(null);
        service = new BookServiceImpl(bookDAO, taskService);
    }

    @Test
    public void registerNewBookMustCreateBookAndTask() {
        Book book = Book.create("Над пропастью во ржи", "Селинджер", 12);

        int taskId = service.registerNewBook(book);

        Transaction tran = taskService.beginTransaction().get();
        Connection connection = taskService.getConnection();
        try {
            Statement stmt = connection.createStatement();
            stmt.execute("SELECT * FROM Books WHERE id = " + book.getId());
            assertTrue(stmt.getResultSet().next());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            tran.commit();
        }

        assertEquals("Сделать ревью книги Над пропастью во ржи", taskService.get(taskId).getName());
    }

    @Test
    public void ifFailInsertBookThenWholeTransactionMustBeRolledBack() {
        Book book = Book.create("Над пропастью во ржи", "Селинджер", 12);

        bookDAO = null;
        service = new BookServiceImpl(bookDAO, taskService);

        try {
            service.registerNewBook(book);
        } catch (RuntimeException e) {
            //Мы сами вызвали исключение - так что проглотим и проверим далее на то, что в базе ничего не сохранилось
        }

        Transaction tran = taskService.beginTransaction().get();
        Connection connection = taskService.getConnection();
        try {
            Statement stmt = connection.createStatement();
            stmt.execute("SELECT * FROM Books");
            assertFalse(stmt.getResultSet().next());

            stmt = connection.createStatement();
            stmt.execute("SELECT * FROM Tasks");
            assertFalse(stmt.getResultSet().next());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            tran.commit();
        }
    }

    @After
    public void clearDB() {
        Transaction tran = taskService.beginTransaction().get();
        Connection connection = taskService.getConnection();
        try {
            connection.createStatement().execute("DROP TABLE public.Books");
            connection.createStatement().execute("DROP TABLE public.Tasks");
            tran.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
