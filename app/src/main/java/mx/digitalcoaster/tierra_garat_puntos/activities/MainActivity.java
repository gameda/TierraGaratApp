package mx.digitalcoaster.tierra_garat_puntos.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mx.digitalcoaster.tierra_garat_puntos.R;
import mx.digitalcoaster.tierra_garat_puntos.fragments.main.DashboardFragment;
import mx.digitalcoaster.tierra_garat_puntos.fragments.main.LocationFragment;
import mx.digitalcoaster.tierra_garat_puntos.fragments.main.MenuFragment;
import mx.digitalcoaster.tierra_garat_puntos.fragments.main.NotificationsFragment;
import mx.digitalcoaster.tierra_garat_puntos.fragments.main.SettingsFragment;
import mx.digitalcoaster.tierra_garat_puntos.helpers.AnalyticsApplication;
import mx.digitalcoaster.tierra_garat_puntos.interfaces.AsyncTaskInterface;
import mx.digitalcoaster.tierra_garat_puntos.models.User;
import mx.digitalcoaster.tierra_garat_puntos.preferences.PreferenceUtils;


public class MainActivity extends AppCompatActivity implements AsyncTaskInterface//implements BottomNavigationView.OnNavigationItemSelectedListener
{

    public User user;
    private ViewPager containerViewPager;
    public final RxPermissions rxPermissions = new RxPermissions(this); //Varieble para otorgar permisos
    BottomNavigationView navigation;
    boolean bLocal;
    PreferenceUtils utils;
    String name;
    String email;
    String gender;
    String birthday;
    String address;
    String num;
    String city;
    String zip;
    String qrcode;
    String puntos;
    public String menu;
    public String beneficios;

    //Vriables para recibir Jsons
    JSONObject menuJson, beneficiosJson;
    JSONArray arrayBeneficios;

    private Tracker mTracker;        //Analytics


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

        //Analytics
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        setContentView (R.layout.activity_main);
        Intent intent = this.getIntent();
        user = (User) getIntent ().getSerializableExtra ("user");       //Se recibe el objeto

        /*//Bind between View and LayOut
        containerViewPager = findViewById (R.id.containterViewPager);
        ////setup the pager
        setupViewPager (containerViewPager);*/

        final Fragment dashboardFragment = new DashboardFragment ();
        final Fragment notificationFragment = new NotificationsFragment ();
        final Fragment locationFragment = new LocationFragment ();
        final Fragment menuFragment = new MenuFragment ();
        final Fragment settingsFragment = new SettingsFragment ();

        if (savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentView, dashboardFragment).commit();
        }

        navigation = findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(item -> {

            FragmentManager fragmentManager = getSupportFragmentManager();

            if (item.getItemId() == R.id.navigation_dashboard) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentView, dashboardFragment).commit();

            } else if (item.getItemId() == R.id.navigation_notifications) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentView, notificationFragment).commit();

            } else if (item.getItemId() == R.id.navigation_location) {
                    rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION).subscribe(granted -> {
                        if (granted) {
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragmentView, locationFragment).commit();
                        } else {

                        }
                    });
            } else if (item.getItemId() == R.id.navigation_menu) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentView, menuFragment).commit();

            } else if (item.getItemId() == R.id.navigation_settings) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentView, settingsFragment).commit();
            }
            return true;
        });
    }

    /*//Fragments are added to a list
    private void setupViewPager(ViewPager viewPager){
        FragmentAdapter adapter =new FragmentAdapter (getSupportFragmentManager ());
        adapter.addFragment (new DashboardFragment (),"DashboardFragment" );
        adapter.addFragment (new NotificationsFragment (),"NotificationsFragment" );
        adapter.addFragment (new LocationFragment (),"LocationFragment" );
        adapter.addFragment (new MenuFragment (),"MenuFragment" );
        adapter.addFragment (new SettingsFragment (),"SettingsFragment" );
        viewPager.setAdapter (adapter);
        viewPager.getAdapter ().notifyDataSetChanged ();

    }

    public void setViewPager(int iPosition) {
        containerViewPager.setCurrentItem (iPosition);
    }
*/


    @Override
    public void processFinish(String sRespuesta) {
        //Toast.makeText(this,"Mensaje recibido: " + sRespuesta, Toast.LENGTH_LONG).show ();

        if(sRespuesta.contains ("error")){
           // Toast.makeText(this,"Error", Toast.LENGTH_LONG).show ();

        }else if(sRespuesta.isEmpty ()){
           // Toast.makeText(getApplicationContext(),"Error de servidor MN", Toast.LENGTH_LONG).show ();

        }else {
            getUserInformation (sRespuesta);
        }
    }

    @Override
    public void processFacebook(String sRespuesta) {
        //Toast.makeText(this,"Mensaje recibido: " + sRespuesta, Toast.LENGTH_LONG).show ();

        if(sRespuesta.contains ("error")){
            //Toast.makeText(this,"Error", Toast.LENGTH_LONG).show ();

        }else if(sRespuesta.isEmpty()){
            //Toast.makeText(getApplicationContext(),"Error de servidor FB", Toast.LENGTH_LONG).show ();

        }else {
            getUserInformation (sRespuesta);
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
            PreferenceUtils.saveCodigo (qrcode, this);
            PreferenceUtils.savePuntos (Integer.valueOf (puntos), this);
            PreferenceUtils.saveAddress (address + " " + num + ", " + city + ", " + zip,  this);
            PreferenceUtils.saveMenu(menu, this);
            PreferenceUtils.saveBeneficios(beneficios, this);


        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        //Analytics
        mTracker.setScreenName("MainActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
