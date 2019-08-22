package mx.digitalcoaster.tierra_garat_puntos.fragments.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import mx.digitalcoaster.tierra_garat_puntos.R;
import mx.digitalcoaster.tierra_garat_puntos.activities.AccessActivity;
import mx.digitalcoaster.tierra_garat_puntos.activities.MainActivity;
import mx.digitalcoaster.tierra_garat_puntos.activities.WelcomeActivity;
import mx.digitalcoaster.tierra_garat_puntos.fragments.main.settings.SettingNotificacionesFragment;
import mx.digitalcoaster.tierra_garat_puntos.fragments.main.settings.SettingPermisosFragment;
import mx.digitalcoaster.tierra_garat_puntos.fragments.main.settings.SettingPersonalFragment;
import mx.digitalcoaster.tierra_garat_puntos.helpers.AnalyticsApplication;
import mx.digitalcoaster.tierra_garat_puntos.preferences.PreferenceUtils;


public class SettingsFragment extends Fragment {

    TextView textView2, textView3, textView4, textView5, textView6, textView7, textView8, textView9, textView10;
    View view;
    Button notiBttn, personalBttn, permiBttn, salirBttn;
    Typeface typeFace;
    PreferenceUtils utils;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.2F);
    private Tracker mTracker;        //Analytics



    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Analytics
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();

        // Inflate the layout for this fragment
        //FacebookSdk.sdkInitialize(getContext ());
        super .onCreateView (inflater,container, savedInstanceState);
        inflater = getActivity().getLayoutInflater();
        view = inflater.inflate (R.layout.fragment_settings, container, false);

        if(view != null) {

            notiBttn = view.findViewById (R.id.notificaBttn);
            personalBttn = view.findViewById (R.id.personalBttn);
            permiBttn = view.findViewById (R.id.permisoBttn);
            salirBttn = view.findViewById (R.id.salirBttn);


            changeFonts (view);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);

        View.OnClickListener listener = new View.OnClickListener (){

            @Override
            public void onClick(View view) {
                if (notiBttn.isPressed()){
                    view.startAnimation (buttonClick);

                    Fragment fragment = new SettingNotificacionesFragment ();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentView, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                } else if(personalBttn.isPressed ()){
                    view.startAnimation (buttonClick);

                    Fragment fragment = new SettingPersonalFragment ();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentView, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }else if(permiBttn.isPressed ()){
                    view.startAnimation (buttonClick);

                    Fragment fragment = new SettingPermisosFragment ();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentView, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }else if(salirBttn.isPressed ()){
                    view.startAnimation (buttonClick);

                    if(utils.getSession (getContext ())){
                       LoginManager.getInstance().logOut();
                       goWelcomeScreen ();

                   }else
                       goWelcomeScreen ();
                }
            }
        };
        //personalBttn.setOnClickListener(listener);
        //permiBttn.setOnClickListener (listener);
        //notiBttn.setOnClickListener (listener);
        salirBttn.setOnClickListener (listener);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                    return true;
                }
                return false;
            }
        });

    }

    private void goWelcomeScreen() {
        PreferenceUtils.deleteData (getContext ());
        Intent intent = new Intent(getActivity (), AccessActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    //Se cambia el font de los textos
    public void changeFonts(View view){

        textView2 = view.findViewById (R.id.text2);
        textView3 = view.findViewById (R.id.text3);
        textView4 = view.findViewById (R.id.text4);
        textView5 = view.findViewById (R.id.text5);
        textView6 = view.findViewById (R.id.text6);
        textView7 = view.findViewById (R.id.text7);
        textView8 = view.findViewById (R.id.text8);
        textView9 = view.findViewById (R.id.text9);
        textView10 = view.findViewById (R.id.text10);

        typeFace = Typeface.createFromAsset (getContext ().getAssets (), "fonts/gotham_light.ttf");
        textView4.setTypeface(typeFace);
        textView6.setTypeface(typeFace);
        textView8.setTypeface(typeFace);
        salirBttn.setTypeface (typeFace);
        //textView10.setTypeface(typeFace);

        typeFace= Typeface.createFromAsset(getContext().getAssets(),"fonts/courier.ttf");
        textView2.setTypeface(typeFace);
        textView3.setTypeface(typeFace);
        textView5.setTypeface(typeFace);
        textView7.setTypeface(typeFace);
        //textView9.setTypeface(typeFace);


    }

    @Override
    public void onResume() {
        super.onResume();
        //Analytics
        mTracker.setScreenName("SettingsFragment");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }




}



