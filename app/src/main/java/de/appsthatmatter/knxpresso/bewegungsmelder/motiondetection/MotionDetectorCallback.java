package de.appsthatmatter.knxpresso.bewegungsmelder.motiondetection;

public interface MotionDetectorCallback {
    void onMotionDetected();
    void onTooDark();
}
