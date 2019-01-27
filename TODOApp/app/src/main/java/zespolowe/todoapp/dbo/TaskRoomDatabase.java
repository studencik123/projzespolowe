package zespolowe.todoapp.dbo;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = { Task.class, Workflow.class }, version = 2, exportSchema = false)
public abstract class TaskRoomDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
    public abstract WorkflowDao workflowDao();

    private static volatile TaskRoomDatabase INSTANCE;

    public static TaskRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TaskRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TaskRoomDatabase.class, "task_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Override the onOpen method to populate the database.
     * For this sample, we clear the database every time it is created or opened.
     *
     * If you want to populate the database only when the database is created for the 1st time,
     * override RoomDatabase.Callback()#onCreate
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            // If you want to keep the data through app restarts,
            // comment out the following line.
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    /**
     * Populate the database in the background.
     * If you want to start with more words, just add them.
     */
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final TaskDao taskDao;
        private final WorkflowDao workflowDao;

        PopulateDbAsync(TaskRoomDatabase db) {
            taskDao = db.taskDao();
            workflowDao = db.workflowDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            taskDao.deleteAll();
            workflowDao.deleteAll();

            String xml = "<workflow>\n" +
                    "<state name=\"start\"/>\n" +
                    "<state name=\"next\"/>\n" +
                    "<transition from=\"start\" to=\"next\">\n" +
                    "<action type=\"equals\" field=\"subject\" value=\"temat\"/>\n" +
                    "<action type=\"set\" field=\"subject\" value=\"nowy temat\"/>\n" +
                    "</transition>\n" +
                    "</workflow>";

            Workflow workflow = new Workflow();
            workflow.name = "testowe";
            workflow.workflow = xml;
            workflowDao.insert(workflow);

            Task task = new Task();
            task.subject = "Visiting Doctor House";
            task.xmlWorkflow = xml;
            taskDao.insert(task);

            Task task2 = new Task();
            task2.subject = "Repairing car radio";
            task2.xmlWorkflow = xml;
            taskDao.insert(task2);

            Task task3 = new Task();
            task3.subject = "Shopping groceries";
            task3.xmlWorkflow = xml;
            taskDao.insert(task3);
            return null;
        }
    }
}