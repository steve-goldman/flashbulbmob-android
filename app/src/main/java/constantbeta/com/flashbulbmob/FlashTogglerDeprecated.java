package constantbeta.com.flashbulbmob;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;

/**
 * Created by steve on 3/10/16.
 */
public class FlashTogglerDeprecated implements FlashToggler {
    private Camera camera;

    public FlashTogglerDeprecated(Context context) throws Exception {
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            throw new Exception("Device does not have flash");
        }
        camera = Camera.open();
    }

    @Override
    public void destroy() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    @Override
    public boolean isFlashOn() {
        return camera != null &&
                camera.getParameters().getFlashMode().equals(Camera.Parameters.FLASH_MODE_TORCH);
    }

    @Override
    public void toggleOn() {
        if (camera != null) {
            if (!isFlashOn()) {
                _toggleOn();
            }
        }
    }

    @Override
    public void toggleOff() {
        if (camera != null) {
            if (isFlashOn()) {
                _toggleOff();
            }
        }
    }

    @Override
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
