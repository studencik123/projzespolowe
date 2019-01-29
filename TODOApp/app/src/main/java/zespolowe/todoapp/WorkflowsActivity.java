package zespolowe.todoapp;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.List;

import zespolowe.todoapp.dbo.Workflow;

public class WorkflowsActivity extends AppCompatActivity implements WorkflowsRecyclerViewAdapter.ItemClickListener
{

    WorkflowsRecyclerViewAdapter workflowsRecyclerViewAdapter;
    RecyclerView workflowsRecyclerView;
    LinearLayoutManager linearLayoutManager;
    SwipeController swipeController = null;
    List<Workflow> workflows;
    TasksService service;

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
        getSupportActionBar().setTitle("Workflows");

        service = new TasksService(getApplication());

        workflows = service.getWorkflows();

        setupRecyclerViewAdapter(workflows);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(floatingActionButtonListener);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        workflows.clear();
        workflows.addAll(service.getWorkflows());
        workflowsRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        workflows.clear();
        workflows.addAll(service.getWorkflows());
        workflowsRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        workflows.clear();
        workflows.addAll(service.getWorkflows());
        workflowsRecyclerViewAdapter.notifyDataSetChanged();
    }

    //przejscie do aktywnosci konkretnego workflow
    @Override
    public void onItemClick(View view, int position)
    {
        Intent intent = new Intent(WorkflowsActivity.this, WorkflowViewActivity.class);
        intent.putExtra("workflow_id", workflows.get(position).id);
        intent.putExtra("name", workflows.get(position).name);
        intent.putExtra("workflow", workflows.get(position).workflow);
        startActivity(intent);
    }

    public void setupRecyclerViewAdapter(List<Workflow> workflowList)
    {
        workflowsRecyclerView = findViewById(R.id.workflowsRecyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        workflowsRecyclerView.setLayoutManager(linearLayoutManager);
        workflowsRecyclerViewAdapter = new WorkflowsRecyclerViewAdapter(this, workflowList);
        workflowsRecyclerViewAdapter.setClickListener(this);
        workflowsRecyclerView.setAdapter(workflowsRecyclerViewAdapter);

        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position)
            {
                service.deleteTask(workflows.get(position).id);
                workflowsRecyclerViewAdapter.workflows.remove(position);
                workflowsRecyclerViewAdapter.notifyItemRemoved(position);
                workflowsRecyclerViewAdapter.notifyItemRangeChanged(position, workflowsRecyclerViewAdapter.getItemCount());
            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(workflowsRecyclerView);

        workflowsRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }

    public void goToCreateWorkflowActivity(View view)
    {
        Intent intent = new Intent(this, CreateWorkflowActivity.class);
        startActivity(intent);
    }
}
