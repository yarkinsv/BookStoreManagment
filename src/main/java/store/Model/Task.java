package store.Model;

import javax.persistence.*;
import java.util.Date;


/**
 * Represents Task entity by its Id, Name, Description, Create Date, and Completed properties.
 * Contains appropriate attributes for Hibernate's configuration of the table in DB.
 */
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "completed")
    private boolean completed;

    Task() {}

    public static Task create(final String name, final String description,
                              final Date createDate, final boolean completed) {
        return new Task(null, name, description, createDate, completed);
    }

    public static Task existing(final int id, final String name, final String description,
                                final Date createDate, final boolean completed) {
        return new Task(id, name, description, createDate, completed);
    }

    private Task(final Integer id, final String name, final String description,
                 final Date createDate, final boolean completed) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
        this.setcreateDate(createDate);
        this.setCompleted(completed);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getcreateDate() {
        return createDate;
    }

    public void setcreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (completed != task.completed) return false;
        if (id != null ? !id.equals(task.id) : task.id != null) return false;
        if (name != null ? !name.equals(task.name) : task.name != null) return false;
        if (description != null ? !description.equals(task.description) : task.description != null) return false;
        return createDate != null ? createDate.equals(task.createDate) : task.createDate == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "store.Model.Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createDate=" + createDate +
                ", completed=" + completed +
                '}';
    }
}