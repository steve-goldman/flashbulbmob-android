package constantbeta.com.flashbulbmob;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by steve on 4/6/16.
 */
public class TorchRepeater
{
    private FlashToggler flashToggler;

    private final Timer timer = new Timer();

    private final int onMillis;
    private final int offMillis;

    private boolean isRunning = false;

    public TorchRepeater(final FlashToggler flashToggler, final int onMillis, final int offMillis)
    {
        this.flashToggler = flashToggler;
        this.onMillis = onMillis;
        this.offMillis = offMillis;
    }

    public void start()
    {
        if (flashToggler != null)
        {
            if (!isRunning)
            {
                isRunning = true;
                turnOn();
            }
        }
    }

    public void stop()
    {
        if (flashToggler != null)
        {
            if (isRunning)
            {
                isRunning = false;
                flashToggler.toggleOff();
            }
        }
    }

    private void turnOn()
    {
        if (isRunning)
        {
            timer.schedule(new TimerTask()
            {
                @Override
                public void run()
                {
                    turnOff();
                }
            }, onMillis);
            flashToggler.toggleOn();
        }
    }

    private void turnOff()
    {
        if (isRunning)
        {
            timer.schedule(new TimerTask()
            {
                @Override
                public void run()
                {
                    turnOn();
                }
            }, offMillis);
            flashToggler.toggleOff();
        }
    }
}
