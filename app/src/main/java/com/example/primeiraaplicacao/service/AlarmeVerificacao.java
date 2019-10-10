package com.example.primeiraaplicacao.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.widget.Toast;

public class AlarmeVerificacao extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;

    @Override
    public void onReceive(Context context, Intent intent)
    {

            Intent i = new Intent(context, ServicoVerificacao.class);
            i.putExtra("foo", "bar");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(i);

            } else context.startService(i);
            // Put here YOUR code.
            Toast.makeText(context, "alarme", Toast.LENGTH_SHORT).show();


    }

}
