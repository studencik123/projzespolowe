package zespolowe.todoapp.dbo;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface WorkflowDao {
        @Insert
        void insert(Workflow workflow);

        @Query("DELETE FROM workflows WHERE id = :id")
        void delete(int id);

        @Query("SELECT * from workflows ORDER BY id ASC")
        List<Workflow> getWorkflows();

        @Query("SELECT * FROM workflows WHERE id = :id ")
        Workflow getWorkflow(int id);

        @Query("DELETE FROM workflows")
        void deleteAll();
}