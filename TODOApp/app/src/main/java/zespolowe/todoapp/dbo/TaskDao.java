package zespolowe.todoapp.dbo;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    void insert(Task task);

    @Query("DELETE FROM tasks WHERE id = :id")
    void delete(int id);

    @Query("SELECT * from tasks ORDER BY id ASC")
    List<Task> getAllTasks();

    @Query("SELECT * FROM tasks WHERE id = :id ")
    Task getTask(int id);

    @Query("DELETE FROM tasks")
    void deleteAll();
}

