package mx.digitalcoaster.tierra_garat_puntos.fragments.registration;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

import mx.digitalcoaster.tierra_garat_puntos.R;
import mx.digitalcoaster.tierra_garat_puntos.activities.RegistrationActivity;
import mx.digitalcoaster.tierra_garat_puntos.fragments.main.settings.SettingNotificacionesFragment;
import mx.digitalcoaster.tierra_garat_puntos.helpers.AnalyticsApplication;
import mx.digitalcoaster.tierra_garat_puntos.models.User;

import static android.view.inputmethod.EditorInfo.IME_ACTION_DONE;
import static android.view.inputmethod.EditorInfo.IME_ACTION_NEXT;

public class Register1Fragment extends Fragment {

    private static final String TAG = "Register1Fragment";

    String sName, sApellido;
    String sSexo = "", sDia, sMes, sAño;
    EditText nombreET,apellidoET;
    Button mujerBttn, hombreBttn, nextBttn;
    ImageButton backBttn;
    Spinner diaSP, mesSP, añoSP;
    ImageView check1, check2;
    TextView textView1, textView2, textView3, textView4, textView5;
    Typeface typeFace;
    User user;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.2F);
    private Tracker mTracker;        //Analytics




    public Register1Fragment(){

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super .onCreateView (inflater,container, savedInstanceState);
        View view = inflater.inflate (R.layout.fragment_register1, container, false);
        Log.d(TAG, "onCreateView: started.");

        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();

        if(view != null){
            nombreET = view.findViewById (R.id.nombreET);
            apellidoET = view.findViewById (R.id.apellidoET);
            mujerBttn = view.findViewById (R.id.mujerBttn);
            hombreBttn = view.findViewById (R.id.hombreBttn);
            diaSP = view.findViewById (R.id.diaSP);
            mesSP = view.findViewById (R.id.mesSP);
            añoSP = view.findViewById (R.id.añoSP);
            nextBttn = view.findViewById (R.id.nextBttn);
            backBttn = view.findViewById (R.id.backBttn);
            check1 = view.findViewById (R.id.iv1);
            check2 = view.findViewById (R.id.iv2);






            changeFonts (view);

        }

        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);

        user = ((RegistrationActivity)getActivity()).user;

        fechasSpinner ();
        onKeyboardListener();
        onEditTextListener (nombreET, view);
        onEditTextListener (apellidoET, view);


        View.OnClickListener listener = new View.OnClickListener (){

            @Override
            public void onClick(View view) {

                //Lo escrito en el ET se pasa a una String
                sName = nombreET.getText().toString ();
                sApellido = apellidoET.getText ().toString ();


                if (mujerBttn.isPressed()) {
                    sSexo = "Mujer";
                    //Cambio el background del boton
                    mujerBttn.setBackgroundResource (R.mipmap.btnhoversxo);
                    hombreBttn.setBackgroundResource (R.mipmap.btnsxo);


                } else if(hombreBttn.isPressed ()){
                    sSexo = "Hombre";
                    hombreBttn.setBackgroundResource (R.mipmap.btnhoversxo);
                    mujerBttn.setBackgroundResource (R.mipmap.btnsxo);

                }
                //Boton siguiente
                else if (nextBttn.isPressed()) {
                    view.startAnimation (buttonClick);

                    //Se verifica que se haya escrito un nombre
                    if(sName.matches ("")){
                        Toast.makeText(getActivity(), "Escribe tu nombre", Toast.LENGTH_SHORT).show();
                    }
                    //Se verifica que se haya escrito un correo y este correcto
                     else if(sApellido.matches ("")){
                        Toast.makeText(getActivity (), "Escribe tu correo electronico", Toast.LENGTH_SHORT).show();

                    } else if(sSexo.isEmpty ()){
                        Toast.makeText (getActivity (), "Escoge tu sexo", Toast.LENGTH_SHORT).show ();

                    } else if(sDia == null || sMes == null || sAño == null) {
                        Toast.makeText (getActivity (), "Escribe tu fecha de nacimiento", Toast.LENGTH_SHORT).show ();

                    } else{
                        user.setsName (sName);
                        user.setsSex (sSexo);
                        user.setsBirth (sDia + "-" + sMes + "-" + sAño);

                        //Se avanza al siguiente fragment
                        Fragment fragment = new Register2Fragment ();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragmentView, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                    }


                } else if(backBttn.isPressed ()){
                    view.startAnimation (buttonClick);
                    getActivity ().onBackPressed();

                }
            }
        };
        mujerBttn.setOnClickListener(listener);
        hombreBttn.setOnClickListener(listener);
        nextBttn.setOnClickListener(listener);
        backBttn.setOnClickListener (listener);

    }

    //Asigna años, meses, dias
    public void fechasSpinner(){

        //Años
        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);

        for (int i = thisYear; i >= thisYear - 100; i--) {
            years.add(Integer.toString(i));
        }

        ArrayAdapter<String> adapterAño = new ArrayAdapter<String>(getActivity (), R.layout.row_fecha, years){
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
        adapterAño.setDropDownViewResource(R.layout.spinner_dropdown);
        añoSP.setAdapter(adapterAño);
        añoSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                sAño = years.get (i);

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Meses
        ArrayList<String> months = new ArrayList<String>();
        for (int i = 1; i<13; i++){
            if(i < 10)
                months.add("0" + Integer.toString(i));
            else
                months.add(Integer.toString(i));

        }
        ArrayAdapter<String> adapterMes = new ArrayAdapter<String>(getActivity (), R.layout.row_fecha, months){
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
        adapterMes.setDropDownViewResource(R.layout.spinner_dropdown);
        mesSP.setAdapter(adapterMes);
        mesSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                sMes = months.get (i);

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Dias
        ArrayList<String> days = new ArrayList<String>();
        for (int i = 1; i<32; i++){
            if(i < 10)
                days.add("0" + Integer.toString(i));
            else
                days.add(Integer.toString(i));

        }
        ArrayAdapter<String> adapterDia = new ArrayAdapter<String>(getActivity (), R.layout.row_fecha, days){

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
        adapterDia.setDropDownViewResource(R.layout.spinner_dropdown);
        diaSP.setAdapter(adapterDia);
        diaSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                sDia = days.get (i);

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    //Verifica cuando se termina de escribir del teclado y pasar a la siguiente pregunta
    public void onKeyboardListener(){
        nombreET.setOnEditorActionListener (new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == IME_ACTION_NEXT) {

                    sName = nombreET.getText().toString ();

                    //Se verifica que se haya escrito un nombre
                    if (!sName.matches ("")) {
                        check1.setVisibility (v.VISIBLE);
                        return false;

                    }else{
                        check1.setVisibility (v.INVISIBLE);
                        return false;

                    }
                }
                return false;
            }
        });

        apellidoET.setOnEditorActionListener (new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == IME_ACTION_NEXT) {

                    sApellido = apellidoET.getText ().toString ();

                    //Se verifica que se haya escrito un correo y este correcto
                    if (!sApellido.matches ("")) {
                        check2.setVisibility (v.VISIBLE);
                        return false;

                    }else{
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

                sName = nombreET.getText().toString ();
                sApellido = apellidoET.getText ().toString ();

                if(!sName.isEmpty()){
                    check1.setVisibility (view.VISIBLE);
                }else
                    check1.setVisibility (view.GONE);

                if(!sApellido.isEmpty ()){
                    check2.setVisibility (view.VISIBLE);
                }
                else
                    check2.setVisibility (view.GONE);

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //change bg whenTextChanged
            }
        });
    }




    //Funcion que verifica que sea un email valido.
   git

    //Se cambia el font de los textos
    public void changeFonts(View view){

        textView1 = view.findViewById (R.id.text1);
        textView2 = view.findViewById (R.id.nombreTV);
        textView3 = view.findViewById (R.id.apellidoTV);
        textView4 = view.findViewById (R.id.sexoTV);
        textView5 = view.findViewById (R.id.nacimientoTV);

        typeFace= Typeface.createFromAsset(getContext().getAssets(),"fonts/gotham_light.ttf");
        textView1.setTypeface(typeFace);
        nextBttn.setTypeface (typeFace);

        typeFace= Typeface.createFromAsset(getContext().getAssets(),"fonts/gotham_bold.ttf");
        textView2.setTypeface(typeFace);
        textView3.setTypeface(typeFace);
        textView4.setTypeface(typeFace);
        textView5.setTypeface(typeFace);

        typeFace= Typeface.createFromAsset(getContext().getAssets(),"fonts/courier.ttf");
        nombreET.setTypeface (typeFace);
        apellidoET.setTypeface (typeFace);
        mujerBttn.setTypeface (typeFace);
        hombreBttn.setTypeface (typeFace);


    }

    @Override
    public void onResume() {
        super.onResume();
        //Analytics
        mTracker.setScreenName("Register1Fragment");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }


}

