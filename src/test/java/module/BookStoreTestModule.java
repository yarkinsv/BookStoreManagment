package module;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import store.BookStoreModuleBase;

import javax.sql.DataSource;

public class BookStoreTestModule extends BookStoreModuleBase {

    @Provides
    @Singleton
    public DataSource provideDataSource() {
        DataSource database = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("create-tables.sql")
                .build();
        return database;
    }

    @Provides
    @Singleton
    public SessionFactory provideSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate_test.cfg.xml");
        StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        SessionFactory sessionFactory = configuration.buildSessionFactory(ssrb.build());
        return sessionFactory;
    }
}
