package edu.csce4623.ahnelson.todomvp3;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import edu.csce4623.ahnelson.todomvp3.data.ToDoItem;

import edu.csce4623.ahnelson.todomvp3.addedittodoitem.AddEditToDoItem;
import edu.csce4623.ahnelson.todomvp3.data.ToDoItem;
import edu.csce4623.ahnelson.todomvp3.todolistactivity.ToDoListActivity;

public class AlarmReceiver extends BroadcastReceiver {

    String ALARM_CHANNEL_ID = "ALARM";



    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String content = "Content!";
        String title = "Notification";


        if(intent.hasExtra("ToDoItemContent")) {
            content = intent.getStringExtra("ToDoItemContent");
        }
        if(intent.hasExtra("ToDoItemTitle")) {
            title = intent.getStringExtra("ToDoItemTitle");
        }
        NotificationCompat.Builder alarms_builder = new NotificationCompat.Builder(context,ALARM_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(content)
                .setChannelId(ALARM_CHANNEL_ID);

        Intent resultIntent = new Intent(context, ToDoListActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(ToDoListActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        alarms_builder.setContentIntent(resultPendingIntent);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelId = ALARM_CHANNEL_ID;
            NotificationChannel channel = new NotificationChannel(channelId, "AlarmNotifications", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            alarms_builder.setChannelId(channelId);
        }
        notificationManager.notify(0,alarms_builder.build());





    }
}