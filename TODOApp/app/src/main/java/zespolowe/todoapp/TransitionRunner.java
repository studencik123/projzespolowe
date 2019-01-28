package zespolowe.todoapp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.SystemClock;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import zespolowe.todoapp.dbo.Task;
import zespolowe.todoapp.workflow.Action;
import zespolowe.todoapp.workflow.Transition;

public class TransitionRunner {
    public static final int REQUEST_CODE = 0;

    public static List<String> getAvailableStates(Task task) {
        List<String> list = new ArrayList<>();
        for (Transition transition : task.GetWorkflow().transitions) {
            if (validateTransition(task, transition))
                list.add(transition.to);
        }
        return list;
    }

    private static boolean validateTransition(Task task, Transition transition) {
        for (Action validator : transition.validators) {
            if (!validate(task, validator))
                return false;
        }
        return true;
    }

    public static boolean runTransition(Task task, Transition transition) {
        if (!validateTransition(task, transition))
            return false;
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
                case "=":
                    return value.equals(fieldValue);
                case "!=":
                    return !value.equals(fieldValue);
                default:
                    return false;
            }
        }
        if (fieldValue instanceof Date) {
            try {
                Date date = new SimpleDateFormat("dd/MM/yyyy").parse(value);
                switch (type) {
                    case "=":
                        return areDatesEqual((Date) fieldValue, date);
                    case "!=":
                        return !areDatesEqual((Date) fieldValue, date);
                    case ">":
                        return ((Date) fieldValue).after(date);
                    case "<":
                        return date.after((Date) fieldValue);
                    case ">=":
                        return !date.after((Date) fieldValue);
                    case "<=":
                        return !((Date) fieldValue).after(date);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return false;
        }
        return false;
    }

    private static boolean areDatesEqual(Date date1, Date date2) {
        return date1.getDate() == date2.getDate();
    }

    private static void runAction(Task task, Action action) {
        switch (action.type) {
            case "set":
                runSetAction(task, action);
                break;
            case "notify":
                runNotifyAction();
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
        if (type == Date.class) {
            try {
                return new SimpleDateFormat("dd/MM/yyyy").parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    private static void runNotifyAction() {

    }
}
