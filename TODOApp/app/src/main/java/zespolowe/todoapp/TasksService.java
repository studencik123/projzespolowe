package zespolowe.todoapp;

import android.app.Application;

import java.util.List;

import zespolowe.todoapp.dbo.Task;
import zespolowe.todoapp.dbo.TaskDao;
import zespolowe.todoapp.dbo.TaskRoomDatabase;
import zespolowe.todoapp.dbo.WorkflowDao;
import zespolowe.todoapp.workflow.Transition;

public class TasksService {
    private TaskDao taskDao;
    private WorkflowDao workflowDao;

    public TasksService(Application application) {
        TaskRoomDatabase db = TaskRoomDatabase.getDatabase(application);
        taskDao = db.taskDao();
        workflowDao = db.workflowDao();
    }

    public List<Task> getList() {
        return taskDao.getAllTasks();
    }

    public void AddTask(Task task) {
        taskDao.insert(task);
    }

    public Task GetTask(int id) {
        return taskDao.getTask(id);
    }

    public void Transition(int taskId, String stateTo) {
        Task task = GetTask(taskId);
        for (Transition transition : task.workflow.transitions) {
            if (transition.from.equals(task.state) && transition.to.equals(stateTo)){
                TransitionRunner.runTransition(task, transition);
                break;
            }
        }
    }
}
