package zespolowe.todoapp.dbo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import zespolowe.todoapp.workflow.Workflow;
import zespolowe.todoapp.workflow.XmlParser;

@Entity(tableName = "tasks")
public class Task {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    public String state;

    @NonNull
    public String subject;

    @NonNull
    @ColumnInfo(name = "workflow")
    public String xmlWorkflow;

    @Ignore
    public Workflow workflow;

    public Workflow GetWorkflow() {
        if (workflow == null)
            workflow = XmlParser.parse(xmlWorkflow);

        return workflow;
    }
}
