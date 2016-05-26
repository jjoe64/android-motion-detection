package de.appsthatmatter.knxpresso.bewegungsmelder;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import de.appsthatmatter.knxpresso.bewegungsmelder.motiondetection.AggregateLumaMotionDetection;
import de.appsthatmatter.knxpresso.bewegungsmelder.motiondetection.MotionDetector;
import de.appsthatmatter.knxpresso.bewegungsmelder.motiondetection.MotionDetectorCallback;

public class MainActivity extends AppCompatActivity {
    private TextView txtStatus;
    private MotionDetector motionDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtStatus = (TextView) findViewById(R.id.txtStatus);

        motionDetector = new MotionDetector(this, (SurfaceView) findViewById(R.id.surfaceView));
        motionDetector.setMotionDetectorCallback(new MotionDetectorCallback() {
            @Override
            public void onMotionDetected() {
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(80);
                txtStatus.setText("Bewegung erkannt");
            }

            @Override
            public void onTooDark() {
                txtStatus.setText("Zu dunkel hier");
            }
        });

        ////// Config Options
        //motionDetector.setCheckInterval(500);
        //motionDetector.setLeniency(20);
        //motionDetector.setMinLuma(1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        motionDetector.onResume();

        if (motionDetector.checkCameraHardware()) {
            txtStatus.setText("Kamera gefunden");
        } else {
            txtStatus.setText("Keine Kamera verfuegbar");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        motionDetector.onPause();
    }

}
