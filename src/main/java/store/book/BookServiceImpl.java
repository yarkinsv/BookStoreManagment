package store.book;

import com.google.inject.Inject;
import org.hibernate.Transaction;
import store.task.TaskDAO;
import store.task.Task;
import store.task.TaskService;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class BookServiceImpl implements BookService {

    private BookDAO bookDAO;
    private TaskService taskService;

    @Inject
    public BookServiceImpl(BookDAO bookDAO, TaskService taskService) {
        //Я не проверяю на null специально для того, чтобы в тестах симулировать NullPointerException
        //Только для того, чтобы проверить общую транзакцию
        this.bookDAO = bookDAO;
        this.taskService = taskService;
    }

    @Override
    public int registerNewBook(Book book) {
        Task newTask = Task.create("Сделать ревью книги " + book.getTitle(),
                "Написать описание для сайта по книге " + book.getTitle() + " автор " + book.getAuthor(),
                new Date(), false);

        Optional<Transaction> tran = taskService.beginTransaction();

        try {
            taskService.insertWithoutTransaction(newTask);
            bookDAO.insert(book, taskService.getConnection());
            tran.ifPresent(Transaction::commit);
        } catch (RuntimeException e) {
            tran.ifPresent(Transaction::rollback);
            throw e;
        }

        return newTask.getId();
    }
}
