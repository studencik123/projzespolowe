package zespolowe.todoapp;

import org.junit.Test;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
        transition.validators.add(new Action("=", "subject", "temat"));
        transition.validators.add(new Action(">", "date", "27/01/2019"));
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
        transition.validators.add(new Action("=", "subject", "XtematX"));
        transition.actions.add(new Action("set", "subject", "nowy temat"));

        assertFalse(TransitionRunner.runTransition(task, transition));
        assertEquals("temat", task.subject);
    }

    @Test
    public void get_available_states()
    {
        Task task = new Task();
        task.subject = "temat";
        Workflow workflow = new Workflow();
        Transition transition = new Transition("start", "next");
        transition.validators.add(new Action("=", "subject", "temat"));
        transition.actions.add(new Action("set", "subject", "nowy temat"));
        Transition transition2 = new Transition("start", "previous");
        transition2.validators.add(new Action("=", "subject", "XtematX"));
        transition2.actions.add(new Action("set", "subject", "nowy temat"));
        Transition transition3 = new Transition("start", "present");
        transition3.validators.add(new Action("=", "subject", "temat"));
        transition3.actions.add(new Action("set", "subject", "nowy temat"));
        workflow.transitions.add(transition);
        workflow.transitions.add(transition2);
        workflow.transitions.add(transition3);
        task.workflow = workflow;

        List<String> types = TransitionRunner.getAvailableStates(task);

        assertEquals(2, types.size());
        assertEquals("next", types.get(0));
        assertEquals("present", types.get(1));
    }
}
