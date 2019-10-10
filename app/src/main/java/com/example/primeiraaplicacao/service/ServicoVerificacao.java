package com.example.primeiraaplicacao.service;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.primeiraaplicacao.MainActivity;
import com.example.primeiraaplicacao.R;

public class ServicoVerificacao extends Service {

    AlarmeVerificacao alarm = new AlarmeVerificacao();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     *
     */


    public void onCreate()
    {
        super.onCreate();
        //startForeground(this);
        startForeground(0,null);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "fusivel";
            String description = "fusivelEletronico";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("4141", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent intento = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intento, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "4141")
                .setSmallIcon(R.drawable.ic_warning_black_24dp)
                .setContentTitle("Alerta!")
                .setContentText("Novo alerta detectado.")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        createNotificationChannel();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(4141, builder.build());

        return super.onStartCommand(intent, flags, startId);

    }
}
