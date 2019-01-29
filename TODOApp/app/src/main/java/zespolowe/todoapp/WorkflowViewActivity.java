package zespolowe.todoapp;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import zespolowe.todoapp.dbo.Workflow;

public class WorkflowViewActivity extends AppCompatActivity implements View.OnClickListener
{
    TextInputLayout nameTextInput, workflowTextInput;
    TextInputEditText nameTextInputEdit, workflowTextInputEdit;
    Button editButton;

    TasksService service;
    Workflow workflow;
    int id;
    String name, workflowText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workflow_view);

        service = new TasksService(getApplication());

        retrieveIntent();
        setupView();
    }

    @Override
    public void onClick(View view)
    {
        name = nameTextInputEdit.getText().toString();
        workflowText = workflowTextInputEdit.getText().toString();

        workflow.name = name;
        workflow.workflow = workflowText;

        service.updateWorkflow(workflow);

        Snackbar snackbar = Snackbar.make(view, "Workflow has been updated", Snackbar.LENGTH_LONG);
        snackbar.show();

        finish();
    }

    public void retrieveIntent()
    {
        id = getIntent().getExtras().getInt("workflow_id");
        name = getIntent().getExtras().getString("name");
        workflowText = getIntent().getExtras().getString("workflow");
        workflow = service.getWorkflow(id);
    }

    public void setupView()
    {
        nameTextInput = (TextInputLayout) findViewById(R.id.workflow_name_input);
        nameTextInputEdit = (TextInputEditText) findViewById(R.id.workflow_name_inputEdit);
        nameTextInputEdit.setText(name);

        workflowTextInput = (TextInputLayout) findViewById(R.id.workflow_text_input);
        workflowTextInputEdit = (TextInputEditText) findViewById(R.id.workflow_text_inputEdit);
        workflowTextInputEdit.setText(workflowText);

        editButton = (Button) findViewById(R.id.button_confirm);
        editButton.setOnClickListener(this);
    }
}