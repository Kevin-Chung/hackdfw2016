package hackdfw2016.smartalarm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WakeUpInfoScreen extends AppCompatActivity {
    private PowerManager.WakeLock wl;

    TextView weather, timeToLeave;
    ImageView weatherIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wake_up_info_screen);
        SharedPreferences sharedPrefs = getSharedPreferences("settings",0);

        String lat = "32.7687624";
        String longi = "-96.7983806";


        String url = "https://api.forecast.io/forecast/297fbd27738bf683251026ccc5477e73/"+lat+","+longi;

        try {
            Log.d("url",url);
            Log.d("3","test");
            run(url);
        } catch (IOException e) {
            e.printStackTrace();
        }


        Calendar cal = Calendar.getInstance();
        String leave = sharedPrefs.getString("prepTime","5");
        Log.d("leave = ",leave);
        cal.add(Calendar.MINUTE, Integer.parseInt(leave));
        Log.d("cal = ", String.valueOf(cal.get(Calendar.MINUTE)));
        timeToLeave = (TextView)findViewById(R.id.time_to_leave);
        if(cal.get(Calendar.HOUR)>12){
            timeToLeave.setText(cal.get(Calendar.HOUR)-12+":"+cal.get(Calendar.MINUTE)+" PM");
        }
        else timeToLeave.setText(cal.get(Calendar.HOUR)+":"+cal.get(Calendar.MINUTE)+" AM");

    }
    String run(String url) throws IOException {
        AsyncTask<String,String,String> task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                OkHttpClient client = new OkHttpClient();
                Log.d("test","test");
                String text;
                Request request = new Request.Builder().url(String.valueOf(params[0])).build();
                try {
                    Response response = client.newCall(request).execute();
                    //Log.d("we made it",response.body().string());
                    String json = response.body().string();
                    JSONObject obj = new JSONObject(json);

                    Log.d("TEST",obj.getJSONObject("currently").toString());
                    JSONObject weather = obj.getJSONObject("currently");
                    String summary = weather.getString("summary");
                    int temp = weather.getInt("temperature");
                    int rain = weather.getInt("precipProbability");
                    text = "The weather is "+summary+" "+temp+" degrees farenheit. Drive safely.";
                    //setWeather(text);
                    this.publishProgress(text);


                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("error","erro");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Object[] values) {
                super.onProgressUpdate(values);


                    super.onProgressUpdate(values);
                    TextView display = (TextView) findViewById(R.id.textView3);
                    display.setText(values[0].toString());

            }

            protected void onPostExceute(String result){

            }
        };
        task.execute(url);
        //Response response = client.newCall(request).execute();
        return "";

    }


}
