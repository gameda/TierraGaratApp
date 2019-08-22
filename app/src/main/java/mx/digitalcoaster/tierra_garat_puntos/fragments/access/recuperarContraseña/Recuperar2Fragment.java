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
import android.text.Editable;
import android.text.TextWatcher;
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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;
import mx.digitalcoaster.tierra_garat_puntos.R;
import mx.digitalcoaster.tierra_garat_puntos.helpers.AnalyticsApplication;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class Recuperar2Fragment extends Fragment {

    ImageButton backBttn;
    Button nextBttn;
    EditText num1, num2, num3, num4;
    private Tracker mTracker;
    TextView textView1, textView2, textView3, textView4, enviaTV;
    String sCorreo, sNum1, sNum2, sNum3, sNum4;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.2F);


    public Recuperar2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        sCorreo = getArguments().getString("correo");
        //Analytics
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();

        inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate (R.layout.fragment_recuperar2, container, false);

        if(view != null){
            nextBttn = view.findViewById (R.id.nextBttn);
            backBttn = view.findViewById (R.id.backBttn);
            textView1 = view.findViewById (R.id.text1);
            textView2 = view.findViewById (R.id.text2);
            textView3 = view.findViewById (R.id.text3);
            textView4 = view.findViewById (R.id.text4);
            enviaTV = view.findViewById (R.id.text5);
            num1 = view.findViewById (R.id.num1);
            num2 = view.findViewById (R.id.num2);
            num3 = view.findViewById (R.id.num3);
            num4 = view.findViewById (R.id.num4);
            changeFonts (view);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textView2.setText("Enviamos el código a " + sCorreo + "\nPor favor ingrésalo aquí:");
        keyBoardListener(num1, num2);
        keyBoardListener(num2, num3);
        keyBoardListener(num3, num4);

        View.OnClickListener listener = new View.OnClickListener (){

            @Override
            public void onClick(View view) {

                //Lo escrito en el ET se pasa a una String
                sNum1 = num1.getText ().toString ();
                sNum2 = num2.getText ().toString ();
                sNum3 = num3.getText ().toString ();
                sNum4 = num4.getText ().toString ();

                if (nextBttn.isPressed()) {
                    view.startAnimation (buttonClick);

                    //Se verifica que se haya escrito un correo y este correcto
                    if(sNum1.isEmpty() || sNum2.isEmpty() || sNum3.isEmpty() || sNum4.isEmpty()){
                        Toast.makeText(getActivity (), "Escribe todos los campos", Toast.LENGTH_SHORT).show();

                    } else{

                        Fragment fragment = new Recuperar3Fragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("codigo", sNum1 + sNum2 + sNum3 + sNum4);
                        bundle.putString("correo", sCorreo);
                        fragment.setArguments(bundle);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragmentView, fragment);
                        fragmentTransaction.commit();
                    }


                } else if(backBttn.isPressed ()){
                    view.startAnimation (buttonClick);
                    getActivity ().onBackPressed();


                }else if(enviaTV.isPressed()){
                    view.startAnimation (buttonClick);
                    if(conexionInternet()){
                        Toast.makeText(getApplicationContext(),"Correo enviado", Toast.LENGTH_LONG).show ();
                    }else{

                    }


                    //Se renvia el correo
                }
            }
        };
        nextBttn.setOnClickListener(listener);
        backBttn.setOnClickListener (listener);
        enviaTV.setOnClickListener (listener);

    }

    public void keyBoardListener(EditText et1, EditText et2){

        et1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(et1.getText().toString().length() == 1)     //size as per your requirement
                {
                    et2.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });
    }

    //Se envia un correo
    public void sendCorreo(){


        JSONObject obj = new JSONObject();

        try {
            obj.put("email", sCorreo);

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
                    Toast.makeText(getApplicationContext(),"Correo reenviado", Toast.LENGTH_LONG).show ();


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
        enviaTV.setTypeface(typeFace);

        typeFace= Typeface.createFromAsset(getContext().getAssets(),"fonts/gotham_bold.ttf");
        textView1.setTypeface(typeFace);
        textView4.setTypeface(typeFace);

        typeFace= Typeface.createFromAsset(getContext().getAssets(),"fonts/courier.ttf");
        num1.setTypeface(typeFace);
        num2.setTypeface(typeFace);
        num3.setTypeface(typeFace);
        num4.setTypeface(typeFace);

    }

    @Override
    public void onResume() {
        super.onResume();
        //Analytics
        mTracker.setScreenName("Recuperar2Fragment");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
