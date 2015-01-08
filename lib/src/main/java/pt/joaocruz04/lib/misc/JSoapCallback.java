package pt.joaocruz04.lib.misc;

/**
 * Created by Joao Cruz on 08/01/15.
 */
public abstract class JSoapCallback {
    public abstract void onSuccess(Object result);
    public abstract void onError(int error);
    public void onDebugMessage(String title, String message) {}
}
