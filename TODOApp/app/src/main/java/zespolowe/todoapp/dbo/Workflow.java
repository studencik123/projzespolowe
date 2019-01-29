package zespolowe.todoapp.dbo;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "workflows", indices = @Index(value = "name", unique = true))
public class Workflow {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    @NonNull
    public String name;

    @NonNull
    public String workflow;
}
