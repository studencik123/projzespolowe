package zespolowe.todoapp;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TasksRecyclerViewAdapter.ItemClickListener
{
    private static final int RC_LIST_STRING = 10001;

    TasksRecyclerViewAdapter tasksRecyclerViewAdapter;
    RecyclerView tasksRecyclerView;
    LinearLayoutManager linearLayoutManager;
    SwipeController swipeController = null;
    ArrayList<Task> tasks;

    //Przycisk przejscia do tworzenia taska
    private FloatingActionButton floatingActionButton;
    private View.OnClickListener floatingActionButtonListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            goToCreateTaskActivity(view);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.tasks = new ArrayList<>();
        addTasks(tasks);

        //setup adaptera listy taskow
        setupRecyclerViewAdapter(tasks);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(floatingActionButtonListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(View view, int position)
    {
        goToTaskActivity(view);
    }

    public void setupRecyclerViewAdapter(ArrayList<Task> taskList)
    {
        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        tasksRecyclerView.setLayoutManager(linearLayoutManager);
        tasksRecyclerViewAdapter = new TasksRecyclerViewAdapter(this, taskList);
        tasksRecyclerViewAdapter.setClickListener(this);
        tasksRecyclerView.setAdapter(tasksRecyclerViewAdapter);

        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                tasksRecyclerViewAdapter.tasks.remove(position);
                tasksRecyclerViewAdapter.notifyItemRemoved(position);
                tasksRecyclerViewAdapter.notifyItemRangeChanged(position, tasksRecyclerViewAdapter.getItemCount());
            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(tasksRecyclerView);

        tasksRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }

    //narazie taski w ten sposob
    public void addTasks(ArrayList<Task> tasks)
    {
        Task task = new Task();
        task.setTaskName("Visiting Doctor House");
        task.setTaskType("Doctor Appointment");
        task.setTaskDate("10/12/2018");
        tasks.add(task);

        Task task2 = new Task();
        task2.setTaskName("Repairing car radio");
        task2.setTaskType("Car Repair");
        task2.setTaskDate("12/12/2018");
        tasks.add(task2);

        Task task3 = new Task();
        task3.setTaskName("Shopping groceries");
        task3.setTaskType("Shopping");
        task3.setTaskDate("13/12/2018");
        tasks.add(task3);
    }

    //todo gdy bedzie DB z taskami tutaj operacja dodania nowego taska do DB
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // Check which request we're responding to
        if (requestCode == RC_LIST_STRING) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // get the list of strings here

                // do operations on the list
            }
        }
    }

    //przejscie do aktywnosci tworzenia Taska
    public void goToCreateTaskActivity(View view)
    {
        Intent intent = new Intent(MainActivity.this, CreateTaskActivity.class);
        startActivity(intent);
    }

    //przejscie do aktywnosci konkretnego Taska
    public void goToTaskActivity(View view)
    {
        Intent intent = new Intent(MainActivity.this, TaskActivity.class);
        startActivity(intent);
    }
}
