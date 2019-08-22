package mx.digitalcoaster.tierra_garat_puntos.fragments.welcome;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
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
import mx.digitalcoaster.tierra_garat_puntos.preferences.CustomTypefaceSpan;
import mx.digitalcoaster.tierra_garat_puntos.preferences.PreferenceUtils;

import static com.facebook.FacebookSdk.getApplicationContext;


public class Welcome3Fragment extends Fragment implements CleanMemory {


    PreferenceUtils utils = new PreferenceUtils();
    TextView textView1, textView2;
    Typeface typeFace;
    TextView ubicaTV, tiendaTV;
    Handler mHandler;
    ImageView view1;

    //Analytics
    private Tracker mTracker;


    public Welcome3Fragment() {
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
        View view = inflater.inflate (R.layout.fragment_welcome3, container, false);
        if (view != null) {
            view1 = view.findViewById(R.id.view);



            //textView1 = view.findViewById (R.id.text1);
            //changeFonts ();
        }
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);
        //Accion despues de cierto tiempo

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
        Typeface gotham_light = Typeface.createFromAsset(getContext().getAssets(),"fonts/gotham_light.ttf");
        Typeface gotham_bold = Typeface.createFromAsset(getContext().getAssets(),"fonts/gotham_bold.ttf");

        SpannableStringBuilder spanString = new SpannableStringBuilder("OBTÉN UNA \nBEBIDA GRATIS \nal acomular \n6 visitas ");
        spanString.setSpan(new CustomTypefaceSpan("", gotham_light), 0, 9,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        spanString.setSpan(new CustomTypefaceSpan ("", gotham_bold), 10, 24,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        spanString.setSpan(new CustomTypefaceSpan("", gotham_light), 24, 37,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        spanString.setSpan(new CustomTypefaceSpan ("", gotham_bold), 37, 48,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        textView1.setText (spanString);



    }

    @Override
    public void onDestroy() {
        super.onDestroy ();
        cleanMemory ();
    }

    @Override
    public void cleanMemory() {
        mHandler = null;
        typeFace = null;

    }

    @Override
    public void onResume() {
        super.onResume();
        //Analytics
        mTracker.setScreenName("Welcome1Fragment");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

}
