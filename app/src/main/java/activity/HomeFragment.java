package activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baseteam.test4.R;
import com.gc.materialdesign.views.ButtonRectangle;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        CheckLogin();

        //MainActivity.shared.GetMusic();

        // Inflate the layout for this fragment
        return rootView;
    }

    public static void CheckLogin()
    {
        ButtonRectangle but = (ButtonRectangle) rootView.findViewById(R.id.button);
        TextView tv = (TextView) rootView.findViewById(R.id.textView);
        if(!VKSdk.isLoggedIn()) return;
        but.setVisibility(View.INVISIBLE);
        tv.setVisibility(View.VISIBLE);


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
