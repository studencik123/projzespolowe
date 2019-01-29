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
        String xml = "<workflow state=\"start\">\n" +
                "<transition from=\"start\" to=\"next\">\n" +
                "<action type=\"equals\" field=\"subject\" value=\"temat\"/>\n" +
                "<action type=\"set\" field=\"subject\" value=\"nowy temat\"/>\n" +
                "</transition>\n" +
                "<transition from=\"start\" to=\"previous\">\n" +
                "<action type=\"equals\" field=\"subject\" value=\"XtematX\"/>\n" +
                "<action type=\"set\" field=\"subject\" value=\"stary temat\"/>\n" +
                "<action type=\"set\" field=\"date\" value=\"02/01/2018\"/>\n" +
                "</transition>\n" +
                "</workflow>";

        Workflow workflow = XmlParser.parse(xml);
        assertEquals("start", workflow.state);
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
    public void parse_valid_xml2()
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

        Workflow workflow = XmlParser.parse(xml);
        assertEquals("To do", workflow.state);
        assertEquals("To do", workflow.transitions.get(0).from);
        assertEquals("Appointed", workflow.transitions.get(0).to);
        assertEquals("set", workflow.transitions.get(0).actions.get(0).type);
        assertEquals("description", workflow.transitions.get(0).actions.get(0).field);
        assertEquals("Appointment has been planned", workflow.transitions.get(0).actions.get(0).value);
        assertEquals("notify", workflow.transitions.get(0).actions.get(1).type);
        assertEquals("5000", workflow.transitions.get(0).actions.get(1).field);
        assertEquals("Appointment!", workflow.transitions.get(0).actions.get(1).value);
    }

    @Test
    public void parse_valid_xml3()
    {
        String xml = "<workflow state=\"To do\">\n" +
                "<transition from=\"To do\" to=\"Plugged\">\n" +
                "<action type=\"notEqual\" field=\"description\" value=\"%plugged%\"/>\n" +
                "<action type=\"set\" field=\"description\" value=\"+ plugged\"/>\n" +
                "</transition>\n" +
                "<transition from=\"Reset printer\" to=\"Plugged\">\n" +
                "<action type=\"notEqual\" field=\"description\" value=\"%plugged%\"/>\n" +
                "<action type=\"set\" field=\"description\" value=\"+ plugged\"/>\n" +
                "</transition>\n" +
                "<transition from=\"Reset PC\" to=\"Plugged\">\n" +
                "<action type=\"notEqual\" field=\"description\" value=\"%plugged%\"/>\n" +
                "<action type=\"set\" field=\"description\" value=\"+ plugged\"/>\n" +
                "</transition>\n" +
                "<transition from=\"To do\" to=\"Reset printer\">\n" +
                "<action type=\"notEqual\" field=\"description\" value=\"%printer reset%\"/>\n" +
                "<action type=\"set\" field=\"description\" value=\"+ printer reset\"/>\n" +
                "</transition>\n" +
                "<transition from=\"Plugged\" to=\"Reset printer\">\n" +
                "<action type=\"notEqual\" field=\"description\" value=\"%printer reset%\"/>\n" +
                "<action type=\"set\" field=\"description\" value=\"+ printer reset\"/>\n" +
                "</transition>\n" +
                "<transition from=\"Reset PC\" to=\"Reset printer\">\n" +
                "<action type=\"notEqual\" field=\"description\" value=\"%printer reset%\"/>\n" +
                "<action type=\"set\" field=\"description\" value=\"+ printer reset\"/>\n" +
                "</transition>\n" +
                "<transition from=\"To do\" to=\"Reset PC\">\n" +
                "<action type=\"notEqual\" field=\"description\" value=\"%PC reset%\"/>\n" +
                "<action type=\"set\" field=\"description\" value=\"+ PC reset\"/>\n" +
                "</transition>\n" +
                "<transition from=\"Plugged\" to=\"Reset PC\">\n" +
                "<action type=\"notEqual\" field=\"description\" value=\"%PC reset%\"/>\n" +
                "<action type=\"set\" field=\"description\" value=\"+ PC reset\"/>\n" +
                "</transition>\n" +
                "<transition from=\"Reset printer\" to=\"Reset PC\">\n" +
                "<action type=\"notEqual\" field=\"description\" value=\"%PC reset%\"/>\n" +
                "<action type=\"set\" field=\"description\" value=\"+ PC reset\"/>\n" +
                "</transition>\n" +
                "<transition from=\"Plugged\" to=\"Call IT\">\n" +
                "<action type=\"equals\" field=\"description\" value=\"%plugged%\"/>\n" +
                "<action type=\"equals\" field=\"description\" value=\"%printer reset%\"/>\n" +
                "<action type=\"equals\" field=\"description\" value=\"%PC reset%\"/>\n" +
                "<action type=\"set\" field=\"description\" value=\"All failed\"/>\n" +
                "</transition>\n" +
                "<transition from=\"Reset printer\" to=\"Call IT\">\n" +
                "<action type=\"equals\" field=\"description\" value=\"%plugged%\"/>\n" +
                "<action type=\"equals\" field=\"description\" value=\"%printer reset%\"/>\n" +
                "<action type=\"equals\" field=\"description\" value=\"%PC reset%\"/>\n" +
                "<action type=\"set\" field=\"description\" value=\"All failed\"/>\n" +
                "</transition>\n" +
                "<transition from=\"Reset PC\" to=\"Call IT\">\n" +
                "<action type=\"equals\" field=\"description\" value=\"%plugged%\"/>\n" +
                "<action type=\"equals\" field=\"description\" value=\"%printer reset%\"/>\n" +
                "<action type=\"equals\" field=\"description\" value=\"%PC reset%\"/>\n" +
                "<action type=\"set\" field=\"description\" value=\"All failed\"/>\n" +
                "</transition>\n" +
                "</workflow>";

        Workflow workflow = XmlParser.parse(xml);
        assertEquals("To do", workflow.state);
        assertEquals("To do", workflow.transitions.get(0).from);
        assertEquals("Plugged", workflow.transitions.get(0).to);
        assertEquals("notEqual", workflow.transitions.get(0).validators.get(0).type);
        assertEquals("description", workflow.transitions.get(0).validators.get(0).field);
        assertEquals("%plugged%", workflow.transitions.get(0).validators.get(0).value);
        assertEquals("set", workflow.transitions.get(0).actions.get(0).type);
        assertEquals("description", workflow.transitions.get(0).actions.get(0).field);
        assertEquals("+ plugged", workflow.transitions.get(0).actions.get(0).value);
    }

    @Test
    public void parse_and_run_valid_xml()
    {
        String xml = "<workflow state=\"start\">\n" +
                "<transition from=\"start\" to=\"next\">\n" +
                "<action type=\"equals\" field=\"subject\" value=\"temat\"/>\n" +
                "<action type=\"set\" field=\"subject\" value=\"nowy temat\"/>\n" +
                "</transition>\n" +
                "</workflow>";

        Workflow workflow = XmlParser.parse(xml);
        Task task = new Task();
        task.subject = "temat";

        assertTrue(TransitionRunner.runTransition(task, workflow.transitions.get(0), null));
        assertEquals("nowy temat", task.subject);
    }

    @Test
    public void parse__and_run_valid_xml()
    {
        String xml = "<workflow state=\"To do\">\n" +
                "<transition from=\"To do\" to=\"Plugged\">\n" +
                "<action type=\"notEqual\" field=\"description\" value=\"%plugged%\"/>\n" +
                "<action type=\"set\" field=\"description\" value=\"+ plugged\"/>\n" +
                "</transition>\n" +
                "<transition from=\"Reset printer\" to=\"Plugged\">\n" +
                "<action type=\"notEqual\" field=\"description\" value=\"%plugged%\"/>\n" +
                "<action type=\"set\" field=\"description\" value=\"+ plugged\"/>\n" +
                "</transition>\n" +
                "<transition from=\"Reset PC\" to=\"Plugged\">\n" +
                "<action type=\"notEqual\" field=\"description\" value=\"%plugged%\"/>\n" +
                "<action type=\"set\" field=\"description\" value=\"+ plugged\"/>\n" +
                "</transition>\n" +
                "<transition from=\"To do\" to=\"Reset printer\">\n" +
                "<action type=\"notEqual\" field=\"description\" value=\"%printer reset%\"/>\n" +
                "<action type=\"set\" field=\"description\" value=\"+ printer reset\"/>\n" +
                "</transition>\n" +
                "<transition from=\"Plugged\" to=\"Reset printer\">\n" +
                "<action type=\"notEqual\" field=\"description\" value=\"%printer reset%\"/>\n" +
                "<action type=\"set\" field=\"description\" value=\"+ printer reset\"/>\n" +
                "</transition>\n" +
                "<transition from=\"Reset PC\" to=\"Reset printer\">\n" +
                "<action type=\"notEqual\" field=\"description\" value=\"%printer reset%\"/>\n" +
                "<action type=\"set\" field=\"description\" value=\"+ printer reset\"/>\n" +
                "</transition>\n" +
                "<transition from=\"To do\" to=\"Reset PC\">\n" +
                "<action type=\"notEqual\" field=\"description\" value=\"%PC reset%\"/>\n" +
                "<action type=\"set\" field=\"description\" value=\"+ PC reset\"/>\n" +
                "</transition>\n" +
                "<transition from=\"Plugged\" to=\"Reset PC\">\n" +
                "<action type=\"notEqual\" field=\"description\" value=\"%PC reset%\"/>\n" +
                "<action type=\"set\" field=\"description\" value=\"+ PC reset\"/>\n" +
                "</transition>\n" +
                "<transition from=\"Reset printer\" to=\"Reset PC\">\n" +
                "<action type=\"notEqual\" field=\"description\" value=\"%PC reset%\"/>\n" +
                "<action type=\"set\" field=\"description\" value=\"+ PC reset\"/>\n" +
                "</transition>\n" +
                "<transition from=\"Plugged\" to=\"Call IT\">\n" +
                "<action type=\"equals\" field=\"description\" value=\"%plugged%\"/>\n" +
                "<action type=\"equals\" field=\"description\" value=\"%printer reset%\"/>\n" +
                "<action type=\"equals\" field=\"description\" value=\"%PC reset%\"/>\n" +
                "<action type=\"set\" field=\"description\" value=\"All failed\"/>\n" +
                "</transition>\n" +
                "<transition from=\"Reset printer\" to=\"Call IT\">\n" +
                "<action type=\"equals\" field=\"description\" value=\"%plugged%\"/>\n" +
                "<action type=\"equals\" field=\"description\" value=\"%printer reset%\"/>\n" +
                "<action type=\"equals\" field=\"description\" value=\"%PC reset%\"/>\n" +
                "<action type=\"set\" field=\"description\" value=\"All failed\"/>\n" +
                "</transition>\n" +
                "<transition from=\"Reset PC\" to=\"Call IT\">\n" +
                "<action type=\"equals\" field=\"description\" value=\"%plugged%\"/>\n" +
                "<action type=\"equals\" field=\"description\" value=\"%printer reset%\"/>\n" +
                "<action type=\"equals\" field=\"description\" value=\"%PC reset%\"/>\n" +
                "<action type=\"set\" field=\"description\" value=\"All failed\"/>\n" +
                "</transition>\n" +
                "</workflow>";

        Workflow workflow = XmlParser.parse(xml);
        Task task = new Task();
        task.subject = "temat";
        task.state = "To do";
        task.workflow = workflow;
        task.xmlWorkflow = xml;

        assertEquals(3, TransitionRunner.getAvailableStates(task).size());

        assertTrue(TransitionRunner.runTransition(task, workflow.transitions.get(0), null));
        assertEquals("temat", task.subject);
    }
}
