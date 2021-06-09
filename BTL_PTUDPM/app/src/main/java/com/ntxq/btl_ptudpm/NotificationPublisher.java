package com.ntxq.btl_ptudpm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationPublisher extends BroadcastReceiver {
    public static final String ID_EXTRA= "ID_EXTRA";
    public static final String NOTIFICATION_EXTRA= "NOTIFICATION_EXTRA";
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification= intent.getParcelableExtra(NOTIFICATION_EXTRA);
        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O){
            int importance= NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel= new NotificationChannel("NtxQ","Zodiac",importance);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        int id= intent.getIntExtra(ID_EXTRA, 0);
        notificationManager.notify(id,notification);
    }
}
