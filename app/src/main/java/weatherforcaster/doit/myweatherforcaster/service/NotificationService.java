package weatherforcaster.doit.myweatherforcaster.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import weatherforcaster.doit.myweatherforcaster.Common.Common;
import weatherforcaster.doit.myweatherforcaster.R;
import weatherforcaster.doit.myweatherforcaster.activity.SettingsActivity;

public class NotificationService extends Service {
    public static final String CHANNEL_ID = "myNot";
    public static final int NOTIFICATION_ID = 13;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        switch (intent.getStringExtra("frequency")) {
            case "Once a day":
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        NewNotification();
                    }
                }, Common.INTERVAL_ONCE);
            case "Twice a day":
                for (int i = 0; i < 3; i++) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            NewNotification();
                        }
                    }, Common.INTERVAL_TWICE/2);
                }
            case "Trice a day":
                for (int i = 0; i < 4; i++) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            NewNotification();
                        }
                    }, Common.INTERVAL_TRICE/3);
                }
        }
        stopSelf();
        Log.d("TESTING", "started");
        return super.onStartCommand(intent, flags, startId);
    }

    private void NewNotification() {
        IsOreo();
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent
                .getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentTitle("Your weather forecast")
                .setContentText("See forecast for your city!")
                .setSmallIcon(R.drawable.notification_icon)
                .addAction(R.drawable.notification_icon, "Open Notification Manager", pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
    }

    private void IsOreo() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "weather";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
