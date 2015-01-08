package pt.joaocruz04.jsoap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import pt.joaocruz04.jsoap.weather.GetCityWeatherByZIP;
import pt.joaocruz04.jsoap.weather.GetWeatherInformation;
import pt.joaocruz04.jsoap.weather.GetWeatherInformationResponse;
import pt.joaocruz04.jsoap.weather.WeatherReturn;
import pt.joaocruz04.lib.SOAPManager;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.get_weather_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeatherInfo();
            }
        });

        findViewById(R.id.get_weather_by_zip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeatherByZip();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        //getWeatherByZip();
    }

    private void getWeatherByZip() {
        String url="http://wsf.cdyne.com/WeatherWS/Weather.asmx";
        String namespace="http://ws.cdyne.com/WeatherWS/";
        String method = "GetCityWeatherByZIP";
        String soap_action = "http://ws.cdyne.com/WeatherWS/GetCityWeatherByZIP";

        SOAPManager.get(namespace, url, method, soap_action, new GetCityWeatherByZIP("10001"), WeatherReturn.class, null, new SOAPManager.WSCallback() {

            @Override
            public void onSuccess(Object result) {
                WeatherReturn res = (WeatherReturn)result;
                System.out.println("There!");
            }

            @Override
            public void onError(SOAPManager.WSError error) {

            }
        });

    }



    private void getWeatherInfo() {
        String url="http://wsf.cdyne.com/WeatherWS/Weather.asmx";
        String namespace="http://ws.cdyne.com/WeatherWS";
        String method = "GetWeatherInformation";
        String soap_action = "http://ws.cdyne.com/WeatherWS/GetWeatherInformation";

        SOAPManager.get(namespace, url, method, soap_action, new GetWeatherInformation(), GetWeatherInformationResponse.class, null, new SOAPManager.WSCallback() {
            @Override
            public void onError(SOAPManager.WSError error) {

            }

            @Override
            public void onDebugMessage(String title, String message) {

            }

            @Override
            public void onSuccess(Object result) {
                GetWeatherInformationResponse res = (GetWeatherInformationResponse)result;
                System.out.println("There!");
            }
        });
    }

}
