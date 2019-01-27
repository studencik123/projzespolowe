package zespolowe.todoapp.workflow;

import java.util.ArrayList;
import java.util.List;

public class Workflow {
    public List<String> states;

    public List<Transition> transitions;

    public Workflow() {
        this.states = new ArrayList<>();
        this.transitions = new ArrayList<>();
    }
}
