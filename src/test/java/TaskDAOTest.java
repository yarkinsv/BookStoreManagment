import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import store.DAO.TaskHibernateDAO;
import store.Model.Task;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TaskDAOTest {

    private TaskHibernateDAO taskDAO;
    private SessionFactory sessionFactory;

    @Before
    public void setUpDBTestBaseClass() {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate_test.cfg.xml");
        StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        sessionFactory = configuration.buildSessionFactory(ssrb.build());
        taskDAO = new TaskHibernateDAO(sessionFactory);
    }

    @Test
    public void insertShouldInsertNewTaskInDBAndReturnTaskWithAssignedId() throws Exception {
        Task task1 = Task.create("Новая книга", "Сделать ревью новой книги", new Date(), false);
        Task task2 = Task.create("Новая книга", "Сделать ревью новой книги", new Date(), false);

        taskDAO.insert(task1);
        taskDAO.insert(task2);

        Task taskFromDB1 = taskDAO.get(task1.getId());
        Task taskFromDB2 = taskDAO.get(task2.getId());

        assertEquals(task1, taskFromDB1);
        assertEquals(task2, taskFromDB2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void insertShouldThrowIllegalArgumentExceptionIfTaskHasId() throws Exception {
        final Task task = Task.existing(1, "Задание", "Описание", new Date(), false);
        taskDAO.insert(task);
    }

    @Test
    public void getShouldReturnTask() throws Exception {

        final Task task = Task.create("Задание", "Описание", new Date(), false);
        taskDAO.insert(task);

        final Task taskFromDB = taskDAO.get(task.getId());

        Assert.assertEquals(task, taskFromDB);
    }

    @Test
    public void getShouldReturnEmptyOptionalIfNoTaskWithSuchId() throws Exception {
        final int nonExistentTaskId = 666;
        final Task taskFromDB = taskDAO.get(nonExistentTaskId);
        assertNull(taskFromDB);
    }

    @Test
    public void getAllShouldReturnAllTasks() throws Exception {

        assertTrue(taskDAO.getAll().isEmpty());

        final Task task1 = Task.create("Задание1", "Описание1", new Date(), false);
        final Task task2 = Task.create("Задание2", "Описание2", new Date(), false);

        taskDAO.insert(task1);
        taskDAO.insert(task2);

        final List<Task> tasksFromDB = taskDAO.getAll();

        Assert.assertEquals(new LinkedList<>(Arrays.asList(task1, task2)), tasksFromDB);
    }

    @Test
    public void updateShouldUpdateTask() throws Exception {

        final Task task = Task.create("Задание", "Описание", new Date(), false);
        taskDAO.insert(task);
        task.setCompleted(true);

        taskDAO.update(task);

        final Task taskFromDB = taskDAO.get(task.getId());
        Assert.assertEquals(task, taskFromDB);
    }

    @Test
    public void deleteShouldDeleteTaskById() throws Exception {
        final Task task1 = Task.create("Задание1", "Описание1", new Date(), false);
        final Task task2 = Task.create("Задание2", "Описание2", new Date(), false);

        taskDAO.insert(task1);
        taskDAO.insert(task2);

        taskDAO.delete(task1.getId());

        assertNull(taskDAO.get(task1.getId()));
        assertNotNull(taskDAO.get(task2.getId()));
    }
}
