package mx.digitalcoaster.tierra_garat_puntos.fragments.access.recuperarContraseña;


import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;
import mx.digitalcoaster.tierra_garat_puntos.R;
import mx.digitalcoaster.tierra_garat_puntos.fragments.access.LoginFragment;
import mx.digitalcoaster.tierra_garat_puntos.helpers.AnalyticsApplication;

import static android.view.inputmethod.EditorInfo.IME_ACTION_DONE;
import static android.view.inputmethod.EditorInfo.IME_ACTION_NEXT;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class Recuperar3Fragment extends Fragment {


    TextView textView1, textView2, contraseñaTV, confirmarTV, mensajeTV;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.2F);
    private Tracker mTracker;
    ImageButton backBttn;
    Button nextBttn;
    EditText  contraseñaET, confirmarET;
    ImageView check1, check2;
    View splash;
    String sCodigo, sCorreo, sPassword, sConfirm;



    public Recuperar3Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        sCodigo = getArguments().getString("codigo");
        sCorreo = getArguments().getString("correo");

        //Analytics
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();

        // Inflate the layout for this fragment
        inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate (R.layout.fragment_recuperar3, container, false);

        if(view != null){
            nextBttn = view.findViewById (R.id.nextBttn);
            backBttn = view.findViewById (R.id.backBttn);
            textView1 = view.findViewById (R.id.text1);
            textView2 = view.findViewById (R.id.text2);
            contraseñaTV = view.findViewById (R.id.contraseñaTV);
            confirmarTV = view.findViewById (R.id.confirmarTV);
            contraseñaET = view.findViewById(R.id.contraseñaET);
            confirmarET = view.findViewById(R.id.confirmarET);
            check1 = view.findViewById(R.id.iv3);
            check2 = view.findViewById(R.id.iv4);
            splash = view.findViewById (R.id.loadingView);
            mensajeTV = view.findViewById (R.id.sloganTV);


            changeFonts (view);
        }
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        onKeyboardListener ();
        onEditTextListener (contraseñaET, view);
        onEditTextListener (confirmarET, view);


        //Se muestra splash por dos segundos
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {

            public void run() {
                splash.setVisibility (View.GONE);
            }
        };
        handler.postDelayed(runnable, 2000); //for initial delay..

        View.OnClickListener listener = new View.OnClickListener (){

            @Override
            public void onClick(View view) {

                if (nextBttn.isPressed()) {
                    view.startAnimation (buttonClick);

                    sPassword = contraseñaET.getText ().toString ();
                    sConfirm = confirmarET.getText ().toString ();

                    if(sPassword.matches ("")){
                        Toast.makeText(getActivity(), "Escribe tu contraseña", Toast.LENGTH_SHORT).show();
                    }

                    //Se verifica que se sea mayor de 8 caracteres.
                    else if(sPassword.length () < 8){
                        Toast.makeText(getActivity(), "Tu contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show();
                    }

                    //Se verifica que coinsidan las contraseñas
                    else  if(!sPassword.matches (sConfirm) || sPassword.isEmpty ()){
                        Toast.makeText(getActivity (), "La contraseña no coincide", Toast.LENGTH_SHORT).show();
                    }

                    else{
                        savePassword ();

                    }



                } else if(backBttn.isPressed ()){
                    view.startAnimation (buttonClick);
                    getActivity ().onBackPressed();

                }
            }
        };
        nextBttn.setOnClickListener(listener);
        backBttn.setOnClickListener (listener);

    }

    public void savePassword(){

        splash.setVisibility (View.VISIBLE);

        JSONObject obj = new JSONObject();

        try {
            obj.put("email", sCorreo);
            obj.put("temporaryPass", sCodigo);
            obj.put("newPass", sPassword);

        } catch (JSONException e) {
            e.printStackTrace ();
        }


        String url = "http://tierragarat.digitalcoaster.mx/createNewPass";
        //call callback method
        final AsyncHttpClient client = new AsyncHttpClient();
        HttpEntity entity;
        entity = new StringEntity (obj.toString(), "UTF-8");

        client.post (getContext (), url, entity, "application/json", new JsonHttpResponseHandler (){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess (statusCode, headers, response);

                String status = " ";
                try {
                     status = response.getString ("status");
                } catch (JSONException e) {
                    e.printStackTrace ();
                }
                if (status.matches ("ok")){
                    Fragment fragment = new LoginFragment ();
                    Bundle bundle = new Bundle();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentView, fragment);
                    fragmentTransaction.commit();
                }

                else {

                    Toast.makeText(getApplicationContext(),"Código incorrecto, favor de verificarlo", Toast.LENGTH_LONG).show ();
                    splash.setVisibility (View.GONE);

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure (statusCode, headers, throwable, errorResponse);
                Toast.makeText(getApplicationContext(),"Algo salió mal, intenetelo más tarde", Toast.LENGTH_LONG).show ();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure (statusCode, headers, throwable, errorResponse);
                Toast.makeText(getApplicationContext(),"Algo salió mal, intenetelo más tarde", Toast.LENGTH_LONG).show ();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure (statusCode, headers, responseString, throwable);
                Toast.makeText(getApplicationContext(),"Algo salió mal, intenetelo más tarde", Toast.LENGTH_LONG).show ();

            }
        });
    }

    public void onKeyboardListener() {

        contraseñaET.setOnEditorActionListener (new EditText.OnEditorActionListener () {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == IME_ACTION_NEXT) {

                    sPassword = contraseñaET.getText ().toString ();
                    //Se verifica que se haya escrito un password y sea mayor a 8 caracteres
                    if (!sPassword.matches ("") && sPassword.length () >= 8) {
                        check1.setVisibility (v.VISIBLE);
                        return false;

                    } else {
                        check1.setVisibility (v.INVISIBLE);
                        return false;

                    }
                }
                return false;
            }
        });

        confirmarET.setOnEditorActionListener (new EditText.OnEditorActionListener () {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == IME_ACTION_DONE) {
                    sConfirm = confirmarET.getText ().toString ();
                    //Se verifica que coinsidan las contraseñas
                    if (sPassword.matches (sConfirm) && !sConfirm.isEmpty ()) {
                        check2.setVisibility (v.VISIBLE);
                        return false;

                    } else {
                        check2.setVisibility (v.INVISIBLE);
                        return false;

                    }
                }
                return false;
            }
        });
    }

    //Se activa un evento cuando se escribe sobre un EditText en especifico
    public void onEditTextListener(EditText et, View view){
        et.addTextChangedListener(new TextWatcher () {

            @Override
            public void afterTextChanged(Editable s) {
                //change bg afterTextChanged
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //change bg beforeTextChanged

                sPassword = contraseñaET.getText ().toString ();
                sConfirm = confirmarET.getText ().toString ();


                if(!sPassword.isEmpty () && sPassword.length () > 7){
                    check1.setVisibility (view.VISIBLE);
                }

                if(!sConfirm.isEmpty () && sConfirm.matches (sPassword) ){
                    check2.setVisibility (view.VISIBLE);
                }
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //change bg whenTextChanged
            }
        });
    }



    //Se cambia el font de los textos
    public void changeFonts(View view){

        Typeface typeFace = Typeface.createFromAsset(getContext().getAssets(), "fonts/gotham_light.ttf");
        nextBttn.setTypeface (typeFace);
        textView2.setTypeface(typeFace);
        contraseñaTV.setTypeface(typeFace);
        confirmarTV.setTypeface(typeFace);

        typeFace= Typeface.createFromAsset(getContext().getAssets(),"fonts/gotham_bold.ttf");
        textView1.setTypeface(typeFace);
        contraseñaTV.setTypeface(typeFace);
        confirmarTV.setTypeface(typeFace);

        typeFace= Typeface.createFromAsset(getContext().getAssets(),"fonts/courier.ttf");
        contraseñaET.setTypeface(typeFace);
        confirmarET.setTypeface(typeFace);
        mensajeTV.setTypeface (typeFace);


    }

    @Override
    public void onResume() {
        super.onResume();
        //Analytics
        mTracker.setScreenName("Recuperar3Fragment");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
