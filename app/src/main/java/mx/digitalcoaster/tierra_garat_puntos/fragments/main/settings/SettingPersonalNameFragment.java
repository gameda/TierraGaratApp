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

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingPersonalNameFragment extends Fragment {

    Button saveBttn;
    ImageButton backBttn;
    EditText editText;
    View view;
    TextView textView, textView2;
    private Tracker mTracker;        //Analytics

    public SettingPersonalNameFragment() {
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
        view =  inflater.inflate (R.layout.fragment_setting_personal_name, container, false);

        if(view != null) {
            backBttn = view.findViewById (R.id.backBttn);
            saveBttn = view.findViewById (R.id.saveBttn);
            textView = view.findViewById (R.id.text2);
            textView2 = view.findViewById (R.id.text3);
            editText = view.findViewById (R.id.text4);
            changeFonts ();

        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);
        View.OnClickListener listener = new View.OnClickListener (){

            @Override
            public void onClick(View view) {
                if(backBttn.isPressed ()){
                    Fragment fragment = new SettingPersonalFragment ();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentView, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        };
        backBttn.setOnClickListener (listener);
    }

    //Se cambia el font de los textos
    public void changeFonts() {

        Typeface typeFace;

        typeFace = Typeface.createFromAsset (getContext ().getAssets (), "fonts/gotham_light.ttf");
        textView2.setTypeface (typeFace);
        saveBttn.setTypeface (typeFace);


        typeFace = Typeface.createFromAsset (getContext ().getAssets (), "fonts/courier.ttf");
        textView.setTypeface (typeFace);
        editText.setTypeface (typeFace);
    }



    @Override
    public void onResume() {
        super.onResume();
        //Analytics
        mTracker.setScreenName("SettingPersonalNameFragment");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
