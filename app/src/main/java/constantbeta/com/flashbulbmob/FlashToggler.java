package constantbeta.com.flashbulbmob;

/**
 * Created by steve on 3/10/16.
 */
public interface FlashToggler {
    boolean isFlashOn();
    void toggle();
    void toggleOn();
    void toggleOff();
    void destroy();
}
