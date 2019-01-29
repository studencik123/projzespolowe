package zespolowe.todoapp.dbo;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface WorkflowDao {
        @Insert
        void insert(Workflow workflow);

        @Update
        void update(Workflow workflow);

        @Query("DELETE FROM workflows WHERE id = :id")
        void delete(int id);

        @Query("SELECT * from workflows ORDER BY id ASC")
        List<Workflow> getWorkflows();

        @Query("SELECT * FROM workflows WHERE id = :id LIMIT 1")
        Workflow getWorkflow(int id);

        @Query("SELECT * FROM workflows WHERE name LIKE :name LIMIT 1")
        Workflow getWorkflow(String name);

        @Query("DELETE FROM workflows")
        void deleteAll();
}