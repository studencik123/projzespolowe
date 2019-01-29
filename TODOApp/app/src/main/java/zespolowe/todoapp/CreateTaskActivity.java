package zespolowe.todoapp;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import zespolowe.todoapp.dbo.Task;
import zespolowe.todoapp.dbo.Workflow;

public class CreateTaskActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    TextInputLayout taskNameInput, taskNotesInput;
    TextInputEditText taskSubjectInputCreate, taskNotesInputCreate;

    TextView dateTextView;
    Button datePickButton;
    Spinner taskTypesSpinner;

    TasksService service;

    Calendar calendar;
    DatePickerDialog datePickerDialog;

    //Przycisk finalizacji tworzenia Taska
    private FloatingActionButton floatingActionButton;
    private View.OnClickListener floatingActionButtonListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            createTaskButtonAction(view);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        service = new TasksService(getApplication());

        setupView();
        setupSpinnerItems();
        setupDatePicker();

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fabCreateTask);
        floatingActionButton.setOnClickListener(floatingActionButtonListener);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {
        String text = adapterView.getItemAtPosition(i).toString();
        Snackbar.make(view, text, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {

    }

    //tworzenie taska + dodac wyjscie z aktywnosci i dodanie do listy w DB
    private void createTaskButtonAction(View view)
    {
        createTask();

        Snackbar snackbar = Snackbar.make(view, "Workflow has been created", Snackbar.LENGTH_LONG);
        snackbar.show();

        finish();
    }

    public void createTask()
    {
        Task task = new Task();
        Date date = new Date();
        task.subject = taskSubjectInputCreate.getText().toString();
        task.type = taskTypesSpinner.getSelectedItem().toString();
        task.description = taskNotesInputCreate.getText().toString();
        task.date = date;

        service.addTask(task);
    }

    public void setupView()
    {
        taskNameInput = (TextInputLayout) findViewById(R.id.task_name_input);
        taskSubjectInputCreate = (TextInputEditText) findViewById(R.id.task_name_input_create);
        taskNotesInput = (TextInputLayout) findViewById(R.id.task_notes_input);
        taskNotesInputCreate = (TextInputEditText) findViewById(R.id.task_notes_input_create);
        dateTextView = (TextView) findViewById(R.id.task_date_text);
        datePickButton = (Button) findViewById(R.id.button_date);
        taskTypesSpinner = (Spinner) findViewById(R.id.spinner_task_types);

    }

    public void setupSpinnerItems()
    {
        List<String> typesList = new ArrayList<>();
        List<Workflow> workflowList = service.getWorkflows();

        for(Workflow workflow : workflowList)
        {
            typesList.add(workflow.name);
        }

        // Initializing an ArrayAdapter
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,typesList) {

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                ((TextView) view).setGravity(Gravity.CENTER);

                TextView tv = (TextView) view;
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taskTypesSpinner.setAdapter(spinnerArrayAdapter );
        taskTypesSpinner.setOnItemSelectedListener(this);
    }

    public void setupDatePicker()
    {
        datePickButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(CreateTaskActivity.this, new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay)
                    {
                        dateTextView.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);
                    }
                }, day, month, year);
                datePickerDialog.show();
            }
        });
    }
}
