package zespolowe.todoapp;

import android.app.Application;

import java.util.List;

import zespolowe.todoapp.dbo.Task;
import zespolowe.todoapp.dbo.TaskDao;
import zespolowe.todoapp.dbo.TaskRoomDatabase;
import zespolowe.todoapp.dbo.Workflow;
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

    public void addTask(Task task) {
        Workflow workflow = workflowDao.getWorkflow(task.type);
        task.xmlWorkflow = workflow.workflow;
        task.state = task.GetWorkflow().state;
        taskDao.insert(task);
    }

    public void updateTask(Task task) { taskDao.update(task); }

    public void deleteTask(int taskId) {
        taskDao.delete(taskId);
    }

    public Task getTask(int id) {
        return taskDao.getTask(id);
    }

    public List<String> getAvailableStates(int taskId) {
        Task task = getTask(taskId);
        return TransitionRunner.getAvailableStates(task);
    }

    public void transition(int taskId, String stateTo) {
        Task task = getTask(taskId);
        for (Transition transition : task.workflow.transitions) {
            if (transition.from.equals(task.state) && transition.to.equals(stateTo)){
                TransitionRunner.runTransition(task, transition);
                break;
            }
        }
    }

    public List<Workflow> getWorkflows() {
        return workflowDao.getWorkflows();
    }

    public void addWorkflow(Workflow workflow) {
        workflowDao.insert(workflow);
    }

    public void updateWorkflow(Workflow workflow) {
        workflowDao.update(workflow);
    }

    public void deleteWorkflow(int workflowId) {
        workflowDao.delete(workflowId);
    }
}
