package task;

import com.google.inject.Guice;
import com.google.inject.Injector;
import module.BookStoreTestModule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import store.task.Task;
import store.task.TaskService;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TaskServiceTest {

    private TaskService taskService;

    @Before
    public void setUpDBTestBaseClass() {
        Injector injector = Guice.createInjector(new BookStoreTestModule());
        taskService = injector.getInstance(TaskService.class);
    }

    @Test
    public void insertShouldInsertNewTaskInDBAndReturnTaskWithAssignedId() throws Exception {
        Task task1 = Task.create("Новая книга", "Сделать ревью новой книги", new Date(), false);
        Task task2 = Task.create("Новая книга", "Сделать ревью новой книги", new Date(), false);

        taskService.insert(task1);
        taskService.insert(task2);

        Task taskFromDB1 = taskService.get(task1.getId());
        Task taskFromDB2 = taskService.get(task2.getId());

        assertEquals(task1, taskFromDB1);
        assertEquals(task2, taskFromDB2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void insertShouldThrowIllegalArgumentExceptionIfTaskHasId() throws Exception {
        final Task task = Task.existing(1, "Задание", "Описание", new Date(), false);
        taskService.insert(task);
    }

    @Test
    public void getShouldReturnTask() throws Exception {

        final Task task = Task.create("Задание", "Описание", new Date(), false);
        taskService.insert(task);

        final Task taskFromDB = taskService.get(task.getId());

        Assert.assertEquals(task, taskFromDB);
    }

    @Test
    public void getShouldReturnEmptyOptionalIfNoTaskWithSuchId() throws Exception {
        final int nonExistentTaskId = 666;
        final Task taskFromDB = taskService.get(nonExistentTaskId);
        assertNull(taskFromDB);
    }

    @Test
    public void getAllShouldReturnAllTasks() throws Exception {

        assertTrue(taskService.getAll().isEmpty());

        final Task task1 = Task.create("Задание1", "Описание1", new Date(), false);
        final Task task2 = Task.create("Задание2", "Описание2", new Date(), false);

        taskService.insert(task1);
        taskService.insert(task2);

        final List<Task> tasksFromDB = taskService.getAll();

        Assert.assertEquals(new LinkedList<>(Arrays.asList(task1, task2)), tasksFromDB);
    }

    @Test
    public void updateShouldUpdateTask() throws Exception {

        final Task task = Task.create("Задание", "Описание", new Date(), false);
        taskService.insert(task);
        task.setCompleted(true);

        taskService.update(task);

        final Task taskFromDB = taskService.get(task.getId());
        Assert.assertEquals(task, taskFromDB);
    }

    @Test
    public void deleteShouldDeleteTaskById() throws Exception {
        final Task task1 = Task.create("Задание1", "Описание1", new Date(), false);
        final Task task2 = Task.create("Задание2", "Описание2", new Date(), false);

        taskService.insert(task1);
        taskService.insert(task2);

        taskService.delete(task1.getId());

        assertNull(taskService.get(task1.getId()));
        assertNotNull(taskService.get(task2.getId()));
    }
}
