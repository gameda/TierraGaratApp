package mx.digitalcoaster.tierra_garat_puntos.fragments.registration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import mx.digitalcoaster.tierra_garat_puntos.R;
import mx.digitalcoaster.tierra_garat_puntos.activities.RegistrationActivity;
import mx.digitalcoaster.tierra_garat_puntos.helpers.AnalyticsApplication;
import mx.digitalcoaster.tierra_garat_puntos.models.User;
import mx.digitalcoaster.tierra_garat_puntos.sync.CodigoPostalHttpGet;
import mx.digitalcoaster.tierra_garat_puntos.sync.LoadDataLogin;
import mx.digitalcoaster.tierra_garat_puntos.sync.LoadDataRegister;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.support.v4.content.ContextCompat.getSystemService;
import static android.view.inputmethod.EditorInfo.IME_ACTION_DONE;


public class Register3Fragment extends Fragment{

    private static final String TAG = "Register3Fragment";
    String sCiudad, sColonia = "", sCP;
    TextView textView, estadoTV, ciudadTV, coloniaTV, avisoTV;
    Spinner coloniaSP;
    EditText cpET;
    Typeface typeFace;
    ImageView check1;
    Button nextBttn;
    ImageButton backBttn;
    View coloniaView;
    View view;
    User user;
    CheckBox checkbox;
    ArrayList<String> colonias;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.2F);
    private Tracker mTracker;        //Analytics



    public Register3Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super .onCreateView (inflater,container, savedInstanceState);
        View view = inflater.inflate (R.layout.fragment_register3, container, false);
        Log.d(TAG, "onCreateView: started.");

        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        // Inflate the layout for this fragment

        if(view != null){

            cpET = view.findViewById(R.id.cpET);
            estadoTV = view.findViewById(R.id.estadoTV);
            ciudadTV = view.findViewById(R.id.ciudadTV);
            coloniaTV = view.findViewById(R.id.coloniaTV);
            coloniaSP = view.findViewById(R.id.coloniaSP);
            nextBttn = view.findViewById (R.id.nextBttn);
            backBttn = view.findViewById (R.id.backBttn);
            avisoTV = view.findViewById (R.id.avisoTV);
            checkbox = view.findViewById (R.id.checkbox);
            coloniaView = view.findViewById(R.id.coloniaView);


            changeFonts (view);

        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);

        user = ((RegistrationActivity)getActivity()).user;        //Se recive el objeto user

        avisoTV.setMovementMethod(LinkMovementMethod.getInstance());

        //Hacer submit desde el teclado y pasar a la siguiente activity
        cpET.setOnEditorActionListener (new EditText.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Intent intent;
                if (actionId == EditorInfo.IME_ACTION_DONE ) {

                    //Lo escrito en el ET se pasa a una String
                    sCP = cpET.getText().toString ();

                }

                return false;
            }
        });

        //Click en boton confirmar
        View.OnClickListener listener = new View.OnClickListener (){
            @Override
            public void onClick(View view) {
                if (nextBttn.isPressed()) {
                    view.startAnimation (buttonClick);

                    //Lo escrito en el ET se pasa a una String
                    sCP = cpET.getText().toString ();

                    //Se verifica que se haya escrito la calle
                    if(sColonia.isEmpty()){
                        Toast.makeText(getActivity(), "Ingresa un C.P. válido, para continuar", Toast.LENGTH_SHORT).show();


                    }else if(!checkbox.isChecked ()){
                        Toast.makeText(getActivity(), "Acepta nuestros términos y condiciones, para finalizar el registro", Toast.LENGTH_SHORT).show();

                    }
                    else{

                        if(conexionInternet()) {
                            user.setsCity (sCiudad);
                            user.setiCP (Integer.parseInt (sCP));
                            user.setiNumE(0);
                            user.setsAddress("-");
                            //Se mada a .../appregister
                            new LoadDataRegister (((RegistrationActivity) getActivity ())).execute (user);
                        }
                        else
                            Toast.makeText (getActivity (), "No hay conexión de internet", Toast.LENGTH_SHORT).show();

                    }
                }else if(backBttn.isPressed ()){
                    view.startAnimation (buttonClick);
                    getActivity ().onBackPressed();

                }
            }
        };
        nextBttn.setOnClickListener(listener);
        backBttn.setOnClickListener (listener);

        cpET.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 5){
                    if(conexionInternet()){
                        getColonias(cpET.getText().toString());
                    }
                    else
                        Toast.makeText (getActivity (), "No hay conexión de internet", Toast.LENGTH_SHORT).show();
                }
                else{
                    estadoTV.setVisibility(View.INVISIBLE);
                    ciudadTV.setVisibility(View.INVISIBLE);
                    coloniaTV.setVisibility(View.INVISIBLE);
                    coloniaView.setVisibility(View.INVISIBLE);
                    sColonia = "";
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Overides el backButton
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

    public void getColonias(String cp) {

        colonias = new ArrayList<String>();
        String page = "";
        String url = "https://api-codigos-postales.herokuapp.com/v2/codigo_postal/" + cp;


        //String to place our result in
        String result = "";

        //Instantiate new instance of our class
        CodigoPostalHttpGet getRequest = new CodigoPostalHttpGet();

        //Perform the doInBackground method, passing in our url
        try {
            result = getRequest.execute(url, "").get();

        } catch (ExecutionException e) {
            e.printStackTrace();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        String estado = "", ciudad = "";
        JSONArray coloniaArray = null;

        try {
            JSONObject response = new JSONObject(result);
            coloniaArray = response.getJSONArray("colonias");
            estado = response.getString("estado");
            ciudad = response.getString("municipio");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(coloniaArray.length() > 0){

            for (int i = 0; i < coloniaArray.length(); i++) {

                try {
                    colonias.add(coloniaArray.getString(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            hideSoftKeyboard(getActivity());
            estadoTV.setText("- " + estado);
            ciudadTV.setText("- " + ciudad);
            estadoTV.setVisibility(View.VISIBLE);
            ciudadTV.setVisibility(View.VISIBLE);
            coloniaTV.setVisibility(View.VISIBLE);
            coloniaView.setVisibility(View.VISIBLE);
            sColonia = colonias.get(0);
            sCiudad = ciudad;
            coloniaSpinner();

        }


    }




    public void coloniaSpinner(){

        //Colonias
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity (), R.layout.row_fecha, colonias){
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                typeFace= Typeface.createFromAsset(getContext().getAssets(),"fonts/courier.ttf");
                ((TextView) v).setTypeface(typeFace);

                return v;
            }

            public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                View v = super.getDropDownView (position, convertView, parent);

                typeFace= Typeface.createFromAsset(getContext().getAssets(),"fonts/courier.ttf");
                ((TextView) v).setTypeface(typeFace);

                return v;
            }
        };
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        coloniaSP.setAdapter(adapter);
        coloniaSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                sColonia = colonias.get (i);

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public static void hideSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
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
        textView = view.findViewById (R.id.text1);

        typeFace= Typeface.createFromAsset(getContext().getAssets(),"fonts/gotham_light.ttf");
        nextBttn.setTypeface (typeFace);
        avisoTV.setTypeface (typeFace);
        coloniaTV.setTypeface(typeFace);

        typeFace= Typeface.createFromAsset(getContext().getAssets(),"fonts/gotham_bold.ttf");
        textView.setTypeface(typeFace);
        estadoTV.setTypeface(typeFace);
        ciudadTV.setTypeface(typeFace);


        typeFace= Typeface.createFromAsset(getContext().getAssets(),"fonts/courier.ttf");
        cpET.setTypeface (typeFace);

    }




    //Se actuliza el frangment una vez que se hace visible
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint (isVisibleToUser);
        if (isVisibleToUser){
          //Do something
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //Analytics
        mTracker.setScreenName("Register3Fragment");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

}
