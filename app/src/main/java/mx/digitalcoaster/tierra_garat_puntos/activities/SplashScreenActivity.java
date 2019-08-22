package mx.digitalcoaster.tierra_garat_puntos.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;

import java.util.Timer;
import java.util.TimerTask;

import io.fabric.sdk.android.Fabric;
import mx.digitalcoaster.tierra_garat_puntos.R;

//import com.crashlytics.android.Crashlytics;
//import info.androidhive.materialdesign.R;
//import io.fabric.sdk.android.Fabric;

public class SplashScreenActivity extends AppCompatActivity {

    private static final long SPLASH_SCREEN_DELAY = 3000;
    ImageView iv1, iv2, iv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics ())
                .debuggable(true)
                .build();
        Fabric.with(fabric);

        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Hide title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        //Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash_screen);

        iv1 = findViewById (R.id.splash1);
        iv2 = findViewById (R.id.splash2);
        iv3 = findViewById (R.id.splash3);


        int random = (int)(Math.random() * 3 + 1);

        if(random == 1){
            iv1.setVisibility (View.VISIBLE);
            iv2.setVisibility (View.GONE);
            iv3.setVisibility (View.GONE);

        } else  if(random == 2){
            iv1.setVisibility (View.GONE);
            iv2.setVisibility (View.VISIBLE);
            iv3.setVisibility (View.GONE);

        }else  if(random == 3){
            iv1.setVisibility (View.GONE);
            iv2.setVisibility (View.GONE);
            iv3.setVisibility (View.VISIBLE);

        }

        TimerTask task = new TimerTask () {
            @Override
            public void run() {

                // Start the next activity
                Intent mainIntent = new Intent ().setClass( SplashScreenActivity.this, WelcomeActivity.class);
                startActivity(mainIntent);

                // Close the activity so the user won't able to go back this
                // activity pressing Back button
                finish();
            }
        };

        // Simulate a long loading process on application startup.
        Timer timer = new Timer ();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
    }
}
