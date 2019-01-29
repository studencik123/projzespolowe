package zespolowe.todoapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

public class TaskActivity extends AppCompatActivity implements View.OnClickListener
{
    int id;
    TextView subjectTextView, stateTextView, workflowTextView, descriptionTextView;
    Button doneTaskButton, delayTaskButton;

    String subject, state, workflowText, description;
    Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        retrieveIntent();
        setupView();
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
                // zmiana stanu tasku na done
                break;

            case R.id.button_2:
                // zmiana stanu tasku na delay
                break;

            case R.id.button_3:
                // zmiana stanu tasku na delay
                break;

            case R.id.button_4:
                // zmiana stanu tasku na delay
                break;

            default:
                break;
        }
    }

    public void retrieveIntent()
    {
        id = getIntent().getExtras().getInt("task_id");
        subject = getIntent().getExtras().getString("subject");
        state = getIntent().getExtras().getString("state");
        date = new Date(getIntent().getExtras().getLong("date"));
        workflowText = getIntent().getExtras().getString("workflow");
        description = getIntent().getExtras().getString("desc");
    }

    public void setupView()
    {
        subjectTextView = (TextView) findViewById(R.id.textView_subject);
        subjectTextView.setText(subject);

        stateTextView = (TextView) findViewById(R.id.textView_state);
        stateTextView.setText(state);

        descriptionTextView = (TextView) findViewById(R.id.textView_desc);
        descriptionTextView.setText(description);

        //todo notes mapa data
        doneTaskButton = (Button) findViewById(R.id.button_1);
        delayTaskButton = (Button) findViewById(R.id.button_2);
        doneTaskButton.setOnClickListener(this);
        delayTaskButton.setOnClickListener(this);
    }

    public void goToTaskEditActivity()
    {

    }
}
