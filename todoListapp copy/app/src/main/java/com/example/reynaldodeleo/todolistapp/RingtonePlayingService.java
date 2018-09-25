package com.example.reynaldodeleo.todolistapp;

import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by reynaldodeleo on 4/17/18.
 */

public class RingtonePlayingService extends Service  {

    Ringtone r;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
         return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.i("Local service ", "Received start id" + startId+ ": " + intent);

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

        long ringDelay = 10500;

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                r.stop();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, ringDelay);

        return START_NOT_STICKY;
    }

    public void onDestroy()
    {
        //tell the user we stopped
        Toast.makeText(this,"on Destroy called",Toast.LENGTH_SHORT).show();
    }


}
