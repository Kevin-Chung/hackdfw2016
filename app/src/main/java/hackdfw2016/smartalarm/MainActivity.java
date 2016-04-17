package hackdfw2016.smartalarm;

import android.app.Activity;
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
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,CreateAlarm.class);
                startActivityForResult(intent, 1);
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
                Alarm alarm = new Alarm(result,"test","");
                String day="";

                int[] dayz = data.getIntArrayExtra("days");
                for(int i = 0 ; i < dayz.length; i++){
                    if(dayz[i]==1){
                        day += days[i]+" ";
                    }
                }
                String arivalTime=data.getStringExtra("arivalTime");
                alarm.setDays(day);
                alarms.add(alarm);
                adapter.notifyDataSetChanged();


                preferenceSettings=getSharedPreferences("Settings", 0);
                preferenceEditor=preferenceSettings.edit();
                preferenceEditor.putString("alarmName",result);
                preferenceEditor.putString("arivalTime",arivalTime);
                preferenceEditor.putString("dayz",day);
                preferenceEditor.commit();


            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult


    public void initAlarms(){
        alarms = new ArrayList<Alarm>();
        alarms.add(new Alarm("alarm 1","2 am", "mwf"));
        alarms.add(new Alarm("alarm 2","2 am"," monday wednesday friday"));
        alarms.add(new Alarm("alarm 3","3 am"," friday"));
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
