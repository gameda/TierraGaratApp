package mx.digitalcoaster.tierra_garat_puntos.fragments.main;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.lang.UScript;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mx.digitalcoaster.tierra_garat_puntos.R;
import mx.digitalcoaster.tierra_garat_puntos.activities.MainActivity;
import mx.digitalcoaster.tierra_garat_puntos.helpers.AnalyticsApplication;
import mx.digitalcoaster.tierra_garat_puntos.models.Beneficio;
import mx.digitalcoaster.tierra_garat_puntos.models.User;
import mx.digitalcoaster.tierra_garat_puntos.preferences.CustomTypefaceSpan;
import mx.digitalcoaster.tierra_garat_puntos.preferences.PreferenceUtils;
import mx.digitalcoaster.tierra_garat_puntos.sync.LoadDataLogin;
import mx.digitalcoaster.tierra_garat_puntos.sync.LoadDataLoginFacebook;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    User user;
    String sName;
    int iPuntos;
    TextView faltantesTV, nombreTV, saludoTV,deslizaTV, parrafo1, parrafo2;
    ImageView[] arrayIV = new ImageView[7];
    TextView[] arrayTV = new TextView[7];
    Button sumaBttn, regresarBttn;
    View qrView;
    SwipeRefreshLayout swipeView;
    RelativeLayout emptyRL, mainView;
    Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
    Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.2F);
    private Tracker mTracker;        //Analytics


    //int random = (int)(Math.random() * 6 + 1);


    //Button closeBttn;
    TextView textView1, textView2, visitasTV;
    ImageView codigoIV;

    Typeface typeFace;
    PreferenceUtils utils = new PreferenceUtils ();
    Date c = Calendar.getInstance().getTime();


    public DashboardFragment() {
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
        View view = inflater.inflate (R.layout.fragment_dashboard, container, false);

        if(view != null){

            arrayIV[1] = view.findViewById(R.id.vaso1);
            arrayIV[2] = view.findViewById(R.id.vaso2);
            arrayIV[3] = view.findViewById(R.id.vaso3);
            arrayIV[4] = view.findViewById(R.id.vaso4);
            arrayIV[5] = view.findViewById(R.id.vaso5);
            arrayIV[6] = view.findViewById(R.id.vaso6);

            arrayTV[1] = view.findViewById(R.id.unoTV);
            arrayTV[2] = view.findViewById(R.id.dosTV);
            arrayTV[3] = view.findViewById(R.id.tresTV);
            arrayTV[4] = view.findViewById(R.id.cuatroTV);
            arrayTV[5] = view.findViewById(R.id.cincoTV);
            arrayTV[6] = view.findViewById(R.id.seisTV);

            deslizaTV = view.findViewById (R.id.deslizaTV);
            saludoTV = view.findViewById (R.id.saludoTV);
            nombreTV = view.findViewById (R.id.nombreTV);
            faltantesTV = view.findViewById (R.id.faltantesTV);
            sumaBttn = view.findViewById (R.id.sumaBttn);

            parrafo1 = view.findViewById (R.id.parrafo1);
            parrafo2 = view.findViewById (R.id.parrafo2);
            swipeView = view.findViewById (R.id.sweepLayout);

            iPuntos = utils.getPuntos (getContext ());
            imageChange();

            mainView = view.findViewById (R.id.mainView);
            emptyRL = view.findViewById (R.id.emptyRL);
            qrView = view.findViewById (R.id.qrView);
            textView1 = view.findViewById (R.id.text1);
            textView2 = view.findViewById (R.id.text2);
            visitasTV = view.findViewById (R.id.puntos2TV);
            codigoIV = view.findViewById (R.id.codigoIV);
            regresarBttn = view.findViewById (R.id.regresaBttn);



            timeComparation();
            changeFonts (view);
            generateQR(utils.getCodigo(getContext()));
            //generateQR (PreferenceUtils.getCodigo (getContext ()));

        }
        return view;
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);


        //Obtiene datos guardados
        user = ((MainActivity)getActivity ()).user;

        iPuntos = utils.getPuntos (getContext ());
        //iPuntos = (int)(Math.random() * 6 + 1);
        sName = utils.getName (getContext ());

        nombreTV.setText(sName);

        swipeView.setColorSchemeResources (R.color.cafe, R.color.cafe_claro, R.color.cafe2, R.color.marron);
        swipeView.setOnRefreshListener (new SwipeRefreshLayout.OnRefreshListener () {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing (true);
                (new Handler ()).postDelayed (new Runnable () {
                    @Override
                    public void run() {
                        swipeView.setRefreshing (false);
                        restaura();
                        resfreshInfo ();
                        imageChange();

                    }
                },500);
            }
        });

        //Current date
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String sDateFormat = df.format(c);
        timeComparation ();             //Se compara las fechas



        View.OnClickListener listener = new View.OnClickListener (){

            @Override
            public void onClick(View view) {
                if(sumaBttn.isPressed ()){
                    view.startAnimation (buttonClick);
                    showQR ();

                }

                else if( regresarBttn.isPressed ()){
                    view.startAnimation (buttonClick);
                    hideQR();
                    restaura();
                    resfreshInfo ();
                    imageChange();

                }

            }
        };
        sumaBttn.setOnClickListener(listener);
        regresarBttn.setOnClickListener(listener);


        //Desible backButton key
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

        //Cuandp se toca es espacio debajo del QR
       /* emptyRL.setOnTouchListener(new View.OnTouchListener () {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideQR();
                restaura();
                resfreshInfo ();
                imageChange();
                return false;
            }

        });*/
    }

    //Se muestra el codigo QR
    public void showQR(){
        // Start animation
        qrView.bringToFront();
        qrView.setVisibility (View.VISIBLE);
        qrView.startAnimation(slide_up);
        emptyRL.setVisibility (View.VISIBLE);
        visitasTV.setText (Integer.toString (iPuntos));

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {

            public void run() {
                sumaBttn.setVisibility (View.GONE);
            }
        };
        handler.postDelayed(runnable, 300); //for initial delay..


    }

    public void hideQR(){
        qrView.startAnimation(slide_down);
        qrView.setVisibility (View.GONE);
        sumaBttn.setVisibility (View.VISIBLE);
    }


    //Se restauran los vasos y numeros a 0 visitas
    public void restaura(){
        arrayIV[1].setVisibility (View.GONE);
        arrayIV[2].setVisibility (View.GONE);
        arrayIV[3].setVisibility (View.GONE);
        arrayIV[4].setVisibility (View.GONE);
        arrayIV[5].setVisibility (View.GONE);
        arrayIV[6].setVisibility (View.GONE);

        arrayTV[1].setVisibility (View.VISIBLE);
        arrayTV[2].setVisibility (View.VISIBLE);
        arrayTV[3].setVisibility (View.VISIBLE);
        arrayTV[4].setVisibility (View.VISIBLE);
        arrayTV[5].setVisibility (View.VISIBLE);
        arrayTV[6].setVisibility (View.VISIBLE);

    }

    //Sa asignana las visitas correspondientes
    public void imageChange(){

        iPuntos = utils.getPuntos (getContext ());
        if(iPuntos > 0) {

            final Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                int i = 1;

                public void run() {

                    if (iPuntos > 0) {
                        if(iPuntos == 1)
                            i = 1;
                        arrayIV[i].setVisibility (View.VISIBLE);
                        arrayTV[i].setVisibility (View.GONE);
                        if(i == 6)
                            i=1;
                        i++;
                    }


                    if (i == iPuntos+1) {
                        i = 1;
                    }
                    handler.postDelayed(this, 100);  //for interval...
                }
            };
            handler.postDelayed(runnable, 600); //for initial delay..
        }




        Typeface gotham_light = Typeface.createFromAsset(getContext().getAssets(),"fonts/gotham_light.ttf");
        Typeface gotham_bold = Typeface.createFromAsset(getContext().getAssets(),"fonts/gotham_bold.ttf");


        if(iPuntos == 6){
            SpannableStringBuilder spanString = new SpannableStringBuilder("¡Tienes una bebida gratis!");
            spanString.setSpan(new CustomTypefaceSpan("", gotham_light), 0, 17,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanString.setSpan(new CustomTypefaceSpan ("", gotham_bold), 18, 25,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanString.setSpan(new CustomTypefaceSpan("", gotham_light), 25, 26,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            faltantesTV.setText (spanString);
            sumaBttn.setText("OBTÉN TU BEBIDA");
            sumaBttn.setBackgroundResource(R.color.marron);
            sumaBttn.setTextColor(getApplicationContext().getResources().getColor(R.color.white));

        } else {
            SpannableStringBuilder spanString = new SpannableStringBuilder("¡Te faltan " + (6 - iPuntos) + " visitas para tener tu bebida gratis!");
            spanString.setSpan(new CustomTypefaceSpan("", gotham_light), 0, 41, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanString.setSpan(new CustomTypefaceSpan("", gotham_bold), 42, 48, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanString.setSpan(new CustomTypefaceSpan("", gotham_light), 48, 49, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            faltantesTV.setText(spanString);
            sumaBttn.setText("SUMA VISITAS");
            sumaBttn.setBackgroundResource(R.color.cafe2);
            sumaBttn.setTextColor(getApplicationContext().getResources().getColor(R.color.cafe_claro));

        }
    }




    //Genera codigo QR
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


    //Se llena la lista de beneficios
    /*public List<Beneficio> getDataForListView() {

        Beneficio beneficio;

        List<Beneficio> listaBeneficio = new ArrayList<Beneficio> ();
        beneficio =  new Beneficio ("2x1 EN BEBIDAS",
                "Ven a conocer nuestra nueva bebida: Naranjo Huacal", true);
        listaBeneficio.add(beneficio);

        return listaBeneficio;

    }*/


    //Se cambia el font de los textos
    public void changeFonts(View view){

        Typeface gotham_light = Typeface.createFromAsset(getContext().getAssets(),"fonts/gotham_light.ttf");
        saludoTV.setTypeface(gotham_light);
        sumaBttn.setTypeface (gotham_light);
        deslizaTV.setTypeface (gotham_light);
        textView1.setTypeface (gotham_light);
        regresarBttn.setTypeface (gotham_light);

        Typeface gotham_bold = Typeface.createFromAsset(getContext().getAssets(),"fonts/gotham_bold.ttf");
        nombreTV.setTypeface (gotham_bold);


        typeFace= Typeface.createFromAsset(getContext().getAssets(),"fonts/courier_bold2.ttf");
        visitasTV.setTypeface (typeFace);

        typeFace= Typeface.createFromAsset(getContext().getAssets(),"fonts/courier_bold.ttf");
        textView2.setTypeface (typeFace);
        arrayTV[1].setTypeface(typeFace);
        arrayTV[2].setTypeface(typeFace);
        arrayTV[3].setTypeface(typeFace);
        arrayTV[4].setTypeface(typeFace);
        arrayTV[5].setTypeface(typeFace);
        arrayTV[6].setTypeface(typeFace);

        //tituloTV.setTypeface (typeFace);

        /*SpannableStringBuilder spanString = new SpannableStringBuilder("¡Te faltan " + (6 - iPuntos) + " visitas para tener tu bebida gratis!");
        spanString.setSpan(new CustomTypefaceSpan("", gotham_light), 0, 41,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        spanString.setSpan(new CustomTypefaceSpan ("", gotham_bold), 42, 48,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        spanString.setSpan(new CustomTypefaceSpan("", gotham_light), 48, 49,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        faltantesTV.setText (spanString);*/

        SpannableStringBuilder spanString2 = new SpannableStringBuilder("Junta 6 visitas para una bebida gratis \nen tu próxima compra.");
        spanString2.setSpan(new CustomTypefaceSpan("", gotham_light), 0, 5,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        spanString2.setSpan(new CustomTypefaceSpan ("", gotham_bold), 6, 15,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        spanString2.setSpan(new CustomTypefaceSpan("", gotham_light), 15, 24,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        spanString2.setSpan(new CustomTypefaceSpan ("", gotham_bold), 25, 38,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        spanString2.setSpan(new CustomTypefaceSpan("", gotham_light), 38, 61,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        parrafo1.setText(spanString2);

        SpannableStringBuilder spanString3 = new SpannableStringBuilder("¡Ven a Tierra Garat!\n\nDa click en suma visitas y obtén tu bebida, acerca tu teléfono con el código QR al cafetero para registrar visitas o canjear tu bebida.");
        spanString3.setSpan(new CustomTypefaceSpan ("", gotham_bold), 0, 21,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        spanString3.setSpan(new CustomTypefaceSpan("", gotham_light), 21, 33,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        spanString3.setSpan(new CustomTypefaceSpan ("", gotham_bold), 33, 46,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        spanString3.setSpan(new CustomTypefaceSpan("", gotham_light), 47, 48,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        spanString3.setSpan(new CustomTypefaceSpan ("", gotham_bold), 48, 65,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        spanString3.setSpan(new CustomTypefaceSpan("", gotham_light), 66, 157,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        parrafo2.setText (spanString3);

    }

    public  void timeComparation(){
        Date currentTime = Calendar.getInstance().getTime();
        Date diaTime = Calendar.getInstance().getTime();
        Date tardeTime = Calendar.getInstance().getTime();
        Date nocheTime = Calendar.getInstance().getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String sTimeFormat = sdf.format(c.getTime());
        try {

            currentTime = sdf.parse(sTimeFormat);
            diaTime = sdf. parse ("23:59");
            tardeTime = sdf.parse("11:59");
            nocheTime = sdf.parse ("19:59");

        } catch (ParseException e) {
            e.printStackTrace ();
        }

        if(currentTime.after(tardeTime) && currentTime.before (nocheTime)){
            saludoTV.setText ("Buenas tardes,");
        }else if(currentTime.after (nocheTime)){
            saludoTV.setText ("Buenas noches,");
        }else if(currentTime.before (tardeTime)){
            saludoTV.setText ("Buenos días,");
        }
    }

    public void resfreshInfo(){
        //Verifica que haya conexion de internet
        ConnectivityManager networkManager = (ConnectivityManager) getContext ().getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = networkManager.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();
            if(isConnected){

                boolean tipo = PreferenceUtils.getSession (getContext ());
                //Checa el tipo de session
                if(tipo) {
                    //Facebook session
                    new LoadDataLoginFacebook ((MainActivity) getActivity ()).execute (
                            PreferenceUtils.getName (getContext ()),
                            PreferenceUtils.getPassword (getContext ()),
                            PreferenceUtils.getEmail (getContext ()));
                }
                else {
                    //Manual session
                    new LoadDataLogin ((MainActivity) getActivity ()).execute (
                            PreferenceUtils.getEmail (getContext ()),
                            PreferenceUtils.getPassword (getContext ()));
                }
            }
            else{
                Toast.makeText (getActivity (), "No hay conexión de internet", Toast.LENGTH_SHORT).show();
            }
    }

    @Override
    public void onResume() {
        super.onResume();
        //Analytics
        mTracker.setScreenName("Re");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

}
