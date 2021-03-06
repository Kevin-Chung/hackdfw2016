package hackdfw2016.smartalarm;

/**
 * Created by HeyImRige on 4/16/2016.
 */


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BackgroundETAChecker extends BroadcastReceiver {


    private PendingIntent pendingIntent;
    private AlarmManager manager;
    private SharedPreferences preferencesSettings;
    private static final int PREFERENCE_MODE_PRIVATE = 0;
    private SharedPreferences.Editor preferenceEditor;
    Double lat, longi;
    String time,prepTime;
    String arivalTime;
    Context myContext;

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        myContext=arg0;
        preferencesSettings = arg0.getSharedPreferences("Settings", 0);
        arivalTime = preferencesSettings.getString("arivalTime", "error");
        prepTime = preferencesSettings.getString("prepTime","noPrep");
        // For our recurring task, we'll just display a message
        Log.d("prepTime",prepTime);
        Log.i("running", arivalTime);
        /*if(arivalTime.equals("4:201")){
            Intent intent = new Intent(arg0, WakeUpActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            arg0.startActivity(intent);
        }*/

        if(!arivalTime.equals("error")) {
            lat = Double.valueOf(preferencesSettings.getString("lat", ""));
            longi = Double.valueOf(preferencesSettings.getString("long", ""));

            String place = preferencesSettings.getString("place", "");

            String url = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&";
            url += "origins=" + lat + "," + longi;
            url += "&destinations=" + place + "Dallas, TX, United States";
            url += "&key=AIzaSyAVsykzRc9BbaQuMy-ILaywAolcxFK6d2w";
            setTime("1 m");

            try {
                run(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

        String run(String url) throws IOException {
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
                        Log.d("json obj",array.toString());
                        String time = array.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("duration").getString("text").toString();
                        Log.d("we made it",time);
                        setTime(time);
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

    public void setTime(String time2){
        this.time = time2.split(" ")[0];
        Log.d("TIMEEE",time);
        Log.d("time2",time.split(" ")[0]);
        calcTime();
    }

    public void calcTime(){
        //time eta in minutes
        //arivalTime
        //todo get prep time
        Calendar arivalCalendar =Calendar.getInstance();
        String[]tokens = arivalTime.split(":");
        arivalCalendar.set(Calendar.HOUR,Integer.parseInt(tokens[0]));
        arivalCalendar.set(Calendar.MINUTE, Integer.parseInt(tokens[1]));

        Calendar currentTime = Calendar.getInstance();
        Log.d("arival time:", Integer.toString(arivalCalendar.get(Calendar.MINUTE)));
        currentTime.add(Calendar.MINUTE, Integer.parseInt(time));//todo plus prep time
        currentTime.add(Calendar.MINUTE,Integer.parseInt(prepTime));
        Log.d("comparison time:", Integer.toString(currentTime.get(Calendar.MINUTE)));
        Log.d("comparison:", Boolean.toString(currentTime.after(arivalCalendar)));
        if(currentTime.after(arivalCalendar)){
            //stop alarm here
            Intent alarmIntent = new Intent(myContext,BackgroundETAChecker.class);
            pendingIntent = PendingIntent.getBroadcast(myContext, 0, alarmIntent, 0);
            manager = (AlarmManager) myContext.getSystemService(Context.ALARM_SERVICE);
            manager.cancel(pendingIntent);
            //go into intent
            Intent intent = new Intent(myContext, WakeUpActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            myContext.startActivity(intent);
        }
    }

}