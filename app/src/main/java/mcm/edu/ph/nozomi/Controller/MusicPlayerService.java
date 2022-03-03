package mcm.edu.ph.nozomi.Controller;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

import mcm.edu.ph.nozomi.R;

public class MusicPlayerService extends Service {
    MediaPlayer player;
    private IBinder mBinder = new MyBinder();
    private Boolean mIsPaused;
    public int currentTrack;

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
