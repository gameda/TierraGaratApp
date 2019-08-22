package mx.digitalcoaster.tierra_garat_puntos.fragments.registration;


import android.graphics.Typeface;
import android.os.Bundle;
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
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.Calendar;

import mx.digitalcoaster.tierra_garat_puntos.R;
import mx.digitalcoaster.tierra_garat_puntos.activities.RegistrationActivity;
import mx.digitalcoaster.tierra_garat_puntos.helpers.AnalyticsApplication;
import mx.digitalcoaster.tierra_garat_puntos.models.User;
import mx.digitalcoaster.tierra_garat_puntos.preferences.PreferenceUtils;

import static android.view.inputmethod.EditorInfo.IME_ACTION_DONE;
import static android.view.inputmethod.EditorInfo.IME_ACTION_NEXT;


public class Register2Fragment extends Fragment {


    private static final String TAG = "Register2Fragment";

    TextView textView1, textView2, textView3, textView4, textView5;
    Typeface typeFace;
    Button nextBttn;
    ImageButton backBttn;
    EditText telET, emailET, passwordET, confirmET;
    ImageView check1, check2, check3, check4;
    User user;
    String sTel, sEmail, sPassword, sConfirm;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.2F);
    private Tracker mTracker;        //Analytics



    Calendar c = Calendar.getInstance();
    int iYear = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);

    public Register2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super .onCreateView (inflater,container, savedInstanceState);
        View view = inflater.inflate (R.layout.fragment_register2, container, false);
        Log.d(TAG, "onCreateView: started.");

        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        // Inflate the layout for this fragment

        if(view != null){
            emailET = view.findViewById (R.id.correoET);
            telET = view.findViewById (R.id.telET);
            passwordET = view.findViewById (R.id.contraseñaET);
            confirmET = view.findViewById (R.id.confirmarET);
            check1 = view.findViewById (R.id.iv1);
            check2 = view.findViewById (R.id.iv2);
            check3 = view.findViewById (R.id.iv3);
            check4 = view.findViewById (R.id.iv4);
            nextBttn = view.findViewById (R.id.nextBttn);
            backBttn = view.findViewById (R.id.backBttn);


            changeFonts (view);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);

        user = ((RegistrationActivity)getActivity()).user;        //Se recive el objeto user

        onEditTextListener (telET, view);
        onEditTextListener (emailET, view);
        onEditTextListener (passwordET, view);
        onEditTextListener (confirmET, view);

        View.OnClickListener listener = new View.OnClickListener (){

            @Override
            public void onClick(View view) {


                sTel = telET.getText ().toString ();
                sEmail = emailET.getText ().toString ();
                sPassword = passwordET.getText ().toString ();
                sConfirm = confirmET.getText ().toString ();

                if (nextBttn.isPressed()) {
                    if (sTel.matches ("")) {
                        Toast.makeText (getActivity (), "Teléfono invalido", Toast.LENGTH_SHORT).show ();
                    }
                    //Se verifica que se haya escrito un correo y este correcto
                    else if (sEmail.matches ("")) {
                        Toast.makeText (getActivity (), "Escribe tu correo electronico", Toast.LENGTH_SHORT).show ();

                    } else if (!isEmailValid (sEmail)) {
                        Toast.makeText (getActivity (), "Correo invalido", Toast.LENGTH_SHORT).show ();
                    }

                    //Se verifica que se haya escrito un password y sea mayor a 8 caracteres
                    else if (sPassword.matches ("")) {
                        Toast.makeText (getActivity (), "Escribe tu contraseña", Toast.LENGTH_SHORT).show ();
                    }

                    //Se verifica que se sea mayor de 8 caracteres.
                    else if (sPassword.length () < 8) {
                        Toast.makeText (getActivity (), "Tu contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show ();
                    }
                    //Se verifica que coinsidan las contraseñas
                    else if (!sPassword.matches (sConfirm) || sPassword.isEmpty ()) {
                        Toast.makeText (getActivity (), "La contraseña no coincide", Toast.LENGTH_SHORT).show ();
                    }
                    //todo OK
                    else {
                        user.setsTel (sTel);
                        user.setsEmail (sEmail);
                        user.setsPassword (sPassword);
                        PreferenceUtils.savePassword (sPassword, getContext());

                        //Se avanza al siguiente fragment
                        Fragment fragment = new Register3Fragment ();
                        FragmentManager fragmentManager = getActivity ().getSupportFragmentManager ();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction ();
                        fragmentTransaction.replace (R.id.fragmentView, fragment);
                        fragmentTransaction.addToBackStack (null);
                        fragmentTransaction.commit ();

                    }

                }else if(backBttn.isPressed ()){
                    view.startAnimation (buttonClick);
                    getActivity ().onBackPressed();

                }
            }
        };
        nextBttn.setOnClickListener(listener);
        backBttn.setOnClickListener (listener);


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


    } //Termina el onViewCreated

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    //Verifica cuando se termina de escribir del teclado y pasar a la siguiente pregunta
    public void onKeyboardListener(){
        telET.setOnEditorActionListener (new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == IME_ACTION_NEXT) {

                    sTel = telET.getText().toString ();

                    //Se verifica que se haya escrito un nombre
                    if (!sTel.matches ("")) {
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

        emailET.setOnEditorActionListener (new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == IME_ACTION_NEXT) {

                    sEmail = emailET.getText ().toString ();

                    //Se verifica que se haya escrito un correo y este correcto
                    if (!sEmail.matches ("")) {
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

        passwordET.setOnEditorActionListener (new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == IME_ACTION_NEXT) {

                    sPassword = passwordET.getText ().toString ();
                    //Se verifica que se haya escrito un password y sea mayor a 8 caracteres
                    if (!sPassword.matches ("") && sPassword.length () >= 8) {
                        check3.setVisibility (v.VISIBLE);
                        return false;

                    }else{
                        check3.setVisibility (v.INVISIBLE);
                        return false;

                    }
                }
                return false;
            }
        });

        confirmET.setOnEditorActionListener (new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == IME_ACTION_DONE) {
                    sConfirm = confirmET.getText ().toString ();
                    //Se verifica que coinsidan las contraseñas
                    if (sPassword.matches (sConfirm) && !sConfirm.isEmpty ()) {
                        check4.setVisibility (v.VISIBLE);
                        return false;

                    }else{
                        check4.setVisibility (v.INVISIBLE);
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

                sEmail = emailET.getText ().toString ();
                sPassword = passwordET.getText ().toString ();
                sConfirm = confirmET.getText ().toString ();
                sTel = telET.getText().toString ();

                if(!sTel.isEmpty()){
                    check1.setVisibility (view.VISIBLE);
                }else
                    check1.setVisibility (view.GONE);

                if(!sEmail.isEmpty () && isEmailValid (sEmail)){
                    check2.setVisibility (view.VISIBLE);
                }

                if(!sPassword.isEmpty () && sPassword.length () > 7){
                    check3.setVisibility (view.VISIBLE);
                }

                if(!sConfirm.isEmpty () && sConfirm.equals (sPassword) ){
                    check4.setVisibility (view.VISIBLE);
                }
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //change bg whenTextChanged
                if( sConfirm.equals (sPassword) ){
                    check4.setVisibility (view.VISIBLE);
                }
                else
                    check4.setVisibility (view.INVISIBLE);
            }
        });
    }


    //Se cambia el font de los textos
    public void changeFonts(View view){

        textView2 = view.findViewById (R.id.telTV);
        textView3 = view.findViewById (R.id.correoTV);
        textView4 = view.findViewById (R.id.contraseñaTV);
        textView5 = view.findViewById (R.id.confirmarTV);


        typeFace = Typeface.createFromAsset(getContext().getAssets(),"fonts/gotham_light.ttf");
        nextBttn.setTypeface(typeFace);
        nextBttn.setTypeface (typeFace);

        typeFace = Typeface.createFromAsset(getContext().getAssets(),"fonts/gotham_bold.ttf");
        textView2.setTypeface(typeFace);
        textView3.setTypeface(typeFace);
        textView4.setTypeface(typeFace);
        textView5.setTypeface(typeFace);


        typeFace = Typeface.createFromAsset(getContext().getAssets(),"fonts/courier.ttf");
        passwordET.setTypeface (typeFace);
        confirmET.setTypeface (typeFace);
        emailET.setTypeface (typeFace);
        telET.setTypeface (typeFace);


    }

    @Override
    public void onResume() {
        super.onResume();
        //Analytics
        mTracker.setScreenName("Register2Fragment");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }





}
