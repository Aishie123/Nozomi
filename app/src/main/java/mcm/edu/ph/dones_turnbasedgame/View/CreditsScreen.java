package mcm.edu.ph.dones_turnbasedgame.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import mcm.edu.ph.dones_turnbasedgame.Controller.MusicPlayerService;
import mcm.edu.ph.dones_turnbasedgame.R;

@SuppressWarnings({"FieldCanBeLocal", "ConstantConditions"})
public class CreditsScreen extends AppCompatActivity implements ServiceConnection {

    private TextView txtCredits;
    private Intent launchURL;
    private MusicPlayerService musicPlayerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //hide the action bar
        setContentView(R.layout.activity_credits_screen);

        txtCredits = findViewById(R.id.txtCreditsList);

        txtCredits.setMovementMethod(new ScrollingMovementMethod());

        //Binding to music service to allow music to unpause. Refer to onServiceConnected method
        Intent musicIntent = new Intent(this, MusicPlayerService.class);
        bindService(musicIntent, this, BIND_AUTO_CREATE);

        //instructs users that they can tap on the app icons at the bottom side of the screen
        Toast.makeText(getApplicationContext(),"Tap on the app icons below to redirect.",Toast.LENGTH_LONG).show();

        }

    // goes back to home screen when btnBack is clicked ---------------------------------------------------------------------------------------------------
    public void goBack(View v) {
        finish();
    }

    // goes to developer's social media accounts ----------------------------------------------------------------------------------------

    public void openFacebook(View v) {
        String url = getString(R.string.link_fb);
        Uri uriUrl = Uri.parse(url);
        launchURL = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchURL);
    }

    public void openInstagram(View v) {
        String url = getString(R.string.link_ig);
        Uri uriUrl = Uri.parse(url);
        launchURL = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchURL);
    }

    public void openGithub(View v) {
        String url = getString(R.string.link_git);
        Uri uriUrl = Uri.parse(url);
        launchURL = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchURL);
    }

    public void openBehance(View v) {
        String url = getString(R.string.link_be);
        Uri uriUrl = Uri.parse(url);
        launchURL = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchURL);
    }

    public void openLinkedIn(View v) {
        String url = getString(R.string.link_link);
        Uri uriUrl = Uri.parse(url);
        launchURL = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchURL);
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------

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