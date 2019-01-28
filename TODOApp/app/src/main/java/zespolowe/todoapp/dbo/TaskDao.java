package zespolowe.todoapp.dbo;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Query("DELETE FROM tasks WHERE id = :id")
    void delete(int id);

    @Query("SELECT * from tasks ORDER BY id ASC")
    List<Task> getAllTasks();

    @Query("SELECT * FROM tasks WHERE id = :id LIMIT 1")
    Task getTask(int id);

    @Query("DELETE FROM tasks")
    void deleteAll();
}

