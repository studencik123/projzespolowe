package zespolowe.todoapp;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class WorkflowViewActivity extends AppCompatActivity implements View.OnClickListener
{
    TextInputLayout nameTextInput, workflowTextInput;
    TextInputEditText nameTextInputEdit, workflowTextInputEdit;
    Button editButton;

    int id;
    String name, workflow;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workflow_view);

        retrieveIntent();
        setupView();
    }

    @Override
    public void onClick(View v)
    {
        //todo update workflow
    }

    public void retrieveIntent()
    {
        id = getIntent().getExtras().getInt("workflow_id");
        name = getIntent().getExtras().getString("name");
        workflow = getIntent().getExtras().getString("workflow");
    }

    public void setupView()
    {
        nameTextInput = (TextInputLayout) findViewById(R.id.workflow_name_input);
        nameTextInputEdit = (TextInputEditText) findViewById(R.id.workflow_name_inputEdit);
        nameTextInputEdit.setText(name);

        workflowTextInput = (TextInputLayout) findViewById(R.id.workflow_text_input);
        workflowTextInputEdit = (TextInputEditText) findViewById(R.id.workflow_text_inputEdit);
        workflowTextInputEdit.setText(workflow);

        editButton = (Button) findViewById(R.id.button_confirm);
        editButton.setOnClickListener(this);
    }
}