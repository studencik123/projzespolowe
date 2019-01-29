package zespolowe.todoapp;

import org.junit.Test;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import zespolowe.todoapp.dbo.Task;
import zespolowe.todoapp.workflow.Action;
import zespolowe.todoapp.workflow.Transition;
import zespolowe.todoapp.workflow.Workflow;
import zespolowe.todoapp.workflow.XmlParser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TransitionRunnerTest {
    @Test
    public void run_valid_transition() throws Exception
    {
        Task task = new Task();
        task.subject = "XtematX";
        task.state = "start";
        task.date = new Date();
        Transition transition = new Transition("start", "next");
        transition.validators.add(new Action("equals", "subject", "%temat%"));
        transition.validators.add(new Action("lowerEqual", "date", "now"));
        transition.actions.add(new Action("set", "subject", "+ nowy"));
        transition.actions.add(new Action("set", "date", "29/01/2019"));

        assertTrue(TransitionRunner.runTransition(task, transition, null));
        assertEquals("XtematX nowy", task.subject);
        assertEquals(new SimpleDateFormat("dd/MM/yyyy").parse("29/01/2019"), task.date);
    }
    @Test
    public void run_valid_transition2() throws Exception
    {
        Task task = new Task();
        task.subject = "temat";
        task.state = "start";
        task.date = new Date();
        Transition transition = new Transition("start", "next");
        transition.validators.add(new Action("equals", "subject", "temat"));
        transition.validators.add(new Action("lowerEqual", "date", "now"));
        transition.actions.add(new Action("set", "subject", "nowy temat"));
        transition.actions.add(new Action("set", "date", "null"));

        assertTrue(TransitionRunner.runTransition(task, transition, null));
        assertEquals("tematnowy temat", task.subject);
        assertEquals(null, task.date);
    }

    @Test
    public void run_invalid_transition()
    {
        Task task = new Task();
        task.subject = "temat";
        task.state = "start";
        Transition transition = new Transition("start", "next");
        transition.validators.add(new Action("equals", "subject", "XtematX"));
        transition.actions.add(new Action("set", "subject", "nowy temat"));

        assertFalse(TransitionRunner.runTransition(task, transition, null));
        assertEquals("temat", task.subject);
    }

    @Test
    public void get_available_states()
    {
        Task task = new Task();
        task.subject = "temat";
        task.state = "start";
        Workflow workflow = new Workflow();
        Transition transition = new Transition("start", "next");
        transition.validators.add(new Action("equals", "subject", "temat"));
        transition.actions.add(new Action("set", "subject", "nowy temat"));
        Transition transition2 = new Transition("start", "previous");
        transition2.validators.add(new Action("equals", "subject", "XtematX"));
        transition2.actions.add(new Action("set", "subject", "nowy temat"));
        Transition transition3 = new Transition("start", "present");
        transition3.validators.add(new Action("=", "subject", "temat"));
        transition3.actions.add(new Action("set", "subject", "nowy temat"));
        workflow.transitions.add(transition);
        workflow.transitions.add(transition2);
        workflow.transitions.add(transition3);
        task.workflow = workflow;

        List<String> types = TransitionRunner.getAvailableStates(task);

        assertEquals(1, types.size());
        assertEquals("next", types.get(0));
//        assertEquals("present", types.get(1));
    }

    @Test
    public void get_available_states2()
    {
        String xml = "<workflow state=\"To do\">\n" +
                "<transition from=\"To do\" to=\"Appointed\">\n" +
                "<action type=\"set\" field=\"description\" value=\"Appointment has been planned\"/>\n" +
                "<action type=\"notify\" field=\"5000\" value=\"Appointment!\"/>\n" +
                "</transition>\n" +
                "<transition from=\"Appointed\" to=\"Visited\">\n" +
                "<action type=\"set\" field=\"description\" value=\"Appointment completed\"/>\n" +
                "</transition>\n" +
                "<transition from=\"Appointed\" to=\"Postponed\">\n" +
                "<action type=\"lower\" field=\"date\" value=\"now\"/>\n" +
                "<action type=\"set\" field=\"description\" value=\"Appointment postponed\"/>\n" +
                "</transition>\n" +
                "<transition from=\"Postponed\" to=\"Visited\">\n" +
                "<action type=\"set\" field=\"description\" value=\"Appointment completed\"/>\n" +
                "<action type=\"set\" field=\"date\" value=\"null\"/>\n" +
                "</transition>\n" +
                "<transition from=\"Visited\" to=\"Appointed\">\n" +
                "<action type=\"set\" field=\"description\" value=\"Appointment has been planned\"/>\n" +
                "<action type=\"notify\" field=\"5000\" value=\"Appointment!\"/>\n" +
                "</transition>\n" +
                "</workflow>";

//        Workflow workflow = XmlParser.parse(xml);
        Task task = new Task();
        task.subject = "temat";
        task.xmlWorkflow = xml;
        task.state = task.GetWorkflow().state;
//        task.workflow = workflow;

        List<String> types = TransitionRunner.getAvailableStates(task);

        assertEquals(1, types.size());
        assertEquals("Appointed", types.get(0));
    }
}
