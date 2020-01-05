package in.blogspot.tecnopandit.theweatherapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    String result;
    EditText city;
    public class Downloader extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... strings) {
           try {
               result="";
               URL url=new URL(strings[0]);
               HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
               httpURLConnection.connect();
               InputStream inputStream=httpURLConnection.getInputStream();
               InputStreamReader reader=new InputStreamReader(inputStream);
               int data=reader.read();
               while (data!=-1)
               {
                   char current=(char)data;
                   result+=current;
                   data=reader.read();
               }
               return result;
           }
           catch (Exception e){
               return e.toString();

           }
        }

    }
    Downloader downloader;
    String cityname="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv=findViewById(R.id.textView2);
        city=findViewById(R.id.editText);
    }


    public void getweather(View view)
    {
        cityname=city.getText().toString();
        downloader=new Downloader();
        try {
            cityname=city.getText().toString();
            try {
                String datagot=downloader.execute("https://openweathermap.org/data/2.5/weather?q="+cityname+"&appid=b6907d289e10d714a6e88b30761fae22").get();

                Log.i("JSON : ",datagot);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            JSONObject jsonObject=new JSONObject(result);
            String w=jsonObject.getString("weather");
            JSONObject temprature=jsonObject.getJSONObject("main");
            JSONObject wind=jsonObject.getJSONObject("wind");
            String speed=wind.getString("speed");
            String deg=wind.getString("deg");
            String tempis=temprature.getString("temp");
            String humid=temprature.getString("humidity");
            Log.i("Weatehr is: ",w);
            Log.i("temprature",temprature.toString());
            Log.i("temp: ",tempis);
            JSONArray jsonArray=new JSONArray(w);
            tv.setText("");
            for (int i=0;i<jsonArray.length();i++)
            {

                JSONObject jpart=jsonArray.getJSONObject(i);

                tv.append(jpart.getString("main")+"\n");
            }
            tv.append("Feels like "+tempis+"°C"+"\n"+"Humidity: "+humid+"%"+"\n"+"Wind speed: "+speed+"Kmph"+"\n"+"Direction: "+deg+"°");
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }
    }
}
