package pt.joaocruz04.jsoap.weather;

import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.SoapResponseElement;
import pt.joaocruz04.lib.misc.SOAPDeserializable;

/**
 * Created by BEWARE S.A. on 07/01/15.
 */

@JSoapClass(namespace = "http://ws.cdyne.com/WeatherWS/")
public class WeatherReturn extends SOAPDeserializable {

    @SoapResponseElement(name = "Success")
    private boolean success;

    @SoapResponseElement(name = "ResponseText")
    private String responseText;

    @SoapResponseElement(name = "City")
    private String city;

    @SoapResponseElement(name = "WeatherStationCity")
    private String stationCity;

    @SoapResponseElement(name = "WeatherID")
    private String weatherID;

    @SoapResponseElement(name = "Description")
    private String description;

    @SoapResponseElement(name = "Temperature")
    private String temperature;

    @SoapResponseElement(name = "RelativeHumidity")
    private String relHumidity;

    @SoapResponseElement(name = "Wind")
    private String wind;

    @SoapResponseElement(name = "Pressure")
    private String pressure;

    @SoapResponseElement(name = "Visibility")
    private String visibility;

    @SoapResponseElement(name = "WindChill")
    private String windChill;

    @SoapResponseElement(name = "Remarks")
    private String remarks;


}
