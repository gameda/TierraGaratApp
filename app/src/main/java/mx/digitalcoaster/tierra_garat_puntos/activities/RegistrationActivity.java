package mx.digitalcoaster.tierra_garat_puntos.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mx.digitalcoaster.tierra_garat_puntos.R;
import mx.digitalcoaster.tierra_garat_puntos.adapters.FragmentAdapter;
import mx.digitalcoaster.tierra_garat_puntos.fragments.main.DashboardFragment;
import mx.digitalcoaster.tierra_garat_puntos.fragments.registration.Register3Fragment;
import mx.digitalcoaster.tierra_garat_puntos.fragments.registration.Register2Fragment;
import mx.digitalcoaster.tierra_garat_puntos.fragments.registration.Register1Fragment;
import mx.digitalcoaster.tierra_garat_puntos.helpers.AnalyticsApplication;
import mx.digitalcoaster.tierra_garat_puntos.interfaces.AsyncTaskInterface;
import mx.digitalcoaster.tierra_garat_puntos.models.User;
import mx.digitalcoaster.tierra_garat_puntos.preferences.PreferenceUtils;

import static android.widget.Toast.makeText;


public class RegistrationActivity extends AppCompatActivity implements AsyncTaskInterface{

    private static final String TAG = "RegistrationActivity";
    public static User user = new User();
    String name, email, gender, birthday, address, num, city, zip, qrcode, puntos, menu, beneficios;
    View splashView;
    JSONObject menuJson, beneficiosJson;
    JSONArray arrayBeneficios;
    TextView sloganTV;


    String sEmail, sName, sId, sBirth, sGender, sLocation;
    private CallbackManager callbackManager;
    LoginButton loginButton;
    private Tracker mTracker;        //Analytics


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

        //Analytics
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        setContentView (R.layout.activity_registration);
        splashView = findViewById (R.id.splash);
        sloganTV = findViewById (R.id.sloganTV);
        changeFonts();


        //Bind between View and LayOut

        final Fragment register = new Register1Fragment ();
        if (savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentView, register).commit();
        }

    }

    public void showSplash(){
        splashView.setVisibility (View.VISIBLE);
        splashView.bringToFront ();
    }



    @Override
    public void processFinish(String sRespuesta) {

        //Toast.makeText(getApplicationContext(),"Mensaje recibido: " + sRespuesta, Toast.LENGTH_LONG).show ();
        if(sRespuesta.contains ("already_registered")){
            showSplash();
            Toast.makeText(getApplicationContext(),"Usuario ya registrado", Toast.LENGTH_LONG).show ();
            Intent intent;
            intent = new Intent (RegistrationActivity.this, AccessActivity.class);
            intent.putExtra("from","RegisterActivity");
            startActivity (intent);

        }

        else if(sRespuesta.contains ("ok")){

            showSplash ();
            getUserInformation (sRespuesta);
            //PreferenceUtils.saveSession (true, this);  //Session manual
            Intent intent;
            PreferenceUtils.saveSession (false, this);  //Session no-facebook
            intent = new Intent (RegistrationActivity.this, MainActivity.class);
            intent.putExtra ("user", user);
            startActivity (intent);
        }

        else if(sRespuesta.isEmpty ()){
            Toast.makeText(getApplicationContext(),"Algo salío mal, intente mas tarde", Toast.LENGTH_LONG).show ();
        }

        else {
            Toast.makeText(getApplicationContext(),"Algo salío mal, intente mas tarde", Toast.LENGTH_LONG).show ();
        }

    }

    @Override
    public void processFacebook(String sRespuesta) {

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

    public void changeFonts(){
        Typeface typeFace = Typeface.createFromAsset (this.getAssets (), "fonts/courier.ttf");
        sloganTV.setTypeface (typeFace);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Analytics
        mTracker.setScreenName("RegistrationActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

}

