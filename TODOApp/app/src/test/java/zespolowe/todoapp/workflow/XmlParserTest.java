package zespolowe.todoapp.workflow;

import org.junit.Test;

import zespolowe.todoapp.TransitionRunner;
import zespolowe.todoapp.dbo.Task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class XmlParserTest {
    @Test
    public void parse_valid_xml()
    {
        String xml = "<workflow>\n" +
                "<state name=\"start\"/>\n" +
                "<state name=\"next\"/>\n" +
                "<transition from=\"start\" to=\"next\">\n" +
                "<action type=\"equals\" field=\"subject\" value=\"temat\"/>\n" +
                "<action type=\"set\" field=\"subject\" value=\"nowy temat\"/>\n" +
                "</transition>\n" +
                "</workflow>";

        Workflow workflow = XmlParser.parse(xml);
        assertEquals("start", workflow.states.get(0));
        assertEquals("next", workflow.states.get(1));
        assertEquals("start", workflow.transitions.get(0).from);
        assertEquals("next", workflow.transitions.get(0).to);
        assertEquals("equals", workflow.transitions.get(0).validators.get(0).type);
        assertEquals("subject", workflow.transitions.get(0).validators.get(0).field);
        assertEquals("temat", workflow.transitions.get(0).validators.get(0).value);
        assertEquals("set", workflow.transitions.get(0).actions.get(0).type);
        assertEquals("subject", workflow.transitions.get(0).actions.get(0).field);
        assertEquals("nowy temat", workflow.transitions.get(0).actions.get(0).value);
    }

    @Test
    public void parse__and_run_valid_xml()
    {
        String xml = "<workflow>\n" +
                "<state name=\"start\"/>\n" +
                "<state name=\"next\"/>\n" +
                "<transition from=\"start\" to=\"next\">\n" +
                "<action type=\"equals\" field=\"subject\" value=\"temat\"/>\n" +
                "<action type=\"set\" field=\"subject\" value=\"nowy temat\"/>\n" +
                "</transition>\n" +
                "</workflow>";

        Workflow workflow = XmlParser.parse(xml);
        Task task = new Task();
        task.subject = "temat";

        assertTrue(TransitionRunner.runTransition(task, workflow.transitions.get(0)));
        assertEquals("nowy temat", task.subject);
    }
}
