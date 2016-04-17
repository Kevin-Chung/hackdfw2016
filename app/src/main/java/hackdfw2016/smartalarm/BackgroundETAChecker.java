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
import android.util.Log;
import android.widget.Toast;

public class BackgroundETAChecker extends BroadcastReceiver {


    private PendingIntent pendingIntent;
    private AlarmManager manager;
    private SharedPreferences preferencesSettings;
    private static final int PREFERENCE_MODE_PRIVATE = 0;
    private SharedPreferences.Editor preferenceEditor;
    @Override
    public void onReceive(Context arg0, Intent arg1) {
        preferencesSettings = arg0.getSharedPreferences("Settings", 0);
        String test = preferencesSettings.getString("arivalTime", "error");
        preferenceEditor=preferencesSettings.edit();
        preferenceEditor.putString("arivalTime", test + "1");
        preferenceEditor.commit();
        // For our recurring task, we'll just display a message
        Toast.makeText(arg0, "HELLO"+test, Toast.LENGTH_SHORT).show();
        Log.i("running",test );
        if(test.equals("4:201")){
            Intent intent = new Intent(arg0, WakeUpActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            arg0.startActivity(intent);
        }
        Intent alarmIntent = new Intent(arg0,BackgroundETAChecker.class);
        pendingIntent = PendingIntent.getBroadcast(arg0, 0, alarmIntent, 0);
        manager = (AlarmManager) arg0.getSystemService(Context.ALARM_SERVICE);
        long interval = 1000000000;
        //manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        manager.setExact(AlarmManager.RTC_WAKEUP, interval, pendingIntent);
    }
}