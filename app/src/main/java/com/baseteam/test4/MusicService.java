package com.baseteam.test4;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import activity.MainActivity;

/**
 * Created by matvey on 07.10.15.
 */
public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    private final IBinder musicBind = new MusicBinder();
    //media player
    private MediaPlayer player;
    //song list
    private ArrayList<VKaudio> songs;
    //current position
    private int songPosn;



    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent){
        player.stop();
        player.release();
        return false;
    }



    public void onCreate(){
        //create the service
        super.onCreate();
        songPosn=0;
        player = new MediaPlayer();
        initMusicPlayer();
    }

    public void playSong(){
        Log.w("123","play audio");
        player.reset();

        try {
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(songs.get(songPosn).url);
            player.prepareAsync();
        }catch (Exception e){
            Log.e("123","error - play audio");

        }
    }
    public void setSong(int songIndex){
        songPosn=songIndex;
    }



    public void initMusicPlayer(){
        //set player properties
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void setList(VKaudio[] theSongs){

        songs=new ArrayList<VKaudio>(Arrays.asList(theSongs));
    }
    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(player.getCurrentPosition()<0){
            mp.reset();
            playNext();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    private static final int NOTIFY_ID=1;
    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();

        /*Intent notIntent = new Intent(this, MainActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(pendInt)
                .setSmallIcon(R.drawable.vk)
                .setTicker(songs.get(songPosn).title)
                .setOngoing(true)
                .setContentTitle("Playing")
        .setContentText(songs.get(songPosn).title);
        Notification not = builder.build();

        startForeground(NOTIFY_ID, not);*/
    }
    @Override
    public void onDestroy() {
        stopForeground(true);
    }



    public int getPosn(){
        return player.getCurrentPosition();
    }

    public int getDur(){
        return player.getDuration();
    }

    public boolean isPng(){
        return player.isPlaying();
    }

    public void pausePlayer(){
        player.pause();
    }

    public void seek(int posn){
        player.seekTo(posn);
    }

    public void go(){
        player.start();
    }

    public void playPrev(){
        songPosn--;
        if(songPosn<0) songPosn=songs.size()-1;
        playSong();
    }
    public void playNext(){
        songPosn++;
        if(songPosn>songs.size()) songPosn=0;
        playSong();
    }


}
