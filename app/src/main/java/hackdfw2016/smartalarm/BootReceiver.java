package hackdfw2016.smartalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by HeyImRige on 4/16/2016.
 */
public class BootReceiver extends BroadcastReceiver {

    private PendingIntent pendingIntent;
    private AlarmManager manager;


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            //debug stuff for starting the alarm
            Toast.makeText(context, "SMART ALARM BACKGROUND RUNNING1",Toast.LENGTH_SHORT).show();




            //setup repeating alarm
            /*Intent alarmIntent = new Intent(context,BackgroundETAChecker.class);
            pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
            manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            int interval = 10000;
            manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
            Toast.makeText(context, "Alarm Set", Toast.LENGTH_SHORT).show();*/
        }
    }
}
