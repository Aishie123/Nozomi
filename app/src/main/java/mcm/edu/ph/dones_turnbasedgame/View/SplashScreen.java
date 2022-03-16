package mcm.edu.ph.dones_turnbasedgame.View;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import mcm.edu.ph.dones_turnbasedgame.R;

@SuppressWarnings("FieldCanBeLocal")
@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    private ProgressBar splashProgress;
    private final int SPLASH_TIME = 3000; //This is 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //hide the action bar
        setContentView(R.layout.activity_splash_screen);

        //This is additional feature, used to run a progress bar
        splashProgress = findViewById(R.id.splashProgress);
        playProgress();

        //Code to start timer and take action after the timer ends
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do any action here. Now we are moving to next page
                Intent mySuperIntent = new Intent(SplashScreen.this, HomeScreen.class);
                startActivity(mySuperIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                //This 'finish()' is for exiting the app when back button pressed from Home page which is ActivityHome
                finish();

            }
        }, SPLASH_TIME);
    }

    //Method to run progress bar for 3 seconds
    private void playProgress() {
        ObjectAnimator.ofInt(splashProgress, "progress", 100)
                .setDuration(3000)
                .start();
    }

}

