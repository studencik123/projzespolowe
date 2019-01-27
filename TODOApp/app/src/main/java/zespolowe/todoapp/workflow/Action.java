package zespolowe.todoapp.workflow;

public class Action {
    public String type;

    public String field;

    public String value;

    public Action(String type, String field, String value) {
        this.type = type;
        this.field = field;
        this.value = value;
    }
}
