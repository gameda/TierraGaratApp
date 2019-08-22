package mx.digitalcoaster.tierra_garat_puntos.fragments.access.recuperarContraseña;


import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import mx.digitalcoaster.tierra_garat_puntos.fragments.registration.Register2Fragment;
import mx.digitalcoaster.tierra_garat_puntos.helpers.AnalyticsApplication;

import static android.view.inputmethod.EditorInfo.IME_ACTION_NEXT;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class Recuperar1Fragment extends Fragment {

    ImageButton backBttn;
    Button nextBttn;
    EditText emailET;
    private Tracker mTracker;
    TextView textView1, textView2, textView3, textView4;
    String sName, sEmail;
    ImageView check;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.2F);




    public Recuperar1Fragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super .onCreateView (inflater,container, savedInstanceState);

        //Analytics
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();

        inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate (R.layout.fragment_recuperar1, container, false);

        if(view != null){
            emailET = view.findViewById (R.id.correoET);
            check = view.findViewById (R.id.iv2);
            nextBttn = view.findViewById (R.id.nextBttn );
            backBttn = view.findViewById (R.id.backBttn);
            textView1 = view.findViewById(R.id.text1);
            textView2 = view.findViewById(R.id.text2);
            textView3 = view.findViewById (R.id.correoTV);


            changeFonts (view);
        }
        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        onKeyboardListener();

        View.OnClickListener listener = new View.OnClickListener (){

            @Override
            public void onClick(View view) {

                //Lo escrito en el ET se pasa a una String
                sEmail = emailET.getText ().toString ();


                if (nextBttn.isPressed()) {
                    view.startAnimation (buttonClick);

                    //Se verifica que se haya escrito un correo y este correcto
                    if(sEmail.matches ("")){
                        Toast.makeText(getActivity (), "Escribe tu correo electronico", Toast.LENGTH_SHORT).show();

                    } else if(!isEmailValid (sEmail)) {
                        Toast.makeText (getActivity (), "Correo invalido", Toast.LENGTH_SHORT).show ();

                    }else{
                        if(conexionInternet())
                            sendCorreo ();
                        else
                            Toast.makeText (getActivity (), "No hay conexión de internet", Toast.LENGTH_SHORT).show();
                    }


                } else if(backBttn.isPressed ()){
                    view.startAnimation (buttonClick);
                    getActivity ().onBackPressed();

                }
            }
        };
        nextBttn.setOnClickListener(listener);
        backBttn.setOnClickListener (listener);

        emailET.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == IME_ACTION_NEXT) {

                    sEmail = emailET.getText().toString();

                    //Se verifica que se haya escrito un correo y este correcto
                    if (!sEmail.matches("") && isEmailValid(sEmail)) {
                        check.setVisibility(v.VISIBLE);
                        return false;

                    } else {
                        check.setVisibility(v.INVISIBLE);
                        return false;

                    }
                }
                return false;
            }
        });
    }

    //Se envia un correo
    public void sendCorreo(){


        JSONObject obj = new JSONObject();

        try {
            obj.put("email", sEmail);

        } catch (JSONException e) {
            e.printStackTrace ();
        }

        String url = "http://tierragarat.digitalcoaster.mx/temporaryPass";
        //call callback method
        final AsyncHttpClient client = new AsyncHttpClient();
        HttpEntity entity;
        entity = new StringEntity (obj.toString(), "UTF-8");

        client.post (getContext (), url, entity, "application/json", new JsonHttpResponseHandler (){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess (statusCode, headers, response);

                String respuesta = response.toString ();
                if(respuesta.contains ("not-ok")){
                    Toast.makeText(getApplicationContext(),"Correo no registrado en la base de datos", Toast.LENGTH_LONG).show ();

                }
                else {
                    Fragment fragment = new Recuperar2Fragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("correo", sEmail);
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentView, fragment);
                    fragmentTransaction.commit();

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

    //Se verifica el ETcorreo
    public void onKeyboardListener() {

        emailET.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == IME_ACTION_NEXT) {

                    sEmail = emailET.getText().toString();

                    //Se verifica que se haya escrito un correo y este correcto
                    if (!sEmail.matches("") && isEmailValid(sEmail)) {
                        check.setVisibility(v.VISIBLE);
                        return false;

                    } else {
                        check.setVisibility(v.INVISIBLE);
                        return false;

                    }
                }
                return false;
            }
        });

    }



    //Funcion que verifica que sea un email valido.
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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

        Typeface typeFace = Typeface.createFromAsset(getContext().getAssets(), "fonts/gotham_light.ttf");
        nextBttn.setTypeface (typeFace);
        textView2.setTypeface(typeFace);
        textView3.setTypeface(typeFace);

        typeFace= Typeface.createFromAsset(getContext().getAssets(),"fonts/gotham_bold.ttf");
        textView1.setTypeface(typeFace);

        typeFace= Typeface.createFromAsset(getContext().getAssets(),"fonts/courier.ttf");
        emailET.setTypeface (typeFace);

    }
    @Override
    public void onResume() {
        super.onResume();
        //Analytics
        mTracker.setScreenName("Recuperar1Fragment");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }





}
