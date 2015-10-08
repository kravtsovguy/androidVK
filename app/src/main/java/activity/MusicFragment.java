package activity;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.MediaController;

import com.baseteam.test4.MusicController;
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

public class MusicFragment extends Fragment implements MediaController.MediaPlayerControl,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener {

    public MusicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setController();

        initMusicPlayer();
    }

    @Override
    public void onStart() {
        super.onStart();


    }






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


    private boolean paused=false, playbackPaused=false;

    public static View rootView;

    public static VKaudio[] music;
    VKaudio current;
    Integer cur=-1;
    ListView list;
    void initList()
    {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    if(position == cur) return;
                    cur = position;
                    PlayAudio();
                    /*if(playbackPaused){
                        setController();
                        playbackPaused=false;
                    }*/
                    //if(!controller.isShown()) controller.show(0);

                    /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(audio.url));
                    startActivity(browserIntent);*/

                } catch (Exception e) {

                }
            }
        });
        controller.setAnchorView(list);
        SetList(music);
    }
    void  SetList(VKaudio[] mas)
    {
        if(mas == null) return;

        VKAdapterAudio ad = new VKAdapterAudio(this.getContext(), mas);
        list.setAdapter(ad);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_music, container, false);


        list = (ListView)rootView.findViewById(R.id.listView);
        initList();

        CheckMusic();


        return rootView;
    }




    public static MediaPlayer player = new MediaPlayer();
    public void initMusicPlayer(){
        //set player properties
        player.setWakeMode(getContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
        player.setOnInfoListener(this);
        player.setOnBufferingUpdateListener(this);
    }

    void PlayAudio()
    {
        Log.w("123", "play audio");
        player.reset();

        try {
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(music[cur].url);
            player.prepareAsync();
        }catch (Exception e){
            Log.e("123","error - play audio");

        }
    }


    void CheckMusic()
    {
        if(music != null) return;
        GetMusic();
    }
    public void GetMusic()
    {
        VKRequest r = VKApi.audio().get(VKParameters.from(VKApiConst.OWNER_ID, VKSdk.getAccessToken().userId, "need_user", 0, "count", 100));
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
                    SetList(music);
                } catch (Exception e) {
                    Log.i("1234", e.toString());
                }
            }
        });
    }

    private void playNext(){

        cur++;
        if(cur >music.length) cur =0;
        PlayAudio();
        //controller.show(0);
    }

    private void playPrev(){
        cur--;
        if(cur<0) cur =music.length-1;
        PlayAudio();
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
        player.pause();
    }

    @Override
    public void seekTo(int pos) {
        player.seekTo(pos);
    }

    @Override
    public void start() {
        player.start();
    }

    @Override
    public int getDuration() {
        return player.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return player.getCurrentPosition();
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
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



    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.i("onCompletion", String.valueOf(player.getCurrentPosition()));

        if(player.getCurrentPosition()>0)
        {
            mp.reset();
            playNext();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    public void onPrepared(MediaPlayer mp) {
        //mp.start();
    }


    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        //mp.seekTo(0);
        return true;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        Log.i("buf", String.valueOf(percent));
        if(mp.isPlaying()) return;
        if(percent<=1) return;
        mp.start();
        if(!controller.isShown()) controller.show();

    }
}
