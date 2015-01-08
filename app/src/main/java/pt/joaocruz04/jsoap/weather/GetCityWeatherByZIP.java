package pt.joaocruz04.jsoap.weather;

import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.JSoapReqField;

/**
 * Created by Joao Cruz on 07/01/15.
 */
@JSoapClass(namespace = "http://ws.cdyne.com/WeatherWS/")
public class GetCityWeatherByZIP {

    @JSoapReqField(order = 0, fieldName = "ZIP")
    private String zip;

    public GetCityWeatherByZIP(String zip) {
        this.zip = zip;
    }
}
