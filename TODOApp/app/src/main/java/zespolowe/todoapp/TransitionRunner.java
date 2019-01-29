package zespolowe.todoapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import zespolowe.todoapp.dbo.Task;
import zespolowe.todoapp.workflow.Action;
import zespolowe.todoapp.workflow.Transition;

public class TransitionRunner {
    public static String NOTIFICATION_ID = "notification_id";
    public static String NOTIFICATION_SUBJECT = "notification_subject";
    public static String NOTIFICATION_TEXT = "notification_text";

    public static List<String> getAvailableStates(Task task) {
        List<String> list = new ArrayList<>();
        for (Transition transition : task.GetWorkflow().transitions) {
            if (validateTransition(task, transition))
                list.add(transition.to);
        }
        return list;
    }

    private static boolean validateTransition(Task task, Transition transition) {
        if (!task.state.equals(transition.from))
            return false;
        for (Action validator : transition.validators) {
            if (!validate(task, validator))
                return false;
        }
        return true;
    }

    public static boolean runTransition(Task task, Transition transition, Context context) {
        if (!validateTransition(task, transition))
            return false;
        for (Action action : transition.actions) {
            runAction(task, action, context);
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
                    return compareStrings((String) fieldValue, value);
                case "notEqual":
                    return !compareStrings((String) fieldValue, value);
                default:
                    return false;
            }
        }
        if (fieldValue instanceof Date) {
            Date date = getTime(value);
            switch (type) {
                case "equals":
                    return areDatesEqual((Date) fieldValue, date);
                case "notEqual":
                    return !areDatesEqual((Date) fieldValue, date);
                case "greater":
                    return ((Date) fieldValue).after(date);
                case "lower":
                    return date.after((Date) fieldValue);
                case "greaterEqual":
                    return !date.after((Date) fieldValue);
                case "lowerEqual":
                    return !((Date) fieldValue).after(date);
            }
            return false;
        }
        return false;
    }

    private static boolean compareStrings(String field, String value) {
        String clearValue = value.replace("%", "");
        if (value.startsWith("%") && value.endsWith("%"))
            return field.contains(clearValue);
        if (value.startsWith("%"))
            return field.endsWith(clearValue);
        if (value.endsWith("%"))
            return field.startsWith(clearValue);
        return field.equals(value);
    }

    private static boolean areDatesEqual(Date date1, Date date2) {
        return date1.getDate() == date2.getDate();
    }

    private static void runAction(Task task, Action action, Context context) {
        switch (action.type) {
            case "set":
                runSetAction(task, action);
                break;
            case "notify":
                runNotifyAction(task, action, context);
                break;
        }
    }

    private static void runSetAction(Task task, Action action) {
        try {
            Field field = task.getClass().getDeclaredField(action.field);
            Object typedValue = null;
            if (action.value.startsWith("+"))
            {
                String value = action.value.replace("+", "");
                Object fieldValue = field.get(task);
                if (fieldValue instanceof String) {
                    typedValue = ((String) fieldValue).concat(value);
                }
            }
            if (typedValue == null)
                typedValue = getTypedValue(field.getType(), action.value);
            field.set(task, typedValue);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static Object getTypedValue(Type type, String value) {
        if (type == Date.class) {
            return getTime(value);
        }
        return value;
    }

    private static void runNotifyAction(Task task, Action action, Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent(context, AlarmReceiver.class);
        notificationIntent.putExtra(NOTIFICATION_ID, task.id);
        notificationIntent.putExtra(NOTIFICATION_SUBJECT, task.subject);
        notificationIntent.putExtra(NOTIFICATION_TEXT, action.value);

        PendingIntent broadcast = PendingIntent.getBroadcast(context, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + getTime(task, action.field), broadcast);
    }

    private static long getTime(Task task, String fieldName) {
        try {
            Field field = task.getClass().getDeclaredField(fieldName);
            Object fieldValue = field.get(task);
            if (fieldValue instanceof Date) {
                return ((Date) fieldValue).getTime() - new Date().getTime();
            }
        } catch (Exception e) {
        }
        Date date = getTime(fieldName);
        if (date != null) {
            return date.getTime() - new Date().getTime();
        }
        try {
            return Long.parseLong(fieldName);
        } catch (Exception e) {

        }
        return 1000;
    }

    private static Date getTime(String value) {
        if (value.equals("now"))
            return new Date();
        try {
            return new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).parse(value);
        } catch (ParseException e) {
        }
        try {
            return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(value);
        } catch (ParseException e1) {}
        return null;
    }
}
