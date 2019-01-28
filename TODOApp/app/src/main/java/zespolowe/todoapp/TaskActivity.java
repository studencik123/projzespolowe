package zespolowe.todoapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

public class TaskActivity extends AppCompatActivity implements View.OnClickListener
{
    int id;
    TextView subjectTextView, stateTextView;
    Button doneTaskButton, delayTaskButton;

    String subject, state;
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
    public void onClick(View v)
    {
        // default method for handling onClick Events..
        switch (v.getId())
        {

            case R.id.button_done:
                // zmiana stanu tasku na done
                break;

            case R.id.button_delay:
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
    }

    public void setupView()
    {
        subjectTextView = (TextView) findViewById(R.id.textView_subject);
        subjectTextView.setText(subject);

        stateTextView = (TextView) findViewById(R.id.textView_state);
        stateTextView.setText(state);

        //todo notes mapa data
        doneTaskButton = (Button) findViewById(R.id.button_done);
        delayTaskButton = (Button) findViewById(R.id.button_delay);
        doneTaskButton.setOnClickListener(this);
        delayTaskButton.setOnClickListener(this);
    }
}
