package mcm.edu.ph.dones_turnbasedgame.Controller;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import mcm.edu.ph.dones_turnbasedgame.R;

public class MusicPlayerService extends Service {
    private MediaPlayer player;
    private IBinder mBinder = new MyBinder();
    private Boolean mIsPaused;
    public int currentTrack;
    private final MediaPlayer [] heroAtkSFX = new MediaPlayer[3];
    private final MediaPlayer [] enemyAtkSFX = new MediaPlayer[3];

    public MusicPlayerService() {
    }

    public void onCreate(){
        super.onCreate();
        mIsPaused=true;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class MyBinder extends Binder {
        public MusicPlayerService getService(){
            return MusicPlayerService.this;
        }
    }

    // BGM --------------------------------------------------------------------------------------------------------------------

    public void playMusic(final int music){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(music == 1) playMainMusic();
                if(music == 2) playIntroMusic();
                if(music == 3) playBattleMusic();
                if(music == 4) playVictoryMusic();
                if(music == 5) playGameOverMusic();
            }
        }).start();
    }

    public void playMainMusic(){
        if(player !=null){
            player.stop();
            player.release();
        }
        player = MediaPlayer.create(this, R.raw.music_home);
        currentTrack = 1;
        player.setLooping(true);
        player.start();

    }
    public void playIntroMusic(){
        if(player !=null){
            player.stop();
            player.release();
        }
        player = MediaPlayer.create(this, R.raw.music_intro);
        currentTrack = 2;
        player.setLooping(true);
        player.start();
    }

    public void playBattleMusic(){
        if(player !=null){
            player.stop();
            player.release();
        }
        player = MediaPlayer.create(this, R.raw.music_battle);
        currentTrack = 3;
        player.setLooping(true);
        player.start();
    }

    public void playVictoryMusic(){
        if(player !=null){
            player.stop();
            player.release();
        }
        player = MediaPlayer.create(this, R.raw.music_win);
        currentTrack = 4;
        player.setLooping(false);
        player.start();
    }

    public void playGameOverMusic(){
        if(player !=null){
            player.stop();
            player.release();
        }
        player = MediaPlayer.create(this, R.raw.music_lose);
        currentTrack = 5;
        player.setLooping(false);
        player.start();
    }

    // SFX --------------------------------------------------------------------------------------------

    public void playHeroAtkSFX(int n){
        heroAtkSFX[0] = MediaPlayer.create(this, R.raw.sfx_heroatk1);
        heroAtkSFX[1] = MediaPlayer.create(this, R.raw.sfx_heroatk2);
        heroAtkSFX[2] = MediaPlayer.create(this, R.raw.sfx_heroatk3);
        heroAtkSFX[n].setVolume(100,100);
        heroAtkSFX[n].setLooping(false);
        heroAtkSFX[n].start();
    }

    public void playEnemyAtkSFX(int num, int n){
        if (num == 0){
            enemyAtkSFX[0] = MediaPlayer.create(this, R.raw.sfx_enemyslash1);
            enemyAtkSFX[1] = MediaPlayer.create(this, R.raw.sfx_enemyslash2);
            enemyAtkSFX[2] = MediaPlayer.create(this, R.raw.sfx_enemyslash3);
            enemyAtkSFX[n].setVolume(100,100);
            enemyAtkSFX[n].setLooping(false);
            enemyAtkSFX[n].start();
        }
        else {
            enemyAtkSFX[0] = MediaPlayer.create(this, R.raw.sfx_enemypunch1);
            enemyAtkSFX[1] = MediaPlayer.create(this, R.raw.sfx_enemypunch2);
            enemyAtkSFX[2] = MediaPlayer.create(this, R.raw.sfx_enemypunch3);
            enemyAtkSFX[n].setVolume(200,200);
            enemyAtkSFX[n].setLooping(false);
            enemyAtkSFX[n].start();
        }
    }


    public void playHeroSS1SFX(){
        MediaPlayer heroSS1SFX = MediaPlayer.create(this, R.raw.sfx_heross1);
        heroSS1SFX.setVolume(100,100);
        heroSS1SFX.setLooping(false);
        heroSS1SFX.start();
    }

    public void playHeroSS2SFX(){
        MediaPlayer heroSS2SFX = MediaPlayer.create(this, R.raw.sfx_heross2);
        heroSS2SFX.setVolume(100,100);
        heroSS2SFX.setLooping(false);
        heroSS2SFX.start();
    }

    // ---------------------------------------------------------------------------------------------------------------

    public void pauseMusic(){
        player.pause();
        mIsPaused=true;
    }

    public void unpauseMusic(){
        if (!player.isPlaying()){
            player.start();
            mIsPaused=false;
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent){
        super.onTaskRemoved(rootIntent);
        stopSelf();
    }

}
