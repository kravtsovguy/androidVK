package activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.MediaController;

import com.baseteam.test4.MusicController;
import com.baseteam.test4.MusicService;
import com.baseteam.test4.MusicService.MusicBinder;
import com.baseteam.test4.R;
import com.baseteam.test4.VKAdapterAudio;
import com.baseteam.test4.VKaudio;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class MusicFragment extends Fragment implements MediaController.MediaPlayerControl {

    public MusicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setController();



    }

    @Override
    public void onStart() {
        super.onStart();

        //musicSrv = new MusicService();

        if(playIntent==null) {
            playIntent = new Intent(MainActivity.shared, MusicService.class);
            MainActivity.shared.bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            MainActivity.shared.startService(playIntent);
        }
    }

    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound=false;
    private boolean paused=false, playbackPaused=false;


    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicBinder binder = (MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            //pass list
            //musicSrv.setList(music);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    private static MusicController controller;
    private void setController(){
        //set the controller up
        controller = new MusicController(getActivity());
        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });
        controller.setMediaPlayer(this);
        if(list!=null) controller.setAnchorView(list);
        controller.setEnabled(true);
    }

    public static VKaudio[] music;
    public static View rootView;
    ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_music, container, false);




        list = (ListView)rootView.findViewById(R.id.listView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    //VKaudio audio = music[position];
                    //PlayAudio(position);
                    musicSrv.setSong(position);
                    musicSrv.playSong();
                    if(playbackPaused){
                        setController();
                        playbackPaused=false;
                    }
                    controller.show(0);

                    /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(audio.url));
                    startActivity(browserIntent);*/

                } catch (Exception e) {

                }
            }
        });
        controller.setAnchorView(list);


        CheckMusic();
        SetList(music);


        return rootView;
    }
    void  SetList(VKaudio[] mas)
    {
        if(mas == null) return;

        VKAdapterAudio ad = new VKAdapterAudio(this.getContext(), mas);
        list.setAdapter(ad);
    }

    void CheckMusic()
    {
        if(music != null) return;
        GetMusic();
    }

    /*public static MediaPlayer player = new MediaPlayer();
    void PlayAudio(int i)
    {
        try {

            player.stop();
            player = new MediaPlayer();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(music[i].url);
            //player.prepare();
            //player.start();
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    player.start();
                }
            });
            player.prepareAsync();
        }catch (Exception e){}
    }*/


    public void GetMusic()
    {
        VKRequest r = VKApi.audio().get(VKParameters.from(VKApiConst.OWNER_ID, VKSdk.getAccessToken().userId, "need_user", 0, "count", 10));
        r.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                Log.i("123", response.json.toString());
                try {
                    JSONObject jj = response.json.getJSONObject("response");
                    JSONArray mas = jj.getJSONArray("items");
                    VKaudio[] aud = new VKaudio[mas.length()];
                    for (int i = 0; i < mas.length(); i++) {
                        JSONObject j = mas.getJSONObject(i);
                        int id = j.getInt("id");
                        String artist = j.getString("artist");
                        String title = j.getString("title");
                        int duration = j.getInt("duration");
                        String url = j.getString("url");
                        aud[i] = new VKaudio(id, artist, title, duration, url);

                    }
                    music = aud;
                    musicSrv.setList(music);
                    SetList(music);
                } catch (Exception e) {
                    Log.i("1234", e.toString());
                }
            }
        });
    }

    private void playNext(){
        musicSrv.playNext();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        controller.show(0);
    }

    private void playPrev(){
        musicSrv.playPrev();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        controller.show(0);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause(){
        super.onPause();
        paused=true;
    }
    @Override
    public void onResume(){
        super.onResume();
        if(paused){
            setController();
            paused=false;
        }
    }

    @Override
    public void onStop() {
        controller.hide();
        super.onStop();
    }


    @Override
    public void pause() {
        playbackPaused=true;
        musicSrv.pausePlayer();
    }

    @Override
    public void seekTo(int pos) {
        musicSrv.seek(pos);
    }

    @Override
    public void start() {
        musicSrv.go();
    }

    @Override
    public int getDuration() {
        if(musicSrv!=null &&  musicBound && musicSrv.isPng())
            return musicSrv.getDur();
        else return 0;
    }

    @Override
    public int getCurrentPosition() {
        if(musicSrv!=null && musicBound && musicSrv.isPng())
        return musicSrv.getPosn();
        else return 0;
    }

    @Override
    public boolean isPlaying() {
        if(musicSrv!=null && musicBound)
        return musicSrv.isPng();
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }


}
