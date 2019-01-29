package zespolowe.todoapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import zespolowe.todoapp.dbo.Task;

public class TaskActivity extends AppCompatActivity implements View.OnClickListener
{
    int id;
    TextView subjectTextView, stateTextView, workflowTextView, descriptionTextView, dateTextView;
    Button firstTaskButton, secondTaskButton, thirdTaskButton, fourthTaskButton, fifthTaskButton;
    TasksService service;
    Task task;

    String subject, state, workflowText, description;
    Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        service = new TasksService(getApplication());

        retrieveIntent();
        setupView();
        updateButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task, menu);
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
        if (id == R.id.action_task_edit)
        {
            goToTaskEditActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v)
    {
        // default method for handling onClick Events..

        switch (v.getId())
        {
            case R.id.button_1:
                service.transition(id, firstTaskButton.getText().toString(), getApplicationContext());
                break;

            case R.id.button_2:
                service.transition(id, secondTaskButton.getText().toString(), getApplicationContext());
                break;

            case R.id.button_3:
                service.transition(id, thirdTaskButton.getText().toString(), getApplicationContext());
                break;

            case R.id.button_4:
                service.transition(id, fourthTaskButton.getText().toString(), getApplicationContext());
                break;

            case R.id.button_5:
                service.transition(id, fifthTaskButton.getText().toString(), getApplicationContext());
                break;

            default:
                break;
        }

        updateView();
        updateButtons();
    }

    public void retrieveIntent()
    {
        id = getIntent().getExtras().getInt("task_id");
        subject = getIntent().getExtras().getString("subject");
        state = getIntent().getExtras().getString("state");
        date = new Date(getIntent().getExtras().getLong("date"));
        workflowText = getIntent().getExtras().getString("workflow");
        description = getIntent().getExtras().getString("desc");

        task = service.getTask(id);
    }

    public void updateButtons()
    {
        List<String> states = service.getAvailableStates(id);
        System.out.println("TUTAJ" + states);

        firstTaskButton.setVisibility(View.GONE);
        secondTaskButton.setVisibility(View.GONE);
        thirdTaskButton.setVisibility(View.GONE);
        fourthTaskButton.setVisibility(View.GONE);
        fifthTaskButton.setVisibility(View.GONE);

        if(states.size() == 0)
        {
            //donothing
        }
        if(states.size() >= 1)
        {
            firstTaskButton.setVisibility(View.VISIBLE);
            firstTaskButton.setText(states.get(0));
        }
        if(states.size() >= 2)
        {
            secondTaskButton.setVisibility(View.VISIBLE);
            secondTaskButton.setText(states.get(1));
        }
        if(states.size() >= 3)
        {
            thirdTaskButton.setVisibility(View.VISIBLE);
            thirdTaskButton.setText(states.get(2));

        }
        if(states.size() >= 4)
        {
            fourthTaskButton.setVisibility(View.VISIBLE);
            fourthTaskButton.setText(states.get(3));
        }
        if(states.size() >= 5)
        {
            fifthTaskButton.setVisibility(View.VISIBLE);
            fifthTaskButton.setText(states.get(4));
        }
    }

    public void setupView()
    {
        subjectTextView = (TextView) findViewById(R.id.textView_subject);
        subjectTextView.setText(subject);

        stateTextView = (TextView) findViewById(R.id.textView_state);
        stateTextView.setText(state);

        descriptionTextView = (TextView) findViewById(R.id.textView_desc);
        descriptionTextView.setText(description);

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        dateTextView = (TextView) findViewById(R.id.textView_date);
        if (task.date != null)
            dateTextView.setText(df.format(task.date));

        //todo data
        firstTaskButton = (Button) findViewById(R.id.button_1);
        secondTaskButton = (Button) findViewById(R.id.button_2);
        thirdTaskButton = (Button) findViewById(R.id.button_3);
        fourthTaskButton = (Button) findViewById(R.id.button_4);
        fifthTaskButton = (Button) findViewById(R.id.button_5);

        firstTaskButton.setVisibility(View.GONE);
        secondTaskButton.setVisibility(View.GONE);
        thirdTaskButton.setVisibility(View.GONE);
        fourthTaskButton.setVisibility(View.GONE);
        fifthTaskButton.setVisibility(View.GONE);

        firstTaskButton.setOnClickListener(this);
        secondTaskButton.setOnClickListener(this);
        thirdTaskButton.setOnClickListener(this);
        fourthTaskButton.setOnClickListener(this);
        fifthTaskButton.setOnClickListener(this);
    }

    public void updateView()
    {
        task = service.getTask(id);
        subject = task.subject;
        state = task.state;
        description = task.description;

        subjectTextView.setText(subject);
        stateTextView.setText(state);
        descriptionTextView.setText(description);
    }

    public void goToTaskEditActivity()
    {

    }
}
