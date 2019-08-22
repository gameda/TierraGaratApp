package mx.digitalcoaster.tierra_garat_puntos.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.MediaRouteButton;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.chabbal.slidingdotsplash.SlidingSplashView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import mx.digitalcoaster.tierra_garat_puntos.R;
import mx.digitalcoaster.tierra_garat_puntos.adapters.FragmentAdapter;
import mx.digitalcoaster.tierra_garat_puntos.adapters.ImageAdapter;
import mx.digitalcoaster.tierra_garat_puntos.fragments.welcome.Welcome1Fragment;
import mx.digitalcoaster.tierra_garat_puntos.fragments.welcome.Welcome2Fragment;
import mx.digitalcoaster.tierra_garat_puntos.fragments.welcome.Welcome3Fragment;
import mx.digitalcoaster.tierra_garat_puntos.helpers.AnalyticsApplication;
import mx.digitalcoaster.tierra_garat_puntos.helpers.OnSwipeTouchListener;
import mx.digitalcoaster.tierra_garat_puntos.interfaces.CleanMemory;
import mx.digitalcoaster.tierra_garat_puntos.preferences.CustomTypefaceSpan;

import static com.facebook.FacebookSdk.getApplicationContext;


public class WelcomeActivity extends AppCompatActivity implements CleanMemory {


    private static final String TAG = "WelcomeActivity";

    private ViewPager containerViewPager;
    String llave;
    Context context;
    Typeface typeface;
    Button siguienteBttn;
    Typeface typeFace;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.2F);
    TextView fraseTV, tituloTV;
    FragmentAdapter adapter =new FragmentAdapter (getSupportFragmentManager ());
    SpannableStringBuilder spanString;

       private Tracker mTracker;        //Analytics


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        //Analytics
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();



        firstTime ();
        setContentView (R.layout.activity_welcome);
        siguienteBttn = findViewById (R.id.nextBttn);

        fraseTV = findViewById(R.id.fraseTV);
        tituloTV = findViewById(R.id.tituloTV);

        //Bind between View and LayOut
        containerViewPager = findViewById (R.id.containterViewPager);

        ImageAdapter adapter = new ImageAdapter(this);
        containerViewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(containerViewPager, true);



        ////setup the pager
        //setupViewPager (containerViewPager);
        changeFonts ();
        changeViewPosition();
        //swipeAction();

        View.OnClickListener listener = new View.OnClickListener (){

            @Override
            public void onClick(View view) {
                if (siguienteBttn.isPressed ()) {
                    view.startAnimation (buttonClick);
                    Intent intent = new Intent (WelcomeActivity.this, AccessActivity.class);
                    startActivity (intent);

                }
            }
        };
        siguienteBttn.setOnClickListener(listener);


    }


    //Fragments are added to a list
    private void setupViewPager(ViewPager viewPager){
        adapter.addFragment (new Welcome1Fragment (),"Welcome1Fragment" );
        adapter.addFragment (new Welcome3Fragment (),"Welcome3Fragment" );
        adapter.addFragment (new Welcome2Fragment (),"Welcome2Fragment" );
        viewPager.setAdapter (adapter);
        viewPager.getAdapter ().notifyDataSetChanged ();

    }


    public void changeViewPosition(){

        Typeface gotham_light = Typeface.createFromAsset(this.getAssets(),"fonts/gotham_light.ttf");
        Typeface gotham_bold = Typeface.createFromAsset(this.getAssets(),"fonts/gotham_bold.ttf");

        SpannableStringBuilder spanString = new SpannableStringBuilder("ACUMULA\nVISITAS.");
        spanString.setSpan(new CustomTypefaceSpan("", gotham_light), 0, 7,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        spanString.setSpan(new CustomTypefaceSpan ("", gotham_bold), 7, 15,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        tituloTV.setText (spanString);
        tituloTV.setTextColor(getApplicationContext().getResources().getColor(R.color.cafe2));

        spanString = new SpannableStringBuilder("Y obtén beneficios\nespeciales.");
        spanString.setSpan(new CustomTypefaceSpan("", gotham_light), 0, 7,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        spanString.setSpan(new CustomTypefaceSpan ("", gotham_bold), 7, 18,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        spanString.setSpan(new CustomTypefaceSpan("", gotham_light), 18, 29,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        fraseTV.setText (spanString);
        fraseTV.setTextColor(getApplicationContext().getResources().getColor(R.color.cafe2));

        containerViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if(position == 0){
                    SpannableStringBuilder spanString = new SpannableStringBuilder("ACUMULA\nVISITAS.");
                    spanString.setSpan(new CustomTypefaceSpan("", gotham_light), 0, 7,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    spanString.setSpan(new CustomTypefaceSpan ("", gotham_bold), 7, 16,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    tituloTV.setText (spanString);
                    tituloTV.setTextColor(getApplicationContext().getResources().getColor(R.color.cafe2));

                    spanString = new SpannableStringBuilder("Y obtén beneficios\nespeciales.");
                    spanString.setSpan(new CustomTypefaceSpan("", gotham_light), 0, 7,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    spanString.setSpan(new CustomTypefaceSpan ("", gotham_bold), 7, 18,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    spanString.setSpan(new CustomTypefaceSpan("", gotham_light), 18, 30,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    fraseTV.setText (spanString);
                    fraseTV.setTextColor(getApplicationContext().getResources().getColor(R.color.cafe2));


                }
                else if(position == 1) {
                    SpannableStringBuilder spanString = new SpannableStringBuilder("OBTÉN UNA\nBEBIDA GRATIS.");
                    spanString.setSpan(new CustomTypefaceSpan("", gotham_light), 0, 9,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    spanString.setSpan(new CustomTypefaceSpan ("", gotham_bold), 9, 24,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    tituloTV.setText (spanString);
                    tituloTV.setTextColor(getApplicationContext().getResources().getColor(R.color.carne));

                    spanString = new SpannableStringBuilder("Al acumular\n6 visitas.");
                    spanString.setSpan(new CustomTypefaceSpan("", gotham_light), 0, 11,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    spanString.setSpan(new CustomTypefaceSpan ("", gotham_bold), 11, 22,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    fraseTV.setText (spanString);
                    fraseTV.setTextColor(getApplicationContext().getResources().getColor(R.color.cafe2));


                }
                else if(position == 2) {
                    SpannableStringBuilder spanString = new SpannableStringBuilder("UBICA\nSUCURSALES.");
                    spanString.setSpan(new CustomTypefaceSpan("", gotham_light), 0, 5,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    spanString.setSpan(new CustomTypefaceSpan ("", gotham_bold), 5, 17,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    tituloTV.setText (spanString);
                    tituloTV.setTextColor(getApplicationContext().getResources().getColor(R.color.carne));
                    fraseTV.setText ("");

                    spanString = new SpannableStringBuilder("Encuentra la más\ncerca de ti.");
                    spanString.setSpan(new CustomTypefaceSpan("", gotham_light), 0, 16,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    spanString.setSpan(new CustomTypefaceSpan ("", gotham_bold), 16, 29,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    fraseTV.setText (spanString);
                    fraseTV.setTextColor(getApplicationContext().getResources().getColor(R.color.cafe2));


                }
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });




       /* containerViewPager.addOnAdapterChangeListener(new ViewPager.OnAdapterChangeListener() {

            @Override
            public void onAdapterChanged(ViewPager viewPager, PagerAdapter pagerAdapter, PagerAdapter pagerAdapter1) {
                int position = viewPager.getCurrentItem();

                if(position == 0){
                    fraseTV.setText("1");
                }
                else if(position == 1) {
                    fraseTV.setText("2");
                }
                else if(position == 2) {
                    fraseTV.setText("3");
                }

            }
        });*/

    }

    public void setViewPager(int iPosition) {
        containerViewPager.setCurrentItem (iPosition);
    }

    //Se cambia el font de los textos
    public void changeFonts(){
        typeFace = Typeface.createFromAsset(this.getAssets(),"fonts/gotham_light.ttf");
        siguienteBttn.setTypeface (typeFace);
    }

    public void firstTime(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = prefs.getBoolean(getString(R.string.pref_previously_started), false);
        if(!previouslyStarted) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(getString(R.string.pref_previously_started), Boolean.TRUE);
            edit.apply();

        }else {
            Intent intent;
            intent = new Intent (WelcomeActivity.this, AccessActivity.class);
            startActivity (intent);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //Analytics
        mTracker.setScreenName("WelcomeActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy ();
    }

    @Override
    public void cleanMemory() {
        containerViewPager = null;
    }


}
