package de.appsthatmatter.knxpresso.bewegungsmelder.motiondetection;

/**
 * Created by jonas on 26.05.16.
 */
public interface MotionDetectorCallback {
    void onMotionDetected();
    void onTooDark();
}
