package constantbeta.com.flashbulbmob;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;

/**
 * Created by steve on 3/10/16.
 */
public class FlashToggler {
    private final boolean hasFlash;
    private Camera camera;

    public FlashToggler(Context context) throws Exception {
        hasFlash = context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if (!hasFlash) {
            throw new Exception("Device does not have flash");
        }
        camera = Camera.open();
    }

    public void destroy() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    public boolean isFlashOn() {
        return camera != null &&
                camera.getParameters().getFlashMode().equals(Camera.Parameters.FLASH_MODE_TORCH);
    }

    public void toggleOn() {
        if (camera != null) {
            if (!isFlashOn()) {
                _toggleOn();
            }
        }
    }

    public void toggleOff() {
        if (camera != null) {
            if (isFlashOn()) {
                _toggleOff();
            }
        }
    }

    public void toggle() {
        if (camera != null) {
            if (isFlashOn()) {
                _toggleOff();
            } else {
                _toggleOn();
            }
        }
    }

    private void _toggleOn() {
        final Camera.Parameters params = camera.getParameters();
        params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(params);
        camera.startPreview();
    }

    private void _toggleOff() {
        final Camera.Parameters params = camera.getParameters();
        params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(params);
        camera.stopPreview();
    }
}
