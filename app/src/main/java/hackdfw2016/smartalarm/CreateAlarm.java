package hackdfw2016.smartalarm;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CreateAlarm extends AppCompatActivity implements View.OnClickListener,
        TimePickerDialog.OnTimeSetListener, GoogleApiClient.OnConnectionFailedListener{
    Button save,cancel;
    Button sunday,monday,tuesday,wednesday,thursday,friday,saturday;
    Button arivalButton;
    EditText alarmName;
    Calendar timeSelected;
    HashMap<String,String> map;

    int[] days = {0,0,0,0,0,0,0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_alarm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        save = (Button)findViewById(R.id.save);
        cancel = (Button)findViewById(R.id.cancel);
        alarmName = (EditText)findViewById(R.id.alarmName);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();

                returnIntent.putExtra("alarmName",alarmName.getText().toString());
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    if(entry.getValue().equals("true")){
                        Log.d("test",entry.getKey());

                    }
                }
                returnIntent.putExtra("days",days);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        map = new HashMap<>();

        sunday = (Button)findViewById(R.id.Sunday);
        sunday.setOnClickListener(this);
        map.put("sunday", "false");

        monday = (Button)findViewById(R.id.Monday);
        monday.setOnClickListener(this);
        map.put("monday", "false");

        tuesday = (Button)findViewById(R.id.Tuesday);
        tuesday.setOnClickListener(this);
        map.put("tuesday", "false");

        wednesday = (Button)findViewById(R.id.Wednesday);
        wednesday.setOnClickListener(this);
        map.put("wednesday", "false");

        thursday = (Button)findViewById(R.id.Thursday);
        thursday.setOnClickListener(this);
        map.put("thursday", "false");

        friday = (Button)findViewById(R.id.Friday);
        friday.setOnClickListener(this);
        map.put("friday", "false");

        saturday = (Button)findViewById(R.id.Saturday);
        saturday.setOnClickListener(this);
        map.put("saturday", "false");



        arivalButton =(Button)findViewById(R.id.arivalTime);

        arivalButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                timeSelected = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        CreateAlarm.this,
                        timeSelected.get(Calendar.YEAR),
                        timeSelected.get(Calendar.MONTH),
                        false//false 12 hour true 24 hour
                );
                tpd.show(getFragmentManager(), "Pick Arrival Time");
            }
        });
    }
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        Button b = (Button)v;
        switch(v.getId()){
            case R.id.Monday: {
                if (map.get("monday").equals("false")) {
                    b.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    map.put("monday", "true");
                    days[1]=1;
                } else {
                    b.setBackgroundColor(Color.WHITE);
                    map.put("monday", "false");
                    days[1]=0;
                }
                break;
            }
            case R.id.Sunday: {
                if (map.get("sunday").equals("false")) {
                    b.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    map.put("sunday", "true");
                    days[0]=1;
                } else {
                    b.setBackgroundColor(Color.WHITE);
                    map.put("sunday", "false");
                    days[0]=0;
                }
                break;
            }
            case R.id.Tuesday: {
                if (map.get("tuesday").equals("false")) {
                    b.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    map.put("tuesday", "true");
                    days[2]=1;
                } else {
                    b.setBackgroundColor(Color.WHITE);
                    map.put("tuesday", "false");
                    days[2]=0;
                }
                break;
            }
            case R.id.Wednesday: {
                if (map.get("wednesday").equals("false")) {
                    b.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    map.put("wednesday", "true");
                    days[3]=1;
                } else {
                    b.setBackgroundColor(Color.WHITE);
                    map.put("wednesday", "false");
                    days[3]=0;
                }
                break;
            }
            case R.id.Thursday: {
                if (map.get("thursday").equals("false")) {
                    b.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    map.put("thursday", "true");
                    days[4]=1;
                } else {
                    b.setBackgroundColor(Color.WHITE);
                    map.put("thursday", "false");
                    days[4]=0;
                }
                break;
            }
            case R.id.Friday: {
                if (map.get("friday").equals("false")) {
                    b.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    map.put("friday", "true");
                    days[5]=1;
                } else {
                    b.setBackgroundColor(Color.WHITE);
                    map.put("friday", "false");
                    days[5]=0;
                }
                break;
            }
            case R.id.Saturday: {
                if (map.get("saturday").equals("false")) {
                    b.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    map.put("saturday", "true");
                    days[6]=1;
                } else {
                    b.setBackgroundColor(Color.WHITE);
                    map.put("saturday", "false");
                    days[6]=0;
                }
                break;
            }

        }
    }

    //Roberts Stuff ^^

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String time = "You picked the following time: "+hourOfDay+"h"+minute;
        //timeTextView.setText(time);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
