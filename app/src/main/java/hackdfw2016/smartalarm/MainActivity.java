package hackdfw2016.smartalarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private PendingIntent pendingIntent;
    private AlarmManager manager;



    ArrayList<Alarm> alarms;
    Context context;
    RecyclerView recyclerView;
    AlarmListAdapter adapter;
    String[] days ={"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturady"};


    private SharedPreferences preferenceSettings;
    private SharedPreferences.Editor preferenceEditor;
    private static final int PREFERENCE_MODE_PRIVATE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;

        //scott
        /*Intent intent = new Intent(this,weatherAPI.class);
        startActivity(intent);*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,CreateAlarm.class);
                startActivityForResult(intent, 1);
            }
        });

        FloatingActionButton traffic = (FloatingActionButton)findViewById(R.id.traffic);
        traffic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("settings",0);
                SharedPreferences.Editor e = sp.edit();

                String preptime = sp.getString("prepTime","");
                e.putString("prepTime",String.valueOf(Integer.parseInt(preptime)+10));
            }
        });


        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);

        initAlarms();

        adapter = new AlarmListAdapter(alarms,this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("alarmName");
                String arivalTime=data.getStringExtra("arivalTime");
                Alarm alarm = new Alarm(result,arivalTime,"");
                String day="";

                int[] dayz = data.getIntArrayExtra("days");
                for(int i = 0 ; i < dayz.length; i++){
                    if(dayz[i]==1){
                        day += days[i]+" ";
                    }
                }
                alarm.setDays(day);
                alarms.add(alarm);
                adapter.notifyDataSetChanged();
                String prepTime=data.getStringExtra("prepTime");

                preferenceSettings=getSharedPreferences("Settings", 0);
                preferenceEditor=preferenceSettings.edit();
                Log.d("Main Prep",prepTime);
                preferenceEditor.putString("prepTime", prepTime);
                preferenceEditor.putString("alarmName", result);
                preferenceEditor.putString("arivalTime", arivalTime);
                preferenceEditor.putString("dayz", day);
                preferenceEditor.commit();


                Intent alarmIntent = new Intent(context,BackgroundETAChecker.class);
                pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
                manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                int interval = 1000;
                //manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
                manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, interval, interval, pendingIntent);
                Toast.makeText(context, "Alarm Set", Toast.LENGTH_SHORT).show();

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult


    public void initAlarms(){
        alarms = new ArrayList<Alarm>();
        alarms.add(new Alarm("T/TH Classes","2:00", "Tuesday Thrusday"));
        alarms.add(new Alarm("M/W Classes","3:00"," Monday Wednesday"));
        alarms.add(new Alarm("Friday!","6:30"," Friday"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
