package mx.digitalcoaster.tierra_garat_puntos.fragments.main.settings;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import mx.digitalcoaster.tierra_garat_puntos.R;
import mx.digitalcoaster.tierra_garat_puntos.fragments.main.SettingsFragment;
import mx.digitalcoaster.tierra_garat_puntos.helpers.AnalyticsApplication;
import mx.digitalcoaster.tierra_garat_puntos.preferences.PreferenceUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingPersonalFragment extends Fragment {


    TextView textView2, textView3, textView4, textView5, textView6, textView7, textView8, textView9, textView10;
    View view;
    Button nombreBttn, contraseñaBttn, direccionBttn;
    ImageButton backBttn;
    Typeface typeFace;
    PreferenceUtils utils;

    private Tracker mTracker;        //Analytics






    public SettingPersonalFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Analytics
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();

        // Inflate the layout for this fragment
        super .onCreateView (inflater,container, savedInstanceState);
        inflater = getActivity().getLayoutInflater();
        view = inflater.inflate (R.layout.fragment_setting_personal, container, false);

        if(view != null) {


            backBttn = view.findViewById (R.id.backBttn);
            nombreBttn = view.findViewById (R.id.nombreBttn);
            contraseñaBttn = view.findViewById (R.id.contraseñaBttn);
            direccionBttn = view.findViewById (R.id.direccionBttn);

            changeFonts (view);
            textView4.setText (utils.getName (getContext ()));
            textView10.setText (utils.getAddress (getContext ()));
        }
        return view;
    }

    //Botones de las diferentes opciones
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);

        View.OnClickListener listener = new View.OnClickListener (){

            @Override
            public void onClick(View view) {
                if(backBttn.isPressed ()){
                    Fragment fragment = new SettingsFragment ();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentView, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                } else if(nombreBttn.isPressed ()){
                    Fragment fragment = new SettingPersonalNameFragment ();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentView, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                } else if(contraseñaBttn.isPressed ()){
                    Fragment fragment = new SettingPersonalPasswordFragment ();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentView, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                } else if(direccionBttn.isPressed ()) {
                    Fragment fragment = new SettingPersonalAddressFragment ();
                    FragmentManager fragmentManager = getActivity ().getSupportFragmentManager ();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction ();
                    fragmentTransaction.replace (R.id.fragmentView, fragment);
                    fragmentTransaction.addToBackStack (null);
                    fragmentTransaction.commit ();
                }

            }
        };
        backBttn.setOnClickListener (listener);
        nombreBttn.setOnClickListener (listener);
        contraseñaBttn.setOnClickListener (listener);
        direccionBttn.setOnClickListener (listener);
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
        textView10.setTypeface(typeFace);

        typeFace= Typeface.createFromAsset(getContext().getAssets(),"fonts/courier.ttf");
        textView2.setTypeface(typeFace);
        textView3.setTypeface(typeFace);
        textView5.setTypeface(typeFace);
        textView9.setTypeface(typeFace);
    }

    @Override
    public void onResume() {
        super.onResume();
        //Analytics
        mTracker.setScreenName("SettingPersonalFragment");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
