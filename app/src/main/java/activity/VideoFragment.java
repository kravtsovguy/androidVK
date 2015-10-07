package activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.baseteam.test4.R;

public class VideoFragment extends Fragment {

    public VideoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_video, container, false);


        // установите свой путь к файлу на SD-карточке

        //String videoSource ="/sdcard/Video/ana.mpeg";
        String videoSource ="https://cs542300.vk.me/2/u3400922/videos/d65c5d132b.720.mp4";
        //http://www.androidbegin.com/tutorial/AndroidCommercial.3gp


        final VideoView video = (VideoView) rootView.findViewById(R.id.videoView);

        MediaController mediaController = new MediaController(getActivity());
        mediaController.setAnchorView(video);


        video.setVideoURI(Uri.parse(videoSource));
        video.setMediaController(mediaController);

        video.requestFocus();
        video.start();

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
