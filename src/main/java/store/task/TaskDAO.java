package store.task;

import org.hibernate.Transaction;

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
}
