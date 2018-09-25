package com.example.reynaldodeleo.todolistapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
//import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity  {
    private EditText message;
    //private EditText editTextTitle;
    //private EditText editTextMessage;

    private Button buttonChannel1;
    private Button buttonChannel2;

    private NotificationHelper mNotificationHelper;

    int hour, mminute;
    public static String messages[] = new String[10];
    public static int itr = 0;
    public static int pid = 0;

    int swt = 0;

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<String>();
//        readItems();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();

        try {
            Scanner sc = new Scanner(openFileInput("Todo.txt"));
            while(sc.hasNextLine()) {
                String data = sc.nextLine();
                itemsAdapter.add(data);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //editTextTitle = findViewById(R.id.edittext_title);
        //editTextMessage = findViewById(R.id.edittext_message);

        message = findViewById(R.id.New_Item);
        //buttonChannel1 = findViewById(R.id.TimeAlarm);

        //buttonChannel2 = findViewById(R.id.button_channel2);

        mNotificationHelper = new NotificationHelper(this);

        /*buttonChannel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            sendOnChannel1(message.getText().toString(), message.getText().toString());
            }
        });
*/
        /*buttonChannel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            sendOnChannel2(message.getText().toString(), message.getText().toString());
            }
        });*/


        //Button button = (Button) findViewById(R.id.button1);
        /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");

            }
        });
        */


    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        // Remove the item within array at position
                        items.remove(pos);
                        if(pos == 0){
                            CheckBox cb6 = (CheckBox)findViewById(R.id.checkBox6);
                            cb6.setVisibility(View.INVISIBLE);
                        }
                        // Refresh the adapter
                        itemsAdapter.notifyDataSetChanged();
                        // Return true consumes the long click event (marks it handled)
//                        writeItems();
                        return true;
                    }

                });
    }



    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.New_Item);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        messages[pid] = itemText;
        CheckBox cb = (CheckBox)findViewById(R.id.checkBox3);
        cb.setVisibility(View.VISIBLE);
        if(swt == 1)
        {
            CheckBox cb6 = (CheckBox)findViewById(R.id.checkBox6);
            cb6.setVisibility(View.VISIBLE);
        }
        swt++;
    }

    @Override
    public void onBackPressed(){
        try {
            PrintWriter pw = new PrintWriter(openFileOutput("Todo.txt",Context.MODE_PRIVATE));
            for(String data: items) {
                pw.println(data);
            }
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finish();
    }

    public void sendOnChannel1(String title, String message){

        NotificationCompat.Builder nb = mNotificationHelper.getChannel1Notification(title, message);
        mNotificationHelper.getManager().notify(1, nb.build());

    }
    public void sendOnChannel2(String title, String message){

        NotificationCompat.Builder nb = mNotificationHelper.getChannel2Notification(title, message);
        mNotificationHelper.getManager().notify(2, nb.build());

    }


    //ALARM CODE
    //REY PART


    public void setTime(View v)
    {
        Calendar cal2 = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new
                TimePickerDialog.OnTimeSetListener(){

                    @Override
                    public void onTimeSet(TimePicker arg0, int hourOfDay, int minute) {
                        hour = hourOfDay;
                        mminute = minute;
                    }
                }, cal2.get(Calendar.HOUR_OF_DAY),
                cal2.get(Calendar.MINUTE),false);

        timePickerDialog.show();

    }

    public void startAlarm(View view)
    {
        Intent intent2 = new Intent(this, MyBroadcastReceiver.class);

        PendingIntent pendingIntent2 = PendingIntent.getBroadcast (this.getApplicationContext(),pid,intent2,0);
        pid++;

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar cal_alarm = Calendar.getInstance();
        cal_alarm.set(Calendar.HOUR_OF_DAY,hour);
        cal_alarm.set(Calendar.MINUTE,mminute);
        cal_alarm.set(Calendar.SECOND,0);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal_alarm.getTimeInMillis(),
                    pendingIntent2);
            Toast.makeText(this,"Alarm Set", Toast.LENGTH_SHORT).show();
        }

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, cal_alarm.getTimeInMillis(),
                    pendingIntent2);
            Toast.makeText(this,"Alarm Set",Toast.LENGTH_SHORT).show();
        }
    }




}
