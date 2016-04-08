package constantbeta.com.flashbulbmob;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;

/**
 * Created by steve on 3/10/16.
 */
public class FlashTogglerNew implements FlashToggler {
    private final CameraManager manager;
    private final String cameraId;
    private boolean isFlashOn;

    public FlashTogglerNew(Context context) throws CameraAccessException {
        manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        cameraId = findCamera();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.setTorchMode(cameraId, false);
        }
    }

    @Override
    public boolean isFlashOn() {
        return isFlashOn;
    }

    @Override
    public void toggle() {
        if (isFlashOn()) {
            _toggleOff();
        }
        else {
            _toggleOn();
        }
    }

    @Override
    public void toggleOn() {
        if (!isFlashOn()) {
            _toggleOn();
            isFlashOn = true;
        }
    }

    @Override
    public void toggleOff() {
        if (isFlashOn()) {
            _toggleOff();
            isFlashOn = false;
        }
    }

    @Override
    public void destroy() {
        // no-op
    }

    private void _toggleOn() {
        setTorchMode(true);
    }

    private void _toggleOff() {
        setTorchMode(false);
    }

    private void setTorchMode(final boolean enabled) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                manager.setTorchMode(cameraId, enabled);
            }
        }
        catch (Exception e) {
            System.err.println(e);
        }
    }

    // Looks for any camera with an associated flash device. Returns 1st one found or null if none
    private String findCamera() throws CameraAccessException {
        for (final String cameraId : manager.getCameraIdList()) {
            if (manager.getCameraCharacteristics(cameraId).get(CameraCharacteristics.FLASH_INFO_AVAILABLE)) {
                return cameraId;
            }
        }

        return null;    // No camera with flash found

    }
}
