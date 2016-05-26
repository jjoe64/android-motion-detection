package de.appsthatmatter.knxpresso.bewegungsmelder.motiondetection;

import android.os.Handler;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by jonas on 26.05.16.
 */
public class MotionDetector extends Thread {
    private final AggregateLumaMotionDetection detector;
    private long checkInterval = 500;
    private long lastCheck = 0;
    private MotionDetectorCallback motionDetectorCallback;
    private AtomicBoolean isRunning = new AtomicBoolean(true);
    private Handler mHandler = new Handler();

    private AtomicReference<byte[]> nextData = new AtomicReference<>();
    private AtomicInteger nextWidth = new AtomicInteger();
    private AtomicInteger nextHeight = new AtomicInteger();
    private int minLuma = 1000;

    public MotionDetector() {
        detector = new AggregateLumaMotionDetection();
    }

    @Override
    public void run() {
        while (isRunning.get()) {
            long now = System.currentTimeMillis();
            if (now-lastCheck > checkInterval) {
                lastCheck = now;

                if (nextData.get() != null) {
                    int[] img = ImageProcessing.decodeYUV420SPtoLuma(nextData.get(), nextWidth.get(), nextHeight.get());

                    // check if it is too dark
                    int lumaSum = 0;
                    for (int i : img) {
                        lumaSum += i;
                    }
                    if (lumaSum < minLuma) {
                        if (motionDetectorCallback != null) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    motionDetectorCallback.onTooDark();
                                }
                            });
                        }
                    } else if (detector.detect(img, nextWidth.get(), nextHeight.get())) {
                        // check
                        if (motionDetectorCallback != null) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    motionDetectorCallback.onMotionDetected();
                                }
                            });
                        }
                    }
                }
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopDetection() {
        isRunning.set(false);
    }

    public void setMotionDetectorCallback(MotionDetectorCallback motionDetectorCallback) {
        this.motionDetectorCallback = motionDetectorCallback;
    }

    public void consume(byte[] data, int width, int height) {
        nextData.set(data);
        nextWidth.set(width);
        nextHeight.set(height);
    }

    public void setCheckInterval(long checkInterval) {
        this.checkInterval = checkInterval;
    }

    public void setMinLuma(int minLuma) {
        this.minLuma = minLuma;
    }

    public void setLeniency(int l) {
        detector.setLeniency(l);
    }
}
