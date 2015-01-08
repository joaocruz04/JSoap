package pt.joaocruz04.jsoap.weather;

import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.JSoapField;

/**
 * Created by BEWARE S.A. on 07/01/15.
 */
@JSoapClass(namespace = "http://ws.cdyne.com/WeatherWS/")
public class GetCityWeatherByZIP {

    @JSoapField(order = 0, fieldName = "ZIP")
    private String zip;

    public GetCityWeatherByZIP(String zip) {
        this.zip = zip;
    }
}
