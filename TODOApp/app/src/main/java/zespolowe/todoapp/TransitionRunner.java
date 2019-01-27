package zespolowe.todoapp;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import zespolowe.todoapp.dbo.Task;
import zespolowe.todoapp.workflow.Action;
import zespolowe.todoapp.workflow.Transition;

public class TransitionRunner {
    public static boolean runTransition(Task task, Transition transition) {
        for (Action validator : transition.validators) {
            if (!validate(task, validator))
                return false;
        }
        for (Action action : transition.actions) {
            runAction(task, action);
        }
        task.state = transition.to;
        return true;
    }

    private static boolean validate(Task task, Action validator) {
        try {
            Field field = task.getClass().getDeclaredField(validator.field);
            Object fieldValue = field.get(task);
            return compareByType(fieldValue, validator.value, validator.type);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean compareByType(Object fieldValue, String value, String type) {
        if (fieldValue instanceof String) {
            switch (type) {
                case "equals":
                    return value.equals(fieldValue);
                case "notEqual":
                    return !value.equals(fieldValue);
                default:
                    return false;
            }
        }
        return false;
    }

    private static void runAction(Task task, Action action) {
        switch (action.type) {
            case "set":
                runSetAction(task, action);
                break;
        }
    }

    private static void runSetAction(Task task, Action action) {
        try {
            Field field = task.getClass().getDeclaredField(action.field);
            field.set(task, getTypedValue(field.getType(), action.value));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static Object getTypedValue(Type type, String value) {
        return value;
    }
}
