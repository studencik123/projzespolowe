package zespolowe.todoapp;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import zespolowe.todoapp.dbo.Workflow;

public class CreateWorkflowActivity extends AppCompatActivity implements View.OnClickListener
{
    TextInputLayout wNameTextInputCreate, wTextInputCreate;
    TextInputEditText wNameTextInputEditCreate, wTextInputEditCreate;
    Button createButton;

    TasksService service;

    String wName, wText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workflow);

        service = new TasksService(getApplication());

        setupView();
    }

    @Override
    public void onClick(View view)
    {
        createWorkflow();

        Snackbar snackbar = Snackbar.make(view, "Workflow has been created", Snackbar.LENGTH_LONG);
        snackbar.show();

        finish();
    }

    public void createWorkflow()
    {
        wName = wNameTextInputEditCreate.getText().toString();
        wText = wTextInputEditCreate.getText().toString();

        Workflow workflow = new Workflow();

        workflow.name = wName;
        workflow.workflow = wText;

        service.addWorkflow(workflow);
    }

    public void setupView()
    {
        wNameTextInputCreate = (TextInputLayout) findViewById(R.id.workflow_name_input_create);
        wNameTextInputEditCreate = (TextInputEditText) findViewById(R.id.workflow_name_inputEdit_create);

        wTextInputCreate = (TextInputLayout) findViewById(R.id.workflow_text_input_create);
        wTextInputEditCreate = (TextInputEditText) findViewById(R.id.workflow_text_inputEdit_create);

        createButton = (Button) findViewById(R.id.button_create);
        createButton.setOnClickListener(this);
    }
}
