package pt.joaocruz04.jsoap.weather;

import pt.joaocruz04.lib.annotations.JSoapResField;
import pt.joaocruz04.lib.misc.SOAPDeserializable;

/**
 * Created by Joao Cruz on 07/01/15.
 */

public class WeatherDescription {

    @JSoapResField(name = "WeatherID")
    private int weatherID;
    @JSoapResField(name = "Description")
    private String description;
    @JSoapResField(name = "PictureURL")
    private String pictureURL;

}
