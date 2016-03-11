package store;

import com.google.inject.AbstractModule;
import store.book.BookDAO;
import store.book.BookJdbcDAO;
import store.book.BookService;
import store.book.BookServiceImpl;
import store.task.TaskDAO;
import store.task.TaskHibernateDAO;
import store.task.TaskService;

public class BookStoreModuleBase extends AbstractModule {
    @Override
    protected void configure() {
        bind(BookDAO.class).to(BookJdbcDAO.class);
        bind(TaskDAO.class).to(TaskHibernateDAO.class);
        bind(BookService.class).to(BookServiceImpl.class);
        bind(TaskService.class);
    }
}
