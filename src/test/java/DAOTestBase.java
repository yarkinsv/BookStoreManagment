import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import store.DAO.BookDAO;
import store.DAO.BookJdbcDAO;

import javax.sql.DataSource;

public class DAOTestBase {

    private static JdbcTemplate jdbcTemplate;
    protected static BookDAO bookJdbcDAO;

    @BeforeClass
    public static void setUpDBTestBaseClass() throws Exception {
        Injector injector = Guice.createInjector(new BookStoreTestModule());
        DataSource dataSource = injector.getInstance(DataSource.class);
        jdbcTemplate = new JdbcTemplate(dataSource);
        bookJdbcDAO = injector.getInstance(BookDAO.class);
    }

    @After
    public void tearDownDBTestBase() throws Exception {
        jdbcTemplate.update("DELETE FROM books");
        jdbcTemplate.update("DELETE FROM tasks");
    }

    @AfterClass
    public static void tearDownDBTestBaseClass() throws Exception {
        ((EmbeddedDatabase)jdbcTemplate.getDataSource()).shutdown();
    }
}
