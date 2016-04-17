package hackdfw2016.smartalarm;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by snidell on 4/16/16.
 */
public class WeatherAPI {
    String APIkey="https://github.com/Kevin-Chung/hackdfw2016.git";
    String url="";
    public WeatherAPI(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        //Response response = client.newCall(request).execute();
    }



}
