import com.google.inject.Guice;
import com.google.inject.Injector;
import store.BookStoreModule;
import store.DAO.BookDAO;
import store.DAO.BookJdbcDAO;
import store.Model.Book;
import store.Model.Task;
import store.DAO.TaskHibernateDAO;
import store.Services.BookService;
import store.Services.BookServiceImpl;
import utils.HibernateSessionFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

class Main {

    public static void main(final String... args) {
        Injector injector = Guice.createInjector(new BookStoreModule());
        BookService service = injector.getInstance(BookService.class);
        BookDAO bookDAO = injector.getInstance(BookDAO.class);
        bookDAO.insert(Book.create("Игра в бисер", "Герман Гессе", 450));
    }
}