package store.task;

import com.google.inject.Inject;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class TaskService {

    private final SessionFactory sessionFactory;
    private final TaskDAO taskDAO;

    @Inject
    public TaskService(final SessionFactory sessionFactory, final TaskDAO taskDAO) {
        this.sessionFactory = requireNonNull(sessionFactory);
        this.taskDAO = taskDAO;
    }

    public void insert(Task task) {
        inTransaction(() -> taskDAO.insert(task));
    }

    /**
     * Just for the purpose of the homework to be able to manage Hibernate transaction in outer Book service.
     * @param task
     */
    public void insertWithoutTransaction(Task task) {
        taskDAO.insert(task);
    }

    public Task get(int taskId) {
        return inTransaction(() -> taskDAO.get(taskId));
    }

    public List<Task> getAll() {
        return inTransaction(taskDAO::getAll);
    }

    public void update(Task task) {
        inTransaction(() -> taskDAO.update(task));
    }

    public void delete(int taskId) {
        inTransaction(() -> taskDAO.delete(taskId));
    }

    private <T> T inTransaction(final Supplier<T> supplier) {
        final Optional<Transaction> transaction = beginTransaction();
        try {
            final T result = supplier.get();
            transaction.ifPresent(Transaction::commit);
            return result;
        } catch (RuntimeException e) {
            transaction.ifPresent(Transaction::rollback);
            throw e;
        }
    }

    private void inTransaction(final Runnable runnable) {
        inTransaction(() -> {
            runnable.run();
            return null;
        });
    }

    //Не очень круто делать public, но как мне кажется это самый простой способ скрестить Hibernate и Jdbc в одной транзакции
    public Optional<Transaction> beginTransaction() {
        final Transaction transaction = sessionFactory.getCurrentSession().getTransaction();
        if (!transaction.isActive()) {
            transaction.begin();
            return Optional.of(transaction);
        }
        return Optional.empty();
    }

    //Не нашел более адекватного способа забрать коннекшн на временную базу данных из Hibernate
    //Нужно для выполнения общей транзакции между Hibernate и Jdbc провайдером
    public Connection getConnection() {
        ConnectionGetter connectionGetter = new ConnectionGetter();
        sessionFactory.getCurrentSession().doWork(connectionGetter);
        return connectionGetter.connection;
    }

    private class ConnectionGetter implements Work {
        public Connection connection;

        @Override
        public void execute(Connection connection) throws SQLException {
            this.connection = connection;
        }
    }
}
