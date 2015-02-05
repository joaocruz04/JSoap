package pt.joaocruz04.jsoap.weather;

import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.JSoapResField;
import pt.joaocruz04.lib.misc.SOAPDeserializable;

/**
 * Created by Joao Cruz on 07/01/15.
 */

@JSoapClass(namespace = "http://ws.cdyne.com/WeatherWS/")
public class WeatherReturn {

    @JSoapResField(name = "Success")
    private boolean success;

    @JSoapResField
    private String ResponseText;

    @JSoapResField(name = "City")
    private String city;

    @JSoapResField(name = "WeatherStationCity")
    private String stationCity;

    @JSoapResField(name = "WeatherID")
    private String weatherID;

    @JSoapResField(name = "Description")
    private String description;

    @JSoapResField(name = "Temperature")
    private String temperature;

    @JSoapResField(name = "RelativeHumidity")
    private String relHumidity;

    @JSoapResField(name = "Wind")
    private String wind;

    @JSoapResField(name = "Pressure")
    private String pressure;

    @JSoapResField(name = "Visibility")
    private String visibility;

    @JSoapResField(name = "WindChill")
    private String windChill;

    @JSoapResField(name = "Remarks")
    private String remarks;


}
