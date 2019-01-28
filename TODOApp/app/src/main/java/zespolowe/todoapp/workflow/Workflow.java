package zespolowe.todoapp.workflow;

import java.util.ArrayList;
import java.util.List;

public class Workflow {
    public String state;

    public List<Transition> transitions;

    public Workflow() {
        this.transitions = new ArrayList<>();
    }
}
