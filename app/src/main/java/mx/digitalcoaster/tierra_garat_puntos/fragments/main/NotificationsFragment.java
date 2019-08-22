package mx.digitalcoaster.tierra_garat_puntos.fragments.main;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mx.digitalcoaster.tierra_garat_puntos.R;
import mx.digitalcoaster.tierra_garat_puntos.activities.MainActivity;
import mx.digitalcoaster.tierra_garat_puntos.adapters.ListViewBeneficiosAdapter;
import mx.digitalcoaster.tierra_garat_puntos.adapters.ListViewNovedadAdapter;
import mx.digitalcoaster.tierra_garat_puntos.helpers.AnalyticsApplication;
import mx.digitalcoaster.tierra_garat_puntos.models.Beneficio;
import mx.digitalcoaster.tierra_garat_puntos.models.Sucursal;
import mx.digitalcoaster.tierra_garat_puntos.preferences.PreferenceUtils;

import static com.facebook.FacebookSdk.getApplicationContext;


public class NotificationsFragment extends Fragment {


    RxPermissions rxPermissions;
    int iPuntos;
    String sName;
    PreferenceUtils utils = new PreferenceUtils ();
    TextView nombreTV, puntosTV, fechaTV, tituloTV, titulo2TV, textTV, text1TV, anuncio1TV, anuncio2TV;
    ListView beneficiosLV, novedadesLV;
    View dashView;


    private View view;
    Typeface typeFace;

    ImageView codigoIV;
    List<Beneficio> beneficios;
    View qrView;
    Button regresaBttn;
    Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down2);
    Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up2);
    AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.2F);
    private Tracker mTracker;        //Analytics


    public NotificationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Analytics
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();

        // Inflate the layout for this fragment
        super.onCreateView (inflater, container, savedInstanceState);
        inflater = getActivity ().getLayoutInflater ();
        view = inflater.inflate (R.layout.fragment_notifications, container, false);

        if (view != null) {


            dashView = view.findViewById(R.id.dashView);
            tituloTV = view.findViewById (R.id.tituloTV);
            //textTV = view.findViewById (R.id.textTV);
            beneficiosLV = view.findViewById (R.id.beneficiosLV);
            novedadesLV = view.findViewById(R.id.novedadesLV);
            titulo2TV = view.findViewById(R.id.tituloTV2);
            anuncio1TV = view.findViewById(R.id.anuncioTV);
            anuncio2TV = view.findViewById(R.id.anuncio2TV);



            qrView = view.findViewById(R.id.qrView);
            regresaBttn = view.findViewById(R.id.regresaBttn);
            codigoIV = view.findViewById(R.id.codigoIV);
            text1TV = view.findViewById(R.id.text1);
            changeFonts (view);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);

        iPuntos = utils.getPuntos (getContext ());
        sName = utils.getName (getContext ());

//        nombreTV.setText (sName);
//        puntosTV.setText (String.valueOf (iPuntos));
//        SimpleDateFormat df = new SimpleDateFormat ("dd-MM-yyyy HH:mm");

        //Obtiene la lista de benficios
        beneficios = getDataForListView ();
        //Adapter de beneficos
        final ListViewBeneficiosAdapter adapter = new ListViewBeneficiosAdapter (getActivity (), R.layout.row_beneficio, beneficios);
        beneficiosLV.setAdapter (adapter);

        //Adapter de novedades
        final ListViewNovedadAdapter adapter2 = new ListViewNovedadAdapter(getActivity (), R.layout.row_novedad, beneficios);
        novedadesLV.setAdapter (adapter2);


        //Hacer click en algun beneficio/promoción
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Beneficio beneficio = adapter.getItem (position);
                if(beneficio.getsType().equals("beneficio")){
                    generateQR(beneficio.getsCodigo());
                    showQR();
                }
                else {
                    changeFragment();

                }

            }
        };
        beneficiosLV.setOnItemClickListener (itemListener);
        novedadesLV.setOnItemClickListener(itemListener);


        View.OnClickListener listener = new View.OnClickListener (){

            @Override
            public void onClick(View view) {
                if(regresaBttn.isPressed ()){
                    view.startAnimation (buttonClick);
                    hideQR ();
                }
            }
        };
        regresaBttn.setOnClickListener(listener);



        //Disable back button
        view.setFocusableInTouchMode (true);
        view.requestFocus ();
        view.setOnKeyListener (new View.OnKeyListener () {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction () == KeyEvent.ACTION_UP) {
                    getFragmentManager ().popBackStack (null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                    return true;
                }
                return false;
            }
        });
    }

    public void changeFragment(){
        RxPermissions rxPermissions = ((MainActivity)getActivity()).rxPermissions;
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION).subscribe(granted -> {
            if (granted) {
                getFragmentManager().beginTransaction().replace(R.id.fragmentView, new LocationFragment()).commit();
                ((BottomNavigationView)getActivity().findViewById(R.id.navigation)).setSelectedItemId(R.id.navigation_location);

            } else {

            }
        });
    }


    //Se llena la lista de beneficios
    public List<Beneficio> getDataForListView() {

        List<Beneficio> listaBeneficio = new ArrayList<Beneficio> ();
        Beneficio beneficio = null;

        try {
            JSONArray js = new JSONArray(utils.getBeneficios(getContext()));

            for(int i = 0; i < js.length(); i++){
                JSONObject obj = new JSONObject(js.getString(i));
                beneficio = new Beneficio(
                        obj.getString("title"),
                        obj.getString("desc"),
                        obj.getString("type"),
                        obj.getString("code"),
                        obj.getInt("used"));
                listaBeneficio.add(beneficio);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }



       /* beneficio =  new Beneficio ("NUEVO NARANJO HUACAL",
                "La nueva bebida ha llegado \n a nuestra tierra. ¡Ven y disfrutalo!",
                false);
        listaBeneficio.add(beneficio);

        beneficio =  new Beneficio ("2x1 EN BEBIDAS",
                "Da click aquí para obtener promoción",
                true, "codigoBeneficio");
        listaBeneficio.add(beneficio);*/



        return listaBeneficio;

    }

    public void showQR(){
        // Start animation
        qrView.bringToFront();
        qrView.setVisibility (View.VISIBLE);
        qrView.startAnimation(slide_down);
        beneficiosLV.setClickable(false);
        beneficiosLV.setEnabled(false);

       /* final Handler handler = new Handler();
        Runnable runnable = new Runnable() {

            public void run() {
                sumaBttn.setVisibility (View.GONE);
            }
        };
        handler.postDelayed(runnable, 300); //for initial delay..*/


    }

    public void hideQR(){
        // Start animation
        qrView.startAnimation(slide_up);
        qrView.setVisibility (View.INVISIBLE);
        beneficiosLV.setClickable(true);
        beneficiosLV.setEnabled(true);

    }

    //Draw QRcode
    public void generateQR (String codigo){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter ();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode (codigo, BarcodeFormat.QR_CODE,
                    800, 800);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder ();
            Bitmap bitmapQR = barcodeEncoder.createBitmap (bitMatrix);
            codigoIV.setImageBitmap (bitmapQR);

        }catch (WriterException e){
            e.printStackTrace ();
        }
    }

    public void changeFonts(View view){

        typeFace = Typeface.createFromAsset (getContext ().getAssets (), "fonts/gotham_light.ttf");
        //nombreTV.setTypeface (typeFace);
        //fechaTV.setTypeface (typeFace);
        regresaBttn.setTypeface(typeFace);

        typeFace = Typeface.createFromAsset (getContext ().getAssets (), "fonts/gotham_bold.ttf");
        text1TV.setTypeface(typeFace);

        typeFace= Typeface.createFromAsset(getContext().getAssets(),"fonts/courier.ttf");
        anuncio1TV.setTypeface (typeFace);
        anuncio2TV.setTypeface (typeFace);

        typeFace= Typeface.createFromAsset(getContext().getAssets(),"fonts/courier_bold.ttf");
        tituloTV.setTypeface (typeFace);
        titulo2TV.setTypeface (typeFace);

    }

    @Override
    public void onResume() {
        super.onResume();
        //Analytics
        mTracker.setScreenName("NotificationsFragment");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }




}
