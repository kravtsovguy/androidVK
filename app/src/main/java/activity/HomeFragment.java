package activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baseteam.test4.R;
import com.gc.materialdesign.views.ButtonRectangle;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUser;

public class HomeFragment extends Fragment {


    public static HomeFragment shared;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shared = this;
    }

    public static View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        checkLogin();

        //MainActivity.shared.GetMusic();
        // Inflate the layout for this fragment
        return rootView;
    }

    public static boolean checkLogin()
    {
        ButtonRectangle but = (ButtonRectangle) rootView.findViewById(R.id.button);
        TextView tv = (TextView) rootView.findViewById(R.id.textView);
        if(!VKSdk.isLoggedIn()) return false;
        but.setVisibility(View.INVISIBLE);
        tv.setVisibility(View.VISIBLE);
        shared.getUser();
        return true;
    }

    void getUser() {
        VKRequest r = VKApi.users().get(VKParameters.from(VKApiConst.USER_IDS, VKAccessToken.currentToken().userId, VKApiConst.FIELDS, "photo_200"));
        r.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                try {
                    Log.i("123", response.json.toString());
                    VKApiUser user = new VKApiUser().parse(response.json.getJSONArray("response").getJSONObject(0));
                    Log.i("123", user.photo_200);
                    setProfilePhoto(user.photo_200);
                } catch (Exception e) {
                }
            }
        });
    }
    void setProfilePhoto(String url)
    {
        Picasso.with(getContext()).load(url).transform(new RoundTransformation()).into(FragmentDrawer.imageV);
    }


                @Override
                public void onAttach (Activity activity){
                    super.onAttach(activity);
                }

                @Override
                public void onDetach () {
                    super.onDetach();
                }


            }

class RoundTransformation implements Transformation {

    @Override
    public Bitmap transform(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());

        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
        if (squaredBitmap != source) {
            source.recycle();
        }

        Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);

        float radius = size / 2f;
        canvas.drawCircle(radius, radius, radius, paint);

        squaredBitmap.recycle();
        return bitmap;
    }

    @Override
    public String key() {
        return "circle";
    }
}
