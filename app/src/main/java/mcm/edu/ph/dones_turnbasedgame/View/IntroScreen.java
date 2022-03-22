package mcm.edu.ph.dones_turnbasedgame.View;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import mcm.edu.ph.dones_turnbasedgame.Controller.MusicPlayerService;
import mcm.edu.ph.dones_turnbasedgame.Model.HeroData;
import mcm.edu.ph.dones_turnbasedgame.R;

@SuppressWarnings({"ConstantConditions", "FieldCanBeLocal"})
public class IntroScreen extends AppCompatActivity implements ServiceConnection{

    private TextView nameQuestion, enemyQuestion;
    private EditText userInput;
    private ImageView btnBack, btnMenu;
    private ImageButton btnNext;
    private MusicPlayerService musicPlayerService;
    private String userName, enemyName;
    private final String TAG = "IntroScreen";
    private final boolean noRestart = true; // disable restart in the menu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //hide the action bar

        setContentView(R.layout.activity_intro_screen);

        Intent musicIntent = new Intent(this, MusicPlayerService.class);
        bindService(musicIntent, (ServiceConnection) this, BIND_AUTO_CREATE);

        nameQuestion = findViewById(R.id.nameQuestion);
        enemyQuestion = findViewById(R.id.enemyQuestion);
        btnBack = findViewById(R.id.btnBack);
        userInput = findViewById(R.id.userInput);
        btnNext = findViewById(R.id.btnIntroNext);
        btnMenu = findViewById(R.id.btnIntroMenu);

        userInput();
        press();
    }

    // onClick -----------------------------------------------------------------------------------------

    public void userInput(){

            btnNext.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    HeroData hero = new HeroData();
                    if (v.getId() == R.id.btnIntroNext){

                        enemyQuestion.setVisibility(View.VISIBLE);
                        nameQuestion.setVisibility(View.INVISIBLE);
                        btnBack.setVisibility(View.VISIBLE);

                        userName = userInput.getText().toString(); // gets user's name from user input
                        userInput.setText("");

                        Log.d(TAG, "The user's name is " + userName);
                        hero.setName(userName);

                        btnNext.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                enemyName = userInput.getText().toString(); // gets enemy's name from enemy input
                                Log.d(TAG, "The enemy's name is " + enemyName);
                                Intent i = new Intent(IntroScreen.this, BattleScreen.class);
                                i.putExtra("enemy", enemyName);
                                i.putExtra("user", userName);
                                startActivity(i);
                                finish();
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                            }
                        });
                    }
                    else{
                        enemyQuestion.setVisibility(View.INVISIBLE);
                        nameQuestion.setVisibility(View.VISIBLE);
                        btnBack.setVisibility(View.INVISIBLE);
                    }
                }
            });
    }

    public void back(View v){
        enemyQuestion.setVisibility(View.INVISIBLE);
        nameQuestion.setVisibility(View.VISIBLE);
        btnBack.setVisibility(View.INVISIBLE);
        userInput.setText("");
        userInput();
    }

    // opens menu
    public void introToMenu(View v){
        Intent goToMenu = new Intent(IntroScreen.this, MenuScreen.class);
        goToMenu.putExtra("no restart", noRestart);
        startActivity(goToMenu);
    }

    //changing button shades when pressed -----------------------------------------------------------------------------------------
    @SuppressLint("ClickableViewAccessibility")
    public void press() {

        btnNext.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnNext.setImageResource(R.drawable.btn_pressed);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnNext.setImageResource(R.drawable.btn_unpressed);
                }
                return false;
            }
        });
    }

    // ----------------------------------------------------------------------------------------------------------

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
            if(musicPlayerService.currentTrack == 2){
                musicPlayerService.unpauseMusic();
            }else{
                musicPlayerService.playMusic(2);
            }

        }
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        MusicPlayerService.MyBinder binder = (MusicPlayerService.MyBinder) iBinder;
        if(binder != null) {
            musicPlayerService = binder.getService();
            musicPlayerService.playMusic(2);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

}
