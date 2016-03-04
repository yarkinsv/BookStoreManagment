package store.DAO;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.jdbc.Work;
import store.Model.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class TaskHibernateDAO implements TaskDAO {

    private final SessionFactory sessionFactory;
    private Connection connection;
    private Transaction transaction;

    public TaskHibernateDAO(final SessionFactory sessionFactory) {
        this.sessionFactory = requireNonNull(sessionFactory);
    }

    private Session session() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void insert(Task task) {
        if (task.getId() != null) {
            throw new IllegalArgumentException("can not save " + task + " with assigned id");
        }

        Transaction tran = session().beginTransaction();
        session().save(task);
        tran.commit();
    }

    @Override
    public void insertNoCommit(Task task) {
        if (task.getId() != null) {
            throw new IllegalArgumentException("can not save " + task + " with assigned id");
        }

        session().save(task);
    }

    @Override
    public Task get(int taskId) {
        Transaction tran = session().beginTransaction();
        Task task = (Task) session().get(Task.class, taskId);
        tran.commit();
        return task;
    }

    @Override
    public List<Task> getAll() {
        Transaction tran = session().beginTransaction();
        final Criteria criteria = session().createCriteria(Task.class);
        final List<Task> tasks = criteria.list();
        tran.commit();
        return new LinkedList<>(tasks);
    }

    @Override
    public void update(Task task) {
        Transaction tran = session().beginTransaction();
        session().update(task);
        tran.commit();
    }

    @Override
    public void delete(int taskId) {
        Transaction tran = session().beginTransaction();
        session().createQuery("DELETE Task WHERE id = :id")
                .setInteger("id", taskId)
                .executeUpdate();
        tran.commit();
    }

    public Connection getCurrentConnection() {
        session().doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                TaskHibernateDAO.this.connection = connection;
            }
        });
        return this.connection;
    }

    public Transaction openTransaction() {
        return session().beginTransaction();
    }
}
