package mx.digitalcoaster.tierra_garat_puntos.fragments.welcome;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import mx.digitalcoaster.tierra_garat_puntos.R;
import mx.digitalcoaster.tierra_garat_puntos.activities.AccessActivity;
import mx.digitalcoaster.tierra_garat_puntos.activities.MainActivity;
import mx.digitalcoaster.tierra_garat_puntos.activities.WelcomeActivity;
import mx.digitalcoaster.tierra_garat_puntos.helpers.AnalyticsApplication;
import mx.digitalcoaster.tierra_garat_puntos.helpers.OnSwipeTouchListener;
import mx.digitalcoaster.tierra_garat_puntos.interfaces.CleanMemory;
import mx.digitalcoaster.tierra_garat_puntos.preferences.PreferenceUtils;


public class Welcome2Fragment extends Fragment implements CleanMemory {


    Typeface typeFace;
    TextView ubicaTV, tiendaTV;
    Handler mHandler;
    ImageView view1;

    private Tracker mTracker;     //Analytics


    public Welcome2Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Analytics
        AnalyticsApplication application = (AnalyticsApplication) getContext();
        mTracker = application.getDefaultTracker();
        // Inflate the layout for this fragment
        inflater = getActivity ().getLayoutInflater ();

        View view = inflater.inflate (R.layout.fragment_welcome2, null, false);
        if (view != null) {

            //ubicaTV = view.findViewById(R.id.ubicaTV);
            //tiendaTV = view.findViewById (R.id.tiendasTV);

           /* //Accion despues de cierto tiempo
            mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    //start your activity here
                    ((WelcomeActivity) getActivity ()).setViewPager (2);
                }

            }, 2000L);*/
            //changeFonts ();
            view1 = view.findViewById(R.id.view);


        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);

        view1.setOnTouchListener(new OnSwipeTouchListener(getActivity()){
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                Toast.makeText(getActivity(), "Right", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                Toast.makeText(getActivity(), "Left", Toast.LENGTH_SHORT).show();

            }
        });


    }


    //Se cambia el font de los textos
    public void changeFonts(){
        typeFace = Typeface.createFromAsset(getActivity ().getAssets(),"fonts/gotham_light.ttf");
        ubicaTV.setTypeface(typeFace);

        typeFace = Typeface.createFromAsset(getActivity().getAssets(),"fonts/gotham_bold.ttf");
        tiendaTV.setTypeface(typeFace);

    }

    @Override
    public void onDestroy() {
        super.onDestroy ();
        cleanMemory ();

    }

    @Override
    public void cleanMemory() {
        typeFace = null;
        mHandler = null;


    }

    @Override
    public void onResume() {
        super.onResume();
        //Analytics
        mTracker.setScreenName("Welcome1Fragment");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

}
