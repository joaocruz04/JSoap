package pt.joaocruz04.jsoap.weather;

import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.JSoapResField;
import pt.joaocruz04.lib.misc.SOAPDeserializable;

/**
 * Created by Joao Cruz on 07/01/15.
 */


@JSoapClass(namespace = "http://ws.cdyne.com/WeatherWS/")
public class GetWeatherInformationResponse extends SOAPDeserializable {

    @JSoapResField(name = "GetWeatherInformationResult")
    public WeatherDescription[] result;

}
