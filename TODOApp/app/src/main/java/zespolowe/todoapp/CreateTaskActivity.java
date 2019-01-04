package zespolowe.todoapp;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class CreateTaskActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    TextInputLayout taskNameInput, taskNotesInput;

    TextView dateTextView;
    Button datePickButton;
    Spinner taskTypesSpinner;

    Calendar calendar;
    DatePickerDialog datePickerDialog;

    //Przycisk finalizacji tworzenia Taska
    private FloatingActionButton floatingActionButton;
    private View.OnClickListener floatingActionButtonListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            createTaskButtonAction();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        taskNameInput = (TextInputLayout) findViewById(R.id.task_name_input);
        taskNotesInput = (TextInputLayout) findViewById(R.id.task_notes_input);
        dateTextView = (TextView) findViewById(R.id.task_date_text);
        datePickButton = (Button) findViewById(R.id.button_date);
        taskTypesSpinner = (Spinner) findViewById(R.id.spinner_task_types);

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
    private void createTaskButtonAction()
    {
        Task task = new Task();
        task.setTaskName(taskNameInput.getEditText().toString());
        task.setTaskType(taskTypesSpinner.getSelectedItem().toString());
    }

    public void setupSpinnerItems()
    {
        // Initializing a String Array
        String[] types = new String[]{
                "Select type of a task",
                "Car repair",
                "Doctor appointment",
        };

        List<String> typesList = new ArrayList<>(Arrays.asList(types));

        // Initializing an ArrayAdapter
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,typesList) {
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                ((TextView) view).setGravity(Gravity.CENTER);

                TextView tv = (TextView) view;
                if(position == 0)
                {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else
                {
                    tv.setTextColor(Color.BLACK);
                }
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
