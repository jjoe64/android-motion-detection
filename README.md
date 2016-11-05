# Android Motion Detector

## functional description

The preview of the camera will be drawn on a 1x1 pixel surface. At this point the preview input of the camera will be analysed at a given interval.
To detect a movement the picture will be dived into tiles. For each tile the average brightness will be calculated. If a average value of a single tile differs from the previous value, a motion is registered.

## integration

1. Copy all classes of the subpackge `motiondetection` into your project. Adapt the package names.
2. There must be a `SurfaceView` in your layout:
```
<SurfaceView
    android:layout_width="1dp"
    android:layout_height="1dp"
    android:id="@+id/surfaceView" />
```
3. create instance of `MotionDetector`
```
motionDetector = new MotionDetector(this, (SurfaceView) findViewById(R.id.surfaceView));
```
4. call lifecycle methods: `motionDetector.onResume() and .onPause()`
5. register callbacks
```
motionDetector.setMotionDetectorCallback(new MotionDetectorCallback() {
    @Override
    public void onMotionDetected() {
        txtStatus.setText("Bewegung erkannt");
    }

    @Override
    public void onTooDark() {
        txtStatus.setText("Zu dunkel hier");
    }
});
```

## customize parameters

there are some important parameters that can be adjusted at runtime.

| Method | Description | Default |
| --- | --- | --- |
| motionDetector.setCheckInterval(500); | milliseconds between pictures to compare. less = less energy cos t | 500 |
| motionDetector.setLeniency(20); | maximal tolerence of difference in pictures. less = more sensible | 20 |
| motionDetector.setMinLuma(1000); | minimum brightness. if lower the callback onTooDark is called | 1000 |

## notice

1. Permissions must be given in the AndroidManifest.xml
```
<uses-feature android:name="android.hardware.camera" />
<uses-permission android:name="android.permission.CAMERA"/>
```
2. if `targetSdkVersion` is greater than 21 you have to implement Runtime Permissions
3. the detection is only running while the activity is active. It may make sense to build in a Wake-Lock.
4. Parts of the code is taken from https://github.com/phishman3579/android-motion-detection

