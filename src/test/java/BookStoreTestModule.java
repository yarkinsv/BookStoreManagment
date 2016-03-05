import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
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

import static utils.DataSourceFactory.createPGSimpleDataSource;

public class BookStoreTestModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(BookDAO.class).to(BookJdbcDAO.class);
        bind(TaskDAO.class).to(TaskHibernateDAO.class);
        bind(BookService.class).to(BookServiceImpl.class);
    }

    @Provides
    public DataSource provideDataSource() {
        DataSource database = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("create-tables.sql")
                .build();
        return database;
    }

    @Provides
    public SessionFactory provideSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate_test.cfg.xml");
        StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        SessionFactory sessionFactory = configuration.buildSessionFactory(ssrb.build());
        return sessionFactory;
    }
}
