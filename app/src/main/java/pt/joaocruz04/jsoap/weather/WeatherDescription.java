package pt.joaocruz04.jsoap.weather;

import pt.joaocruz04.lib.annotations.SoapResponseElement;
import pt.joaocruz04.lib.misc.SOAPDeserializable;

/**
 * Created by BEWARE S.A. on 07/01/15.
 */

public class WeatherDescription extends SOAPDeserializable {

    @SoapResponseElement(name = "WeatherID")
    private int weatherID;
    @SoapResponseElement(name = "Description")
    private String description;
    @SoapResponseElement(name = "PictureURL")
    private String pictureURL;


}
