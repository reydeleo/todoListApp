package com.example.reynaldodeleo.todolistapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by reynaldodeleo on 4/12/18.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {
     String message = MainActivity.messages[MainActivity.itr];

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,message, Toast.LENGTH_LONG).show();
        MainActivity.itr++;

        //create an intent to the ringtone service
        Intent service_intent = new Intent(context,RingtonePlayingService.class);

        //start the ringtone service
        context.startService(service_intent);
    }
}
