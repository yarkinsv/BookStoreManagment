package store.DAO;

import org.hibernate.Transaction;
import store.Model.Task;

import java.sql.Connection;
import java.util.List;

/**
 * Data Access Object for Task entity.
 */
public interface TaskDAO {

    /**
     * Add new Task to the database.
     * @param task task to add, its id must be null or exception will be thrown.
     */
    void insert(Task task);

    /**
     * Add new Task to the database. And returns opened connection with uncommited transaction.
     * @param task
     * @return Connection with uncommited transaction of inserted record.
     */
    void insertNoCommit(Task task);

    /**
     * Get existing task from the database.
     * @param taskId task id
     * @return corresponding task entity or null if it does not exist by provided id
     */
    Task get(int taskId);

    /**
     * Get all tasks from DB
     * @return List of tasks
     */
    List<Task> getAll();

    /**
     * Update all fields of existing task
     * @param task task to be updated, its id must be not null or exception will be thrown
     */
    void update(Task task);

    /**
     * Delete task by its id. If id doesn't exist nothing will happen
     * @param taskId id of a task to be deleted
     */
    void delete(int taskId);

    /**
     * Get active connection after insertNoCommit method executed
     * @return
     */
    Connection getCurrentConnection();

    /**
     * Get active transaction after insertNoCommit method executed
     * @return
     */
    Transaction openTransaction();
}
