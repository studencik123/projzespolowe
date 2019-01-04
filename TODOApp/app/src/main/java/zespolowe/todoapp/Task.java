package zespolowe.todoapp;

public class Task
{
    private String taskName, taskNotes;

    //zanim zrobimy wlasny typ to string
    private String taskType;
    private String taskDate;

    //TODO przyda sie data + lokalizacja

    public String getTaskName()
    {
        return taskName;
    }

    public void setTaskName(String taskName)
    {
        this.taskName = taskName;
    }

    public String getTaskNotes()
    {
        return taskNotes;
    }

    public void setTaskNotes(String taskNotes)
    {
        this.taskNotes = taskNotes;
    }

    public String getTaskType()
    {
        return taskType;
    }

    public void setTaskType(String taskType)
    {
        this.taskType = taskType;
    }

    public String getTaskDate()
    {
        return taskDate;
    }

    public void setTaskDate(String taskDate)
    {
        this.taskDate = taskDate;
    }
}
