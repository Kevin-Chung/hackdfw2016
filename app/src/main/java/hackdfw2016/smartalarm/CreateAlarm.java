package hackdfw2016.smartalarm;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.PaintDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
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
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationRequestCreator;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CreateAlarm extends AppCompatActivity implements View.OnClickListener,
        TimePickerDialog.OnTimeSetListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {
    Button save, cancel;
    Button sunday, monday, tuesday, wednesday, thursday, friday, saturday;
    Button arivalButton;
    EditText alarmName,prepTime;
    Calendar timeSelected;
    HashMap<String, String> map;
    Button addPlace;
    Context context;
    int[] days = {0, 0, 0, 0, 0, 0, 0};
    Activity activity;
    Place place;
    double[] coords;
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor sharedPrefsEditor;

    GoogleApiClient mGoogleApiClient;

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

        coords = new double[4];

        context = this;
        activity = this;


        sharedPrefs=getSharedPreferences("Settings", 0);
        sharedPrefsEditor=sharedPrefs.edit();


        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        if (mGoogleApiClient == null) {
            Log.d("fail 2", "fail");
        }
        prepTime = (EditText)findViewById(R.id.prepTime);
        save = (Button) findViewById(R.id.save);
        cancel = (Button) findViewById(R.id.cancel);
        alarmName = (EditText) findViewById(R.id.alarmName);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();

                returnIntent.putExtra("alarmName", alarmName.getText().toString());
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    if (entry.getValue().equals("true")) {
                        Log.d("test", entry.getKey());

                    }
                }
                returnIntent.putExtra("prepTime",prepTime.getText().toString());
                Log.d("create prep",prepTime.getText().toString());
                returnIntent.putExtra("days", days);
                String timeString = Integer.toString(timeSelected.get(Calendar.HOUR)).concat(":").concat(Integer.toString(timeSelected.get(Calendar.MINUTE)));
                returnIntent.putExtra("arivalTime", timeString);
                setResult(Activity.RESULT_OK, returnIntent);
                sharedPrefsEditor.commit();
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        addPlace = (Button) findViewById(R.id.addPlace);
        addPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(activity);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });


        map = new HashMap<>();

        sunday = (Button) findViewById(R.id.Sunday);
        sunday.setOnClickListener(this);
        map.put("sunday", "false");

        monday = (Button) findViewById(R.id.Monday);
        monday.setOnClickListener(this);
        map.put("monday", "false");

        tuesday = (Button) findViewById(R.id.Tuesday);
        tuesday.setOnClickListener(this);
        map.put("tuesday", "false");

        wednesday = (Button) findViewById(R.id.Wednesday);
        wednesday.setOnClickListener(this);
        map.put("wednesday", "false");

        thursday = (Button) findViewById(R.id.Thursday);
        thursday.setOnClickListener(this);
        map.put("thursday", "false");

        friday = (Button) findViewById(R.id.Friday);
        friday.setOnClickListener(this);
        map.put("friday", "false");

        saturday = (Button) findViewById(R.id.Saturday);
        saturday.setOnClickListener(this);
        map.put("saturday", "false");

        //Better Picker
        arivalButton = (Button) findViewById(R.id.arivalTime);

        arivalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                place = PlaceAutocomplete.getPlace(this, data);
                sharedPrefsEditor.putString("place",place.getName()+"");

                try{
                    Log.d("hello","hello");

                    String url = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&";
                    url+="origins="+coords[0]+","+coords[1];
                    url+="&destinations="+place.getName()+"Dallas, TX, United States";
                    url+="&key=AIzaSyAVsykzRc9BbaQuMy-ILaywAolcxFK6d2w";
                    //url = URLEncoder.encode(url,"UTF-8");
                    run(url);

                }catch(IOException e){
                    Log.d("exception",e.getMessage()+"");
                }
                Log.i("callbacl", "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("callbacl", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        Button b = (Button) v;
        switch (v.getId()) {
            case R.id.Monday: {
                if (map.get("monday").equals("false")) {
                    b.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    map.put("monday", "true");
                    days[1] = 1;
                } else {
                    b.setBackgroundColor(Color.WHITE);
                    map.put("monday", "false");
                    days[1] = 0;
                }
                break;
            }
            case R.id.Sunday: {
                if (map.get("sunday").equals("false")) {
                    b.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    map.put("sunday", "true");
                    days[0] = 1;
                } else {
                    b.setBackgroundColor(Color.WHITE);
                    map.put("sunday", "false");
                    days[0] = 0;
                }
                break;
            }
            case R.id.Tuesday: {
                if (map.get("tuesday").equals("false")) {
                    b.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    map.put("tuesday", "true");
                    days[2] = 1;
                } else {
                    b.setBackgroundColor(Color.WHITE);
                    map.put("tuesday", "false");
                    days[2] = 0;
                }
                break;
            }
            case R.id.Wednesday: {
                if (map.get("wednesday").equals("false")) {
                    b.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    map.put("wednesday", "true");
                    days[3] = 1;
                } else {
                    b.setBackgroundColor(Color.WHITE);
                    map.put("wednesday", "false");
                    days[3] = 0;
                }
                break;
            }
            case R.id.Thursday: {
                if (map.get("thursday").equals("false")) {
                    b.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    map.put("thursday", "true");
                    days[4] = 1;
                } else {
                    b.setBackgroundColor(Color.WHITE);
                    map.put("thursday", "false");
                    days[4] = 0;
                }
                break;
            }
            case R.id.Friday: {
                if (map.get("friday").equals("false")) {
                    b.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    map.put("friday", "true");
                    days[5] = 1;
                } else {
                    b.setBackgroundColor(Color.WHITE);
                    map.put("friday", "false");
                    days[5] = 0;
                }
                break;
            }
            case R.id.Saturday: {
                if (map.get("saturday").equals("false")) {
                    b.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    map.put("saturday", "true");
                    days[6] = 1;
                } else {
                    b.setBackgroundColor(Color.WHITE);
                    map.put("saturday", "false");
                    days[6] = 0;
                }
                break;
            }

        }
    }

    //Roberts Stuff ^^

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String time = "You picked the following time: " + hourOfDay + "h" + minute;
        timeSelected.set(Calendar.HOUR, hourOfDay);
        timeSelected.set(Calendar.MINUTE, minute);
        //timeTextView.setText(time);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    protected void onStart() {
        Log.d("test", "here");
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d("test", "test");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return;
        }

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);


        if (mLastLocation != null) {

            Log.d("LOCATION LAT", String.valueOf(mLastLocation.getLatitude()));
            Log.d("LOCATION LAT", String.valueOf(mLastLocation.getLongitude()));
            /*mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));*/
        } else {
            Log.d("fail", "fail");
        }
        startLocationUpdates();

    }


    LocationRequest mLocationRequest = LocationRequest.create();

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("permission","not granted");
            //return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
               mGoogleApiClient, mLocationRequest, this);

        /*String url2 = "https://maps.googleapis.com/maps/api/distancematrix/json?origins="+coords[0]+","+coords[1]+"Dallas, TX, United States"
                +"&destinations="+place.getName()+"key=AIzaSyAVsykzRc9BbaQuMy-ILaywAolcxFK6d2w";
        try {
            URLEncoder.encode(url2,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/



    }
    String run(String url) throws IOException{
        Request request = new Request.Builder().url(url).build();
        OkHttpClient client = new OkHttpClient();
        final String url2 = url;
        AsyncTask<String,Void,Integer> task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder().url(String.valueOf(params[0])).build();
                try {
                    Response response = client.newCall(request).execute();
                    //Log.d("we made it",response.body().string());
                    String json = response.body().string();
                    JSONObject array = new JSONObject(json);
                    Log.d("we made it",array.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("duration").getString("text").toString());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExceute(Integer result){


            }

        };
        task.execute(url);
        //Response response = client.newCall(request).execute();

        return "";
    }

    @Override
    public void onLocationChanged(Location location) {
        Location mCurrentLocation = location;
        String mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        Log.d("location",String.valueOf(mCurrentLocation.getLatitude())+" "+String.valueOf(mCurrentLocation.getLongitude()));
        Toast.makeText(this,"location was received",Toast.LENGTH_SHORT).show();
        coords[0] = 32.7595618 ;
        sharedPrefsEditor.putString("lat",coords[0]+"");
        coords[1] =  -96.80699;
        coords[1] =  -96.80699;
        Log.d("coords", coords[0]+"\n"+coords[1]);
        sharedPrefsEditor.putString("long",coords[1]+"");
    }





    @Override
    public void onConnectionSuspended(int i) {

    }
}
