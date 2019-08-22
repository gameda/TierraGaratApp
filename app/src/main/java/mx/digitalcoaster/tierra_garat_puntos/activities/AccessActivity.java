package mx.digitalcoaster.tierra_garat_puntos.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mx.digitalcoaster.tierra_garat_puntos.R;
import mx.digitalcoaster.tierra_garat_puntos.fragments.access.AccessFragment;
import mx.digitalcoaster.tierra_garat_puntos.fragments.access.LoginFragment;
import mx.digitalcoaster.tierra_garat_puntos.helpers.AnalyticsApplication;
import mx.digitalcoaster.tierra_garat_puntos.interfaces.AsyncTaskInterface;
import mx.digitalcoaster.tierra_garat_puntos.interfaces.CleanMemory;
import mx.digitalcoaster.tierra_garat_puntos.models.User;
import mx.digitalcoaster.tierra_garat_puntos.preferences.PreferenceUtils;
import mx.digitalcoaster.tierra_garat_puntos.sync.LoadDataLogin;
import mx.digitalcoaster.tierra_garat_puntos.sync.LoadDataLoginFacebook;

public class AccessActivity extends AppCompatActivity implements AsyncTaskInterface, CleanMemory {


    private static final String TAG = "AccessActivity";
    User user = new User ();
    FragmentTransaction fragmentTransaction = null;
    ConnectivityManager networkManager;
    String name, email, gender, birthday, address, num, city, zip, qrcode, puntos, menu, beneficios;
    JSONObject menuJson, beneficiosJson;
    JSONArray arrayBeneficios;
    View splashView;
    TextView sloganTV;

    private Tracker mTracker;        //Analytics

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

        //Analytics
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();


        setContentView (R.layout.activity_access);

        splashView = findViewById (R.id.splash);
        sloganTV = findViewById (R.id.sloganTV);
        changeFonts();

        //Bind between View and LayOut
        verifyLogin ();
        final Fragment access = new AccessFragment ();
        if (savedInstanceState == null) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentView, access).commit();
        }
        verifyIntent();
    }

    public void verifyIntent(){
        Intent intent;
        intent = getIntent();
        String from = intent.getStringExtra("from");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if(intent.hasExtra("from")){
            final Fragment access = new LoginFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.fragmentView, access).commit();

        }
    }

    //Verifica que ya se este logeado anteriormente
    public void verifyLogin(){

        //Verifica que haya conexion de internet
        networkManager = (ConnectivityManager) this.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = networkManager.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        if (PreferenceUtils.getEmail (this) != null) { //Verifica que hay una session abierta.
            showSplash ();
            if(isConnected){

                //Checa el tipo de session
                if(PreferenceUtils.getSession (this))
                    //Facebook session
                    new LoadDataLoginFacebook (this).execute(
                            PreferenceUtils.getName (this),
                            PreferenceUtils.getEmail (this),
                            PreferenceUtils.getPassword (this));
                 else
                    //Manual session
                    new LoadDataLogin (this).execute (
                            PreferenceUtils.getEmail (this),
                            PreferenceUtils.getPassword (this));
            }
            else{ //Si no hay internet pasa directo a la session abierta.
                Intent intent = new Intent (AccessActivity.this, MainActivity.class);
                startActivity (intent);
            }
        }
    }


    @Override
    public void processFinish(String sRespuesta) {
        //Toast.makeText(getApplicationContext(),"Mensaje recibido: " + sRespuesta, Toast.LENGTH_LONG).show ();

        if(sRespuesta.isEmpty()){
            Toast.makeText(getApplicationContext(),"Datos incorrectos", Toast.LENGTH_LONG).show ();

        }
        if(sRespuesta.contains ("no-user")){
            Toast.makeText(getApplicationContext(),"Correo incorrecto", Toast.LENGTH_LONG).show ();

        } else if(sRespuesta.contains ("incorrect-info")){
            Toast.makeText(getApplicationContext(),"Contraseña incorreta", Toast.LENGTH_LONG).show ();
        }
        else {
            showSplash ();
            getUserInformation (sRespuesta);
            PreferenceUtils.saveSession (false, this);  //Session no-facebook
            Intent intent;
            intent = new Intent (AccessActivity.this, MainActivity.class);
            intent.putExtra ("user", user);
            startActivity (intent);
        }

    }

    @Override
    public void processFacebook(String sRespuesta) {
         //Toast.makeText(getApplicationContext(),"Mensaje recibido: " + sRespuesta, Toast.LENGTH_LONG).show ();

        if(sRespuesta.contains ("already_registered")){
            Toast.makeText(getApplicationContext(),"Correo registrado anteriormente", Toast.LENGTH_LONG).show ();
            LoginManager.getInstance().logOut();

        }else {
            showSplash ();
            getUserInformation (sRespuesta);
            PreferenceUtils.saveSession (true, this);  //Session facebook
            Intent intent;
            intent = new Intent (AccessActivity.this, MainActivity.class);
            intent.putExtra ("user", user);
            startActivity (intent);

        }
    }


    //Funcion para parcear el JSON
    public void getUserInformation(String sInformation) {
        try {

            JSONObject jsonInfo = new JSONObject (sInformation);
            name = jsonInfo.getJSONObject("user").getString("name");
            email = jsonInfo.getJSONObject("user").getString("email");
            gender = jsonInfo.getJSONObject("user").getString("gender");
            birthday = jsonInfo.getJSONObject("user").getString("birthday");
            address = jsonInfo.getJSONObject("user").getString("address_street");
            num = jsonInfo.getJSONObject("user").getString("address_number");
            city = jsonInfo.getJSONObject("user").getString("address_city");
            zip = jsonInfo.getJSONObject("user").getString("address_zip");
            qrcode = jsonInfo.getJSONObject("user").getString("qrcode");
            puntos = jsonInfo.getJSONObject ("user").getString ("points");

            menuJson = jsonInfo.getJSONObject("menu");
            arrayBeneficios = jsonInfo.getJSONObject("user").getJSONArray("benefits");
            menu = menuJson.toString();
            beneficios = arrayBeneficios.toString();


            user.setsName (name);
            user.setsEmail (email);
            user.setsSex (gender);
            user.setsBirth (birthday);
            user.setsAddress (address);
            user.setiNumE (Integer.valueOf (num));
            user.setsCity (city);
            user.setiCP (Integer.valueOf (zip));
            user.setsCodigoQR (qrcode);
            user.setPuntos (Integer.valueOf (puntos));

            //Se guardan los datos en memoria
            PreferenceUtils.saveEmail (email, this);
            PreferenceUtils.saveName (name, this);
            PreferenceUtils.saveCodigo (qrcode,this);
            PreferenceUtils.savePuntos (Integer.valueOf (puntos), this);
            PreferenceUtils.saveAddress (address + " " + num + ", " + city + ", " + zip, this);
            PreferenceUtils.saveMenu(menu, this);
            PreferenceUtils.saveBeneficios(beneficios, this);



        }catch (JSONException e) {
                e.printStackTrace();
        }
    }

    public void showSplash(){
        splashView.setVisibility (View.VISIBLE);
        splashView.bringToFront ();
    }

    //Se cambian las fuentes
    public void changeFonts(){
        Typeface typeFace = Typeface.createFromAsset (this.getAssets (), "fonts/courier.ttf");
        sloganTV.setTypeface (typeFace);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy ();
    }

    @Override
    public void cleanMemory() {
        fragmentTransaction = null;
        networkManager = null;
        name = null;
        email = null;
        gender = null;
        birthday = null;
        address = null;
        num = null;
        city = null;
        zip = null;
        qrcode = null;
        puntos = null;
        splashView = null;


    }

    @Override
    protected void onResume() {
        super.onResume();
        //Analytics
        mTracker.setScreenName("AccessActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    /*@Override
    public void onBackPressed() {
        super.onBackPressed ();
        if (getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }*/
}
