package zespolowe.todoapp.workflow;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class XmlParser {
    public static List<String> ValidatorTypes = new ArrayList<>(
            Arrays.asList("equals", "notEqual", "greater", "lower", "greaterEqual", "lowerEqual"));

    public static Workflow parse(String xmlWorkflow) {
        Workflow workflow = new Workflow();
        try {
            Document doc = loadXMLFromString(xmlWorkflow);
            Node workflowNode = doc.getElementsByTagName("workflow").item(0);
            if (workflowNode.getNodeType() == Node.ELEMENT_NODE) {
                Element state = (Element) workflowNode;
                String name = state.getAttribute("state");
                workflow.state = name;
            }

            NodeList transitions = doc.getElementsByTagName("transition");
            for (int i=0; i<transitions.getLength(); i++) {
                Node node = transitions.item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE)
                    continue;

                Element transition = (Element) node;
                String from = transition.getAttribute("from");
                String to = transition.getAttribute("to");
                Transition t = new Transition(from, to);
                NodeList actions = transition.getChildNodes();
                for (int j = 0; j < actions.getLength(); j++) {
                    Node actionNode = actions.item(j);
                    if (actionNode.getNodeType() != Node.ELEMENT_NODE)
                        continue;

                    Element action = (Element) actionNode;
                    String type = action.getAttribute("type");
                    String field = action.getAttribute("field");
                    String value = action.getAttribute("value");
                    if (ValidatorTypes.contains(type)) {
                        t.validators.add(new Action(type, field, value));
                    }
                    else {
                        t.actions.add(new Action(type, field, value));
                    }
                }
                workflow.transitions.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return workflow;
    }

    private static Document loadXMLFromString(String xml) throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }
}
