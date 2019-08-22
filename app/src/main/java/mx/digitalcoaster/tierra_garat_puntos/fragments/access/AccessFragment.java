package mx.digitalcoaster.tierra_garat_puntos.fragments.access;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import mx.digitalcoaster.tierra_garat_puntos.R;
import mx.digitalcoaster.tierra_garat_puntos.activities.AccessActivity;
import mx.digitalcoaster.tierra_garat_puntos.helpers.AnalyticsApplication;
import mx.digitalcoaster.tierra_garat_puntos.interfaces.CleanMemory;
import mx.digitalcoaster.tierra_garat_puntos.preferences.PreferenceUtils;
import mx.digitalcoaster.tierra_garat_puntos.sync.LoadDataLoginFacebook;


public class AccessFragment extends Fragment implements CleanMemory {

    Button manualBttn, facebookBttn;
    String sName, sEmail="", sId, sBirth, sGender, sLocation;
    Typeface typeFace;
    TextView textView1, textView2, textView3;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.2F);
    private Tracker mTracker;        //Analytics


    //Faceboook: Variables
    private CallbackManager callbackManager;
    LoginButton loginButton;


    public AccessFragment() {
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
        View view = inflater.inflate (R.layout.fragment_access, container, false);

        if(view != null){
            manualBttn = view.findViewById (R.id.manualBttn);
            facebookBttn = view.findViewById (R.id.facebookBttn);
            changeFonts (view);

            /*callbackManager = CallbackManager.Factory.create();
            loginButton = view.findViewById (R.id.login_button);//Boton de facebook en el .xml
            loginButton.setReadPermissions("email");
            loginButton.setFragment(this);*/
            facebookOperation(view);

        }
        return view;
    }

    //Inicia Facebook

    //Facebook : Metodo OBLIGATORIO que hace el bind
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    //Acciones para logearse con Facebook y obtener datos
    public void facebookOperation(View view){
        callbackManager = CallbackManager.Factory.create();
        loginButton = view.findViewById (R.id.login_button);//Boton de facebook en el .xml
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile",
                "email",
                "user_birthday",
                "user_gender",
                "user_location"));
        loginButton.setFragment(this);                                             //En caso de usar el API en un fragmento

        //En caso de Login exitoso
        //Manejo de eventos
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback < LoginResult > () {


            @Override
            public void onSuccess(LoginResult loginResult) {
                //Toast.makeText (getActivity (), "Entro", Toast.LENGTH_SHORT).show ();


                //Entra y hace un request, que regresa un JSONObject con los datos solicitados
                GraphRequest request = GraphRequest.newMeRequest (loginResult.getAccessToken(), (object, response) -> {

                    getFacebookData(object); //Metodo que parsea el JSONObject

                    //Toast.makeText (getActivity (), object.toString (), Toast.LENGTH_SHORT).show ();
                    if(!sEmail.isEmpty ()) { //Verica que haya regresado un email

                        PreferenceUtils.savePassword (sId, getContext ());
                        //Se mada a .../facebooklogin
                        new LoadDataLoginFacebook ((AccessActivity) getActivity ()).execute (sName, sId, sEmail, sBirth, sGender, sLocation);
                    }
                    else{
                        Toast toast = Toast.makeText (getActivity (), "Tu cuenta de Facebook no tiene registrado un correo electronico, ingresa manualmente",
                                Toast.LENGTH_LONG);
                        //toast.setGravity (Gravity.CENTER,0,0);
                        toast.show ();
                        LoginManager.getInstance().logOut();
                        //LogoutFacebook y mensaje de que su facebook no cuenta con email
                    }
                });
                Bundle parameters = new Bundle ();
                parameters .putString ("fields", "id, name, email, gender, birthday, location");
                request.setParameters (parameters);
                request.executeAsync();



            }
            //En caso de cancelar el proceso de Login
            @Override
            public void onCancel() {
                //Toast.makeText (getActivity (), "Se cancelo", Toast.LENGTH_SHORT).show ();
            }
            //En caso de Login fallado
            @Override
            public void onError(FacebookException error) {
                //Toast.makeText (getActivity (), "Error", Toast.LENGTH_SHORT).show ();


            }
        });
    }

    public void onClick(View v) {
        if (v == facebookBttn) {
            LoginManager.getInstance().logInWithReadPermissions(this,
                    (Arrays.asList(
                            "public_profile",
                            "email",
                            "user_birthday",
                            "user_gender",
                            "user_location"))
            );   //Se declaran los permisos a solicitar

        }
    }

    //Metodo que parcea el Json que te devuelve facebook
    public void getFacebookData(JSONObject object) {

        try{
            sEmail = object.getString ("email");
            sId = object.getString ("id");
            sName = object.getString ("name");
            sBirth = object.getString ("birthday");
            sGender = object.getString ("gender");
            sLocation = object.getJSONObject ("location").getString ("name");
            //Toast.makeText (getActivity (), object.toString (), Toast.LENGTH_SHORT).show ();


        } catch (JSONException e){
            e.printStackTrace ();
        }
    }



    //Termina facebook



    @Override
    public void onViewCreated   (View view,  Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);

        View.OnClickListener listener = new View.OnClickListener (){

            @Override
            public void onClick(View view) {
                if (manualBttn.isPressed()) {
                    view.startAnimation (buttonClick);
                    //Se avanza al siguiente fragment
                    Fragment fragment = new ManualFragment ();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentView, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }

            }
        };
        manualBttn.setOnClickListener(listener);

        facebookBttn.setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                v.startAnimation (buttonClick);
                if (v == facebookBttn) {
                    loginButton.performClick();
                }
            }
        });


    }


    //Se cambia el font de los textos
    public void changeFonts(View view){
        textView1 = view.findViewById (R.id.text1);
        textView2 = view.findViewById (R.id.text2);
        textView3 = view.findViewById (R.id.text3);

        typeFace= Typeface.createFromAsset(getContext().getAssets(),"fonts/gotham_light.ttf");
        textView3.setTypeface(typeFace);
        textView1.setTypeface(typeFace);
        manualBttn.setTypeface (typeFace);
        facebookBttn.setTypeface (typeFace);

        typeFace= Typeface.createFromAsset(getContext().getAssets(),"fonts/gotham_bold.ttf");
        textView2.setTypeface(typeFace);
    }

    @Override
    public void onDestroy() {
        super.onDestroy ();
        cleanMemory ();
    }

    @Override
    public void cleanMemory() {
        textView1 = null;
        textView2 = null;
        textView3 = null;
        manualBttn.setOnClickListener(null);
        facebookBttn.setOnClickListener(null);
        manualBttn = null;
        facebookBttn = null;
        sName = null;
        sEmail = null;
        sId = null;
        sBirth = null;
        sGender = null;
        sLocation = null;
        callbackManager = null;
        loginButton = null;
        typeFace = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Analytics
        mTracker.setScreenName("AccessFragment");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

}
