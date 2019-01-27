package zespolowe.todoapp.dbo;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "workflows")
public class Workflow {
    @PrimaryKey
    @NonNull
    public int id;

    @NonNull
    public String name;

    @NonNull
    public String workflow;
}
