package zespolowe.todoapp;

import org.junit.Test;


import java.text.SimpleDateFormat;
import java.util.Date;

import zespolowe.todoapp.dbo.Task;
import zespolowe.todoapp.workflow.Action;
import zespolowe.todoapp.workflow.Transition;
import zespolowe.todoapp.workflow.Workflow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TransitionRunnerTest {
    @Test
    public void run_valid_transition() throws Exception
    {
        Task task = new Task();
        task.subject = "temat";
        task.date = new Date();
        Transition transition = new Transition("start", "next");
        transition.validators.add(new Action("equals", "subject", "temat"));
        transition.validators.add(new Action("equals", "date", "27/01/2019"));
        transition.actions.add(new Action("set", "subject", "nowy temat"));
        transition.actions.add(new Action("set", "date", "29/01/2019"));

        assertTrue(TransitionRunner.runTransition(task, transition));
        assertEquals("nowy temat", task.subject);
        assertEquals(new SimpleDateFormat("dd/MM/yyyy").parse("29/01/2019"), task.date);
    }

    @Test
    public void run_invalid_transition()
    {
        Task task = new Task();
        task.subject = "temat";
        Transition transition = new Transition("start", "next");
        transition.validators.add(new Action("equals", "subject", "XtematX"));
        transition.actions.add(new Action("set", "subject", "nowy temat"));

        assertFalse(TransitionRunner.runTransition(task, transition));
        assertEquals("temat", task.subject);
    }
}
