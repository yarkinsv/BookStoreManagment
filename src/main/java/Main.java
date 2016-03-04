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

    public static void main(final String... args) throws IOException,SQLException {
/*
        final Properties properties = PropertiesFactory.load();

        final DataSource dataSource = createPGSimpleDataSource(
                properties.getProperty("jdbc.server"),
                Integer.valueOf(properties.getProperty("jdbc.port")),
                properties.getProperty("jdbc.user"),
                properties.getProperty("jdbc.password"),
                properties.getProperty("jdbc.database"),
                properties.getProperty("jdbc.schema")
        );
*/
        BookService service = new BookServiceImpl(
                new BookJdbcDAO(null),
                new TaskHibernateDAO(HibernateSessionFactory.createSessionFactory())
        );

        Book book = Book.create("New book", "Author", 25);

        service.registerNewBook(book);
    }

    private static void play(final BookDAO bookDAO) {
        //bookDAO.insert(Book.create("Игра в бисер", "Герман Гессе", 450));
        bookDAO.delete(2);


    }

    private Main() {
    }
}