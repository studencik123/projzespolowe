package zespolowe.todoapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class WorkflowsActivity extends AppCompatActivity
{

    WorkflowsRecyclerViewAdapter workflowsRecyclerViewAdapter;
    RecyclerView tasksRecyclerView;

    private FloatingActionButton floatingActionButton;
    private View.OnClickListener floatingActionButtonListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            goToCreateWorkflowActivity(view);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workflows);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(floatingActionButtonListener);
    }

    public void goToCreateWorkflowActivity(View view)
    {

    }
}
