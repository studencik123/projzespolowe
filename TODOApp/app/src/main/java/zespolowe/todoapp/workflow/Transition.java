package zespolowe.todoapp.workflow;

import java.util.ArrayList;
import java.util.List;

public class Transition {
    public String from;

    public String to;

    public List<Action> validators;

    public List<Action> actions;

    public Transition(String from, String to) {
        this.from = from;
        this.to = to;
        validators = new ArrayList<>();
        actions = new ArrayList<>();
    }
}
