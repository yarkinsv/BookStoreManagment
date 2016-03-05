package store;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.hibernate.SessionFactory;
import store.DAO.BookDAO;
import store.DAO.BookJdbcDAO;
import store.DAO.TaskDAO;
import store.DAO.TaskHibernateDAO;
import store.Services.BookService;
import store.Services.BookServiceImpl;
import utils.HibernateSessionFactory;
import utils.PropertiesFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import static utils.DataSourceFactory.createPGSimpleDataSource;

public class BookStoreModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(BookDAO.class).to(BookJdbcDAO.class);
        bind(TaskDAO.class).to(TaskHibernateDAO.class);
        bind(BookService.class).to(BookServiceImpl.class);
    }

    @Provides
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
    public SessionFactory provideSessionFactory() {
        return HibernateSessionFactory.createSessionFactory();
    }
}
