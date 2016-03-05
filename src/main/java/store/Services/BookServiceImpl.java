package store.Services;

import com.google.inject.Inject;
import org.hibernate.Transaction;
import store.DAO.BookDAO;
import store.DAO.TaskDAO;
import store.Model.Book;
import store.Model.Task;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class BookServiceImpl implements BookService {

    private BookDAO bookDAO;
    private TaskDAO taskDAO;

    @Inject
    public BookServiceImpl(BookDAO bookDAO, TaskDAO taskDAO) {
        this.bookDAO = bookDAO;
        this.taskDAO = taskDAO;
    }

    @Override
    public int registerNewBook(Book book) {
        Task newTask = Task.create("Сделать ревью книги " + book.getTitle(),
                "Написать описание для сайта по книге " + book.getTitle() + " автор " + book.getAuthor(),
                new Date(), false);
        Transaction tran = taskDAO.openTransaction();

        try {
            taskDAO.insertNoCommit(newTask);
            bookDAO.insert(book, taskDAO.getCurrentConnection());
            tran.commit();
        } catch (RuntimeException e) {
            tran.rollback();
        }

        return newTask.getId();
    }
}
