package store;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.hibernate.SessionFactory;
import store.book.BookDAO;
import store.book.BookJdbcDAO;
import store.task.TaskDAO;
import store.task.TaskHibernateDAO;
import store.book.BookService;
import store.book.BookServiceImpl;
import store.task.TaskService;
import utils.HibernateSessionFactory;
import utils.PropertiesFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

import static utils.DataSourceFactory.createPGSimpleDataSource;

public class BookStoreModule extends BookStoreModuleBase {

    @Provides
    @Singleton
    public DataSource provideDataSource() {
        final Properties properties;
        try {
            properties = PropertiesFactory.load();

            final DataSource dataSource = createPGSimpleDataSource(
                    properties.getProperty("jdbc.server"),
                    Integer.valueOf(properties.getProperty("jdbc.port")),
                    properties.getProperty("jdbc.user"),
                    properties.getProperty("jdbc.password"),
                    properties.getProperty("jdbc.database"),
                    properties.getProperty("jdbc.schema")
            );

            return dataSource;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Provides
    @Singleton
    public SessionFactory provideSessionFactory() {
        return HibernateSessionFactory.createSessionFactory();
    }
}
