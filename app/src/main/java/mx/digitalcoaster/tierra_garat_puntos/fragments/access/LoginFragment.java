package mx.digitalcoaster.tierra_garat_puntos.fragments.access;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import mx.digitalcoaster.tierra_garat_puntos.R;
import mx.digitalcoaster.tierra_garat_puntos.activities.AccessActivity;
import mx.digitalcoaster.tierra_garat_puntos.fragments.access.recuperarContraseña.Recuperar1Fragment;
import mx.digitalcoaster.tierra_garat_puntos.helpers.AnalyticsApplication;
import mx.digitalcoaster.tierra_garat_puntos.interfaces.CleanMemory;
import mx.digitalcoaster.tierra_garat_puntos.preferences.PreferenceUtils;
import mx.digitalcoaster.tierra_garat_puntos.sync.LoadDataLogin;


public class LoginFragment extends Fragment implements CleanMemory {

    Button confirmarBttn, checkBox;
    TextView textView1, textView2, textView3, olvidarTV;
    EditText correoET, contraseñaET;
    String sCorreo, sPassword;
    Typeface typeFace;
    ImageButton backBttn;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.2F);
    private Tracker mTracker;        //Analytics


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super .onCreateView (inflater,container, savedInstanceState);

        //Analytics
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();

        inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate (R.layout.fragment_login, container, false);

        if(view != null){
            confirmarBttn = view.findViewById (R.id.confirmarBttn);
            correoET = view.findViewById (R.id.correoET);
            contraseñaET = view.findViewById (R.id.contraseñaET);
            olvidarTV = view.findViewById(R.id.olvidadoTV);
            backBttn = view.findViewById (R.id.backBttn);

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
                if (confirmarBttn.isPressed()) {
                    view.startAnimation (buttonClick);

                    sCorreo = correoET.getText().toString().trim();
                    sPassword = contraseñaET.getText ().toString().trim();
                    if(sPassword.isEmpty () || sCorreo.isEmpty ())
                        Toast.makeText (getActivity (), "Ingresa tus datos", Toast.LENGTH_SHORT).show ();

                    else{
                        if(conexionInternet()) {
                            PreferenceUtils.savePassword (sPassword, getContext ());
                            //Se mada a .../applogin
                            new LoadDataLogin ((AccessActivity) getActivity ()).execute (sCorreo, sPassword);
                        }
                        else
                            Toast.makeText (getActivity (), "No hay conexión de internet", Toast.LENGTH_SHORT).show();

                    }
                }
                else if(backBttn.isPressed ()){
                    view.startAnimation (buttonClick);
                    getActivity ().onBackPressed();
                }


                if(olvidarTV.isPressed()){
                    view.startAnimation (buttonClick);
                    //Se avanza al siguiente fragment
                    Fragment fragment = new Recuperar1Fragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentView, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }

            }
        };
        confirmarBttn.setOnClickListener(listener);
        olvidarTV.setOnClickListener(listener);
        backBttn.setOnClickListener(listener);


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

    public boolean conexionInternet(){
        ConnectivityManager networkManager = (ConnectivityManager) getActivity ().getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = networkManager.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();
        if(isConnected) {
            return true;
        }else
            return false;
    }

    //Se cambia el font de los textos
    public void changeFonts(View view){

        textView1 = view.findViewById (R.id.text1);
        textView2 = view.findViewById (R.id.text2);
        textView3 = view.findViewById (R.id.text3);

        typeFace= Typeface.createFromAsset(getContext().getAssets(),"fonts/gotham_light.ttf");
        confirmarBttn.setTypeface (typeFace);
        textView2.setTypeface(typeFace);
        olvidarTV.setTypeface(typeFace);

        typeFace= Typeface.createFromAsset(getContext().getAssets(),"fonts/gotham_bold.ttf");
        textView1.setTypeface(typeFace);


        typeFace= Typeface.createFromAsset(getContext().getAssets(),"fonts/courier.ttf");
        correoET.setTypeface(typeFace);
        contraseñaET.setTypeface (typeFace);




    }

    @Override
    public void onDestroy() {
        super.onDestroy ();
    }

    @Override
    public void cleanMemory() {
        confirmarBttn.setOnClickListener(null);
        confirmarBttn = null;
        textView3 = null;
        textView2 = null;
        textView1 = null;
        correoET = null;
        contraseñaET = null;
        sCorreo = null;

    }
    @Override
    public void onResume() {
        super.onResume();
        //Analytics
        mTracker.setScreenName("LoginFragment");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }



}
