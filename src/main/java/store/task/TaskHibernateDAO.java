package store.task;

import com.google.inject.Inject;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class TaskHibernateDAO implements TaskDAO {

    private final SessionFactory sessionFactory;

    @Inject
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

        session().save(task);
    }

    @Override
    public Task get(int taskId) {
        Task task = (Task) session().get(Task.class, taskId);
        return task;
    }

    @Override
    public List<Task> getAll() {
        final Criteria criteria = session().createCriteria(Task.class);
        final List<Task> tasks = criteria.list();
        return new LinkedList<>(tasks);
    }

    @Override
    public void update(Task task) {
        session().update(task);
    }

    @Override
    public void delete(int taskId) {
        session().createQuery("DELETE Task WHERE id = :id")
                .setInteger("id", taskId)
                .executeUpdate();
    }
}
