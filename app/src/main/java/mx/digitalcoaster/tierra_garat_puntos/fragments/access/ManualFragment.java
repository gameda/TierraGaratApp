package mx.digitalcoaster.tierra_garat_puntos.fragments.access;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
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

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import mx.digitalcoaster.tierra_garat_puntos.R;
import mx.digitalcoaster.tierra_garat_puntos.activities.AccessActivity;
import mx.digitalcoaster.tierra_garat_puntos.activities.RegistrationActivity;
import mx.digitalcoaster.tierra_garat_puntos.fragments.registration.Register2Fragment;
import mx.digitalcoaster.tierra_garat_puntos.helpers.AnalyticsApplication;


public class ManualFragment extends Fragment {

    Button signinBttn, loginBttn;
    Typeface typeFace;
    TextView textView1, textView2;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.2F);
    private Tracker mTracker;        //Analytics


    public ManualFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        //Analytics
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();

        // Inflate the layout for this fragment
        super .onCreateView (inflater,container, savedInstanceState);
        inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate (R.layout.fragment_manual, container, false);
        if(view != null){
            signinBttn = view.findViewById (R.id.signinBttn);
            loginBttn = view.findViewById (R.id.loginBttn);
            changeFonts (view);

        }
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);

        View.OnClickListener listener = new View.OnClickListener (){

            @Override
            public void onClick(View view) {
                if (loginBttn.isPressed()) {
                    view.startAnimation (buttonClick);

                    //Se avanza al siguiente fragment
                    Fragment fragment = new LoginFragment ();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentView, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else if(signinBttn.isPressed ()){
                    view.startAnimation (buttonClick);
                    Intent intent;
                    intent = new Intent (getActivity (), RegistrationActivity.class);
                    startActivity (intent);

                }
            }
        };
        signinBttn.setOnClickListener(listener);
        loginBttn.setOnClickListener(listener);



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

    //Se cambia el font de los textos
    public void changeFonts(View view){

        textView1 = view.findViewById (R.id.text1);
        textView2 = view.findViewById (R.id.text2);

        typeFace= Typeface.createFromAsset(getContext().getAssets(),"fonts/gotham_light.ttf");
        signinBttn.setTypeface(typeFace);
        loginBttn.setTypeface(typeFace);
        textView1.setTypeface(typeFace);

        typeFace= Typeface.createFromAsset(getContext().getAssets(),"fonts/gotham_bold.ttf");
        textView2.setTypeface(typeFace);

    }

    @Override
    public void onResume() {
        super.onResume();
        //Analytics
        mTracker.setScreenName("ManualFragment");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }



}
