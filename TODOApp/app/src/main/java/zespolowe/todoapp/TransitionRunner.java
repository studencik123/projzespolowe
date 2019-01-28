package zespolowe.todoapp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        if (fieldValue instanceof Date) {
            try {
                Date date = new SimpleDateFormat("dd/MM/yyyy").parse(value);
                switch (type) {
                    case "equals":
                        return areDatesEqual((Date) fieldValue, date);
                    case "notEqual":
                        return !areDatesEqual((Date) fieldValue, date);
                    case "greater":
                        return ((Date) fieldValue).after(date);
                    case "lower":
                        return date.after((Date) fieldValue);
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

//    public void scheduleNotification(Context context, long delay, int notificationId) {//delay is after how much time(in millis) from current time you want to schedule the notification
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
//                .setContentTitle(context.getString(R.string.title))
//                .setContentText(context.getString(R.string.content))
//                .setAutoCancel(true)
//                .setSmallIcon(R.drawable.app_icon)
//                .setLargeIcon(((BitmapDrawable) context.getResources().getDrawable(R.drawable.app_icon)).getBitmap())
//                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
//
//        Intent intent = new Intent(context, YourActivity.class);
//        PendingIntent activity = PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//        builder.setContentIntent(activity);
//
//        Notification notification = builder.build();
//
//        Intent notificationIntent = new Intent(context, MyNotificationPublisher.class);
//        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, notificationId);
//        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//
//        long futureInMillis = SystemClock.elapsedRealtime() + delay;
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
//    }
}
