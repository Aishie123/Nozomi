package mcm.edu.ph.dones_turnbasedgame.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;

import mcm.edu.ph.dones_turnbasedgame.Controller.MusicPlayerService;
import mcm.edu.ph.dones_turnbasedgame.R;

@SuppressWarnings("FieldCanBeLocal")
public class SettingsScreen extends AppCompatActivity implements ServiceConnection  {
    private AudioManager audioManager;
    private ImageView btnBack;
    private SeekBar musicVolBar, brightBar;
    private Intent goToHome;
    private MusicPlayerService musicPlayerService;
    private int brightness;

    //Content resolver used as a handle to the system's settings
    private ContentResolver cResolver;
    //Window object, that will store a reference to the current window
    private Window window;

    private static final String SCREEN_BRIGHTNESS_MODE = "screen_brightness_mode";
    private static final int SCREEN_BRIGHTNESS_MODE_MANUAL = 0;
    private static final int SCREEN_BRIGHTNESS_MODE_AUTOMATIC = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //hide the action bar

        setContentView(R.layout.activity_settings_screen);
        checkSystemWritePermission();

        btnBack = findViewById(R.id.btnSettingsBack);
        musicVolBar = findViewById(R.id.volumeBar);
        brightBar = findViewById(R.id.brightnessBar);

        //Binding to music service to allow music to unpause. Refer to onServiceConnected method
        Intent musicIntent = new Intent(this, MusicPlayerService.class);
        bindService(musicIntent, this, BIND_AUTO_CREATE);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setVolume();
        setBrightness();

        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                finish();
                goToHome = new Intent(SettingsScreen.this, HomeScreen.class);
                startActivity(goToHome);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        }
        );

    }

    private void setBrightness(){

        //Get the content resolver
        cResolver = getContentResolver();

        //Get the current window
        window = getWindow();

        //Set the seekbar range between 0 and 255
        brightBar.setMax(255);
        //Set the seek bar progress to 1
        brightBar.setKeyProgressIncrement(1);

        try
        {
            //Get the current system brightness
            brightness = Settings.System.getInt(cResolver, Settings.System.SCREEN_BRIGHTNESS);
        }
        catch (Settings.SettingNotFoundException e)
        {
            //Throw an error case it couldn't be retrieved
            Log.e("Error", "Cannot access system brightness");
            e.printStackTrace();
        }

        //Set the progress of the seek bar based on the system's brightness
        brightBar.setProgress(brightness);

        //Register OnSeekBarChangeListener, so it can actually change values
        brightBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                //Set the system brightness using the brightness variable value
                Settings.System.putInt(cResolver, SCREEN_BRIGHTNESS_MODE, SCREEN_BRIGHTNESS_MODE_MANUAL);
                Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
                //Get the current window attributes
                WindowManager.LayoutParams layoutpars = window.getAttributes();
                //Set the brightness of this window
                layoutpars.screenBrightness = brightness / (float)255;
                //Apply attribute changes to this window
                window.setAttributes(layoutpars);
            }

            public void onStartTrackingTouch(SeekBar seekBar)
            {
                //Nothing handled here
            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                //Set the minimal brightness level
                //if seek bar is 20 or any value below
                if(progress<=20)
                {
                    //Set the brightness to 20
                    brightness=20;
                }
                else //brightness is greater than 20
                {
                    //Set brightness variable based on the progress bar
                    brightness = progress;
                }
            }
        });
    }


    private void setVolume()
    {
        try
        {
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            musicVolBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            musicVolBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

            musicVolBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
            {
                @Override
                public void onStopTrackingTouch(SeekBar arg0)
                {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0)
                {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2)
                {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private boolean checkSystemWritePermission() {
        boolean retVal = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            retVal = Settings.System.canWrite(this);
            Log.d("TAG", "Can Write Settings: " + retVal);
            if(retVal){
                ///Permission granted by the user
            }else{
                //permission not granted navigate to permission screen
                openAndroidPermissionsMenu();
            }
        }
        return retVal;
    }

    private void openAndroidPermissionsMenu() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + this.getPackageName()));
        startActivity(intent);
    }

    @Override
    public void onPause(){
        super.onPause();
        if(musicPlayerService!=null){
            musicPlayerService.pauseMusic();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if(musicPlayerService!=null){
            musicPlayerService.unpauseMusic();
        }
    }


    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        MusicPlayerService.MyBinder binder = (MusicPlayerService.MyBinder) iBinder;
        if(binder != null) {
            musicPlayerService = binder.getService();
            musicPlayerService.unpauseMusic();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

}