package pt.joaocruz04.jsoap.weather;

import pt.joaocruz04.lib.annotations.JSoapClass;
import pt.joaocruz04.lib.annotations.SoapResponseElement;
import pt.joaocruz04.lib.misc.SOAPDeserializable;

/**
 * Created by BEWARE S.A. on 07/01/15.
 */


@JSoapClass(namespace = "http://ws.cdyne.com/WeatherWS/")
public class GetWeatherInformationResponse extends SOAPDeserializable {

    @SoapResponseElement(name = "GetWeatherInformationResult")
    public WeatherDescription[] GetWeatherInformationResult;

}
