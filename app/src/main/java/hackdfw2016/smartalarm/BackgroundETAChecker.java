package hackdfw2016.smartalarm;

/**
 * Created by HeyImRige on 4/16/2016.
 */


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BackgroundETAChecker extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        System.out.println("OISDFDSOIFSDF\n");
        // For our recurring task, we'll just display a message
        Toast.makeText(arg0, "I'm running...BackgroundETA Chekcer", Toast.LENGTH_SHORT).show();
    }
}