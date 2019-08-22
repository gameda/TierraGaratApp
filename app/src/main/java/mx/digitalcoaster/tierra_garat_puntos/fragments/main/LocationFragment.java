package mx.digitalcoaster.tierra_garat_puntos.fragments.main;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import mx.digitalcoaster.tierra_garat_puntos.R;
import mx.digitalcoaster.tierra_garat_puntos.adapters.ListViewSucursalesAdapter;
import mx.digitalcoaster.tierra_garat_puntos.adapters.MyInfoWindowAdapter;
import mx.digitalcoaster.tierra_garat_puntos.interfaces.CleanMemory;
import mx.digitalcoaster.tierra_garat_puntos.models.Sucursal;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;
import static java.security.AccessController.getContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener,
        CleanMemory, GoogleMap.OnMarkerClickListener
{


    private static final String TAG = "LocationFragment";

    TextView text1, sucursalTV;
    Button showBttn, hideBttn;
    View locationView;
    LatLng currentLocation;
    List<Sucursal> lista;
    ListView sucursalLV;
    Date c = Calendar.getInstance().getTime();
    Context context ;
    LayoutInflater inflater;
    Boolean geolocalizacion;

    Marker mLondres, mHidalgo, mJalapa,mNL, mMasaryk, mAmsterdam, mAmberes, mReforma, mToreo, mTecno, mPalmas, mEugenia, mHegel, mParroquia;


    //Cordenadas de las diferentes sucursales
    private static final LatLng LONDRES = new LatLng (19.35419608033993, -99.16667945800401);
    private static final LatLng HIDALGO = new LatLng (19.349589, -99.160740);
    private static final LatLng JALAPA = new LatLng (19.418740, -99.161398);
    private static final LatLng NL = new LatLng (19.410169, -99.172600);
    private static final LatLng MASARYK = new LatLng (19.431186, -99.184698);
    private static final LatLng AMSTERDAM = new LatLng (19.410104, -99.169157);
    private static final LatLng AMBERES = new LatLng (19.425996, -99.165598);
    private static final LatLng REFORMA = new LatLng (19.429688, -99.163127);
    private static final LatLng TOREO = new LatLng (19.455478, -99.219020);
    private static final LatLng TECNO_PARQUE = new LatLng (19.503463, -99.178851);
    private static final LatLng PASEO_PALMAS = new LatLng (19.429102, -99.211237);
    private static final LatLng EUGENIA = new LatLng ( 19.388482, -99.171760);
    private static final LatLng HEGEL = new LatLng (19.434322, -99.187804);
    private static final LatLng PARROQUIA = new LatLng (19.368722, -99.167889);
    private static final LatLng RIO_ELBA = new LatLng (19.425148, -99.174813);
    private static final LatLng SAN_ISIDRO = new LatLng (19.433635, -99.212230);
    private static final LatLng SANTA_MONICA = new LatLng (19.389525, -99.173576);
    private static final LatLng INSURGENTES = new LatLng (19.359991, -99.183192);

    //Direcciones de sucursales
    private static final String sPalmas = "Paseo de las Palmas 340, Local 3, Colonia Lomas de Chapultepec III Sección, Miguel Hidalgo, C.P. 11000, Ciudad de México.";
    private static final String sLondres = "Londres 361, Col, del Carmen, Coyoacán, C.P. 04100, Ciudad de México.";
    private static final String sJalapa = "Jalapa 99-A, Local 4, Colonia Roma Norte, Cuauhtémoc, C.P. 06700, Ciudad de México.";
    private static final String sHidalgo = "Miguel Hidalgo 102, Local “A”, Colonia Del Carmen, Coyoacán, C.P. 04020, Ciudad de México.";
    private static final String sNL = "Nuevo León 122, Local “A”, Colonia Hipódromo, Cuauhtémoc, C.P. 06100, Ciudad de México.";
    private static final String sMasaryk = "Presidente Masaryk 86, Colonia Chapultepec Morales, Miguel Hidalgo, C.P. 11580, Ciudad de México.";
    private static final String sAmsterdam = "Ámsterdam 218, Local C, Colonia Hipódromo, Cuauhtémoc, C.P. 06100, Ciudad de México.";
    private static final String sAmberes = "Amberes 33, Local “A”, Colonia Juárez, Cuauhtémoc, C.P. 06600, Ciudad de México.";
    private static final String sReforma = "Paseo de la Reforma 231, Local 1, Colonia Cuauhtémoc, Cuauhtémoc, C.P. 06500, Ciudad de México.";
    private static final String sToreo ="Boulevard Manuel Ávila Camacho 5, Local 1K101, Fracc. Lomas de Sotelo, Naucalpan de Juárez, C.P. 53390, Estado de México.";
    private static final String sEugeniua = "Eugenia 302, Planta Baja, Colonia Del Valle Centro, Benito Juárez, C.P. 03100, Ciudad de México.";
    private static final String sHegel = "Hegel 232, Local “B”, Colonia Polanco V Sección, Miguel Hidalgo, C.P. 11320, Ciudad de México.";
    private static final String sParroquia = "Parroquia 815 Planta Baja, Colonia Del Valle Sur, Benito Juárez, C.P. 03100, Ciudad de México.";
    private static final String sTecnopaque ="Eje 5 Norte 990, Kiosko de la Plaza 1, Colonia Santa Bárbara, Azcapotzalco, C.P. 02230, Ciudad de México.";
    private static final String sRioElba ="Río Elba 50, Colonia Cuauhtémoc, Cuauhtémoc, C.P. 06500, Ciudad de México.";
    private static final String sSanI ="San Isidro 44, planta baja local 001. Colonia Reforma Social, Miguel Hidalgo, C.P. 11650, Ciudad de México";
    private static final String sSantaM ="Santa Mónica 3, Local 2, Planta Baja, Colonia Del Valle, Benito Juarez, C.P. 03100, Ciudad de México.";
    private static final String sInsurgentes ="Avenida de los Insurgentes 1722, Local A, Colonia Florida, Álvaro Obregón, C.P. 01030, Ciudad de México.";


    private GoogleMap mMap;                                 //Mapa inicial
    private static GoogleApiClient mGoogleApiClient;       //Variable para accesar al API
    LocationRequest mlocationRequest;                      //Variable para acceder a la localizacion
    SupportMapFragment mapFragment;
    MapView mapView;




    public LocationFragment() {
        //inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        // Required empty public constructor
    }

    /*@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated (savedInstanceState);
        try {
            MapsInitializer.initialize (getActivity ());
        } catch (Exception e) {
            mapsSupported = false;
        }

        if (mapView != null) {
            mapView.onCreate (savedInstanceState);
        }
        initializeMap ();
    }

    private void initializeMap() {
        if (mMap == null && mapsSupported) {
            mapView = getActivity().findViewById(R.id.mapView);
            mapView.getMapAsync (this);
            //setup markers etc...
        }
    }*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate (R.layout.fragment_location, container, false);


        //Inicialización del mapa
        if (view != null) {


            mapFragment = (SupportMapFragment) getChildFragmentManager ().findFragmentById (R.id.mapView);
            mapFragment.getMapAsync(this);


            text1 = view.findViewById (R.id.cdmxTV);
            sucursalTV = view.findViewById (R.id.sucursalTV);       //Lista de sucursales
            showBttn = view.findViewById (R.id.verBttn);
            hideBttn = view.findViewById (R.id.ocultarBttn);
            locationView = view.findViewById (R.id.locationView);
            sucursalLV = view.findViewById (R.id.sucursalLV);
            lista = getDataForListView ();




            changeFonts (view);

        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);


        statusTimeSucursal();
        View.OnClickListener listener = new View.OnClickListener (){
            //Se depliega la lista de sucursales
            @Override
            public void onClick(View view) {
                if(showBttn.isPressed ()){
                    locationView.setVisibility (View.VISIBLE);
                    locationView.bringToFront ();
                    showBttn.setVisibility (View.GONE);

                    //Obtiene distancia entre sucursal y tu posición
                    for (Sucursal sucursal : lista) {

                        float[] results = new float[1];
                        if(currentLocation != null) {
                            geolocalizacion = true;
                            Location.distanceBetween(
                                    currentLocation.latitude, currentLocation.longitude,
                                    sucursal.getCordenadas().latitude, sucursal.getCordenadas().longitude, results);
                            sucursal.setDistancia (Math.round(results[0]));

                        }
                        else {
                            geolocalizacion = false;
                            sucursal.setDistancia (0);
                        }

                    }

                    if (!geolocalizacion)
                        Toast.makeText (getActivity (), "Enciende tu ubicación para una mejor experiencia.", Toast.LENGTH_LONG).show ();


                    //Ordena las sucursales por distancia
                    Collections.sort(lista, (o1, o2) -> (o1.getDistancia ()) - (o2.getDistancia ()));

                    final ListViewSucursalesAdapter adapter = new ListViewSucursalesAdapter (getActivity (), R.layout.row_location, lista);
                    sucursalLV.setAdapter(adapter);
                    irSucursal (adapter,sucursalLV);

                    //Hora calculada de cada sucursal
                    statusTimeSucursal();

                }
                else if(hideBttn.isPressed ()){
                    locationView.setVisibility (View.GONE);
                    showBttn.setVisibility (View.VISIBLE);
                }
            }
        };
        showBttn.setOnClickListener(listener);
        hideBttn.setOnClickListener (listener);

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


    //Se crea la vista del Mapa
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        customiseMap ();



        // Builder to configure a GoogleApiClient
        mGoogleApiClient = new GoogleApiClient.Builder (getActivity ())
                .addApi (LocationServices.API)
                .addConnectionCallbacks (this)
                .addOnConnectionFailedListener (this)
                .build ();
        mGoogleApiClient.connect ();

        addMarkers ();
        //Cambia la ventana del marker
        MyInfoWindowAdapter customInfoWindow = new MyInfoWindowAdapter(getActivity(), lista);
        mMap.setInfoWindowAdapter(customInfoWindow);

        mMap.setOnMarkerClickListener(this);

        //Abre la indicaciones de google maps
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?&daddr=Tierra Garat " + marker.getTitle()));

                startActivity(intent);
            }
        });

        mMap.getUiSettings().setMapToolbarEnabled(false); //Desactiva botones auxiliares de map
        mMap.getUiSettings().setZoomControlsEnabled(true);

    }

    //Customisation del mapa
    public void customiseMap(){

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getActivity (), R.raw.map_style));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
    }


    //Se agregan las ubicaciones de las sucursales al mapa
    public void addMarkers() {

        mLondres = mMap.addMarker (new MarkerOptions ()
                .position (LONDRES)
                .title ("LONDRES")
                .snippet(sLondres)
                .icon (BitmapDescriptorFactory.fromResource(R.mipmap.tg))

        );

        mHidalgo = mMap.addMarker (new MarkerOptions ()
                .position (HIDALGO)
                .title ("MIGUEL HIDALGO")
                .snippet(sHidalgo)
                .icon (BitmapDescriptorFactory.fromResource(R.mipmap.tg))
        );
        mJalapa = mMap.addMarker (new MarkerOptions ()
                .position (JALAPA)
                .title ("JALAPA")
                .snippet(sJalapa)
                .icon (BitmapDescriptorFactory.fromResource(R.mipmap.tg))
        );

        mNL = mMap.addMarker (new MarkerOptions ()
                .position (NL)
                .title ("NUEVO LEÓN")
                .snippet(sNL)
                .icon (BitmapDescriptorFactory.fromResource(R.mipmap.tg))
        );
        mMasaryk = mMap.addMarker (new MarkerOptions ()
                .position (MASARYK)
                .title ("MASARYK")
                .snippet(sMasaryk)
                .icon (BitmapDescriptorFactory.fromResource(R.mipmap.tg))
        );

        mAmsterdam = mMap.addMarker (new MarkerOptions ()
                .position (AMSTERDAM)
                .title ("ÁMSTERDAM")
                .snippet(sAmsterdam)
                .icon (BitmapDescriptorFactory.fromResource(R.mipmap.tg))
        );
        mAmberes = mMap.addMarker (new MarkerOptions ()
                .position (AMBERES)
                .title ("AMBERES")
                .snippet(sAmberes)
                .icon (BitmapDescriptorFactory.fromResource(R.mipmap.tg))
        );

        mReforma = mMap.addMarker (new MarkerOptions ()
                .position (REFORMA)
                .title ("REFORMA")
                .snippet(sReforma)
                .icon (BitmapDescriptorFactory.fromResource(R.mipmap.tg))
        );
        mToreo = mMap.addMarker (new MarkerOptions ()
                .position (TOREO)
                .title ("TOREO")
                .snippet(sToreo)
                .icon (BitmapDescriptorFactory.fromResource(R.mipmap.tg))
        );
        mTecno = mMap.addMarker (new MarkerOptions ()
                .position (TECNO_PARQUE)
                .title ("TECNO PARQUE")
                .snippet(sTecnopaque)
                .icon (BitmapDescriptorFactory.fromResource(R.mipmap.tg))
        );
        mPalmas = mMap.addMarker (new MarkerOptions ()
                .position (PASEO_PALMAS)
                .title ("PALMAS")
                .snippet(sPalmas)
                .icon (BitmapDescriptorFactory.fromResource(R.mipmap.tg))
        );

        mEugenia = mMap.addMarker (new MarkerOptions ()
                .position (EUGENIA)
                .title ("EUGENIA")
                .snippet(sEugeniua)
                .icon (BitmapDescriptorFactory.fromResource(R.mipmap.tg))
        );

        mHegel = mMap.addMarker (new MarkerOptions ()
                .position (HEGEL)
                .title ("HEGEL")
                .snippet(sHegel)
                .icon (BitmapDescriptorFactory.fromResource(R.mipmap.tg))
        );

        mParroquia = mMap.addMarker (new MarkerOptions ()
                .position (PARROQUIA)
                .title ("PARROQUIA")
                .snippet(sParroquia)
                .icon (BitmapDescriptorFactory.fromResource(R.mipmap.tg))
        );

        mParroquia = mMap.addMarker (new MarkerOptions ()
                .position (RIO_ELBA)
                .title ("RÍO ELBA")
                .snippet(sRioElba)
                .icon (BitmapDescriptorFactory.fromResource(R.mipmap.tg))
        );

        mParroquia = mMap.addMarker (new MarkerOptions ()
                .position (SAN_ISIDRO)
                .title ("SAN ISIDRO")
                .snippet(sSanI)
                .icon (BitmapDescriptorFactory.fromResource(R.mipmap.tg))
        );

        mParroquia = mMap.addMarker (new MarkerOptions ()
                .position (SANTA_MONICA)
                .title ("SANTA MÓNICA")
                .snippet(sSantaM)
                .icon (BitmapDescriptorFactory.fromResource(R.mipmap.tg))
        );

        mParroquia = mMap.addMarker (new MarkerOptions ()
                .position (INSURGENTES)
                .title ("INSURGENTE 1722")
                .snippet(sInsurgentes)
                .icon (BitmapDescriptorFactory.fromResource(R.mipmap.tg))
        );


        /*mLondres.showInfoWindow();
        mHidalgo.showInfoWindow();
        mJalapa.showInfoWindow();
        mNL.showInfoWindow();
        mMasaryk.showInfoWindow();
        mAmsterdam.showInfoWindow();
        mAmberes.showInfoWindow();
        mReforma.showInfoWindow();
        mToreo.showInfoWindow();
        mTecno.showInfoWindow();
        mEugenia.showInfoWindow();
        mHegel.showInfoWindow();*/


    }


    //Se obtiene mi localizacion en tiempo real
    @Override
    public void onConnected(Bundle bundle) {
        mlocationRequest = LocationRequest.create ();
        mlocationRequest.setPriority (LocationRequest.PRIORITY_HIGH_ACCURACY);


        if (Build.VERSION.SDK_INT >= 23 &&
                ActivityCompat.checkSelfPermission ( getContext (), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission (getContext (), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]
                    {android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION},  REQUEST_CODE);
            //return;
        }
        else {
            Log.e("DB", "PERMISSION GRANTED");
        }

        LocationServices.FusedLocationApi.requestLocationUpdates (mGoogleApiClient, mlocationRequest, this);
        mMap.setMyLocationEnabled (true);

    }

    //Cuando se cambia la ubicacion de usuario en tiempo real
    @Override
    public void onLocationChanged(Location location) {
        if(location == null ){ //En caso de no tener el GPS prendido
            Toast.makeText (getActivity (), "No se puede encontrar tu ubicación", Toast.LENGTH_SHORT).show ();

        }else { //En caso de encontrar ubicacion del usuario

            LatLng latLng = new LatLng (location.getLatitude (), location.getLongitude ());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom (latLng, 15);
            mMap.animateCamera (update);
            currentLocation = latLng;
        }
    }

    //Metodos que NO se usan
    @Override
    public void onConnectionSuspended(int i) {
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }




    //Funcion para cambiar el formato de imagen Vector a Bitmap
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    public List<Sucursal> getDataForListView() {
        Sucursal sucursal;

        List<Sucursal> listSucursal = new ArrayList<Sucursal> ();

        sucursal = new Sucursal(
                "JALAPA",
                sJalapa,
                "08:00", "22:30",
                JALAPA, 0,
                "abierto");
        listSucursal.add (sucursal);

        sucursal = new Sucursal(
                "REFORMA",
                sReforma,
                "06:30", "22:00",
                REFORMA, 0,
                "abierto");
        listSucursal.add (sucursal);

        sucursal = new Sucursal(
                "TOREO",
                sToreo,
                "08:00", "21:30",
                TOREO, 0,
                "abierto");
        listSucursal.add (sucursal);

        sucursal = new Sucursal(
                "AMBERES",
                sAmberes,
                "07:00", "22:00",
                AMBERES, 0,
                "abierto");
        listSucursal.add (sucursal);

        sucursal = new Sucursal(
                "MASARYK",
                sMasaryk,
                "07:00", "22:00",
                MASARYK, 0,
                "abierto");
        listSucursal.add (sucursal);


        sucursal = new Sucursal(
                "ÁMSTERDAM",
                sAmsterdam,
                "07:00", "22:00",
                AMSTERDAM, 0,
                "abierto");
        listSucursal.add (sucursal);

        sucursal = new Sucursal(
                "TECNO PARQUE",
                sTecnopaque,
                "08:00", "21:00",
                TECNO_PARQUE, 0,
                "abierto");
        listSucursal.add (sucursal);

        sucursal = new Sucursal(
                "LONDRES",
                sLondres,
                "07:00", "22:30",
                LONDRES, 0,
                "abierto");
        listSucursal.add (sucursal);

        sucursal = new Sucursal(
                "NUEVO LEÓN",
                sNL,
                "07:00", "22:30",
                NL, 0,
                "abierto");
        listSucursal.add (sucursal);

        sucursal = new Sucursal(
                "MIGUEL HIDALGO",
                sHidalgo,
                "07:00", "22:30",
                HIDALGO, 0,
                "abierto");
        listSucursal.add (sucursal);

        sucursal = new Sucursal(
                "PALMAS",
                sPalmas,
                "07:00", "22:00",
                PASEO_PALMAS, 0,
                "abierto");
        listSucursal.add (sucursal);

        sucursal = new Sucursal(
                "EUGENIA",
                sEugeniua,
                "07:00", "22:00",
                EUGENIA, 0,
                "abierto");
        listSucursal.add (sucursal);
        sucursal = new Sucursal(
                "HEGEL",
                sHegel,
                "07:00", "22:00",
                HEGEL, 0,
                "abierto");
        listSucursal.add (sucursal);
        sucursal = new Sucursal(
                "PARROQUIA",
                sParroquia,
                "07:00", "22:00",
                PARROQUIA, 0,
                "abierto");
        listSucursal.add (sucursal);
        sucursal = new Sucursal(
                "RÍO ELBA",
                sRioElba,
                "", "",
                RIO_ELBA, 0,
                "¡PRÓXIMAMENTE!");
        listSucursal.add (sucursal);

        sucursal = new Sucursal(
                "SAN ISIDRO",
                sSanI,
                "07:00", "22:00",
                SAN_ISIDRO, 0,
                "abierto");
        listSucursal.add (sucursal);

        sucursal = new Sucursal(
                "SANTA MÓNICA",
                sSantaM,
                "", "",
                SANTA_MONICA, 0,
                "¡PRÓXIMAMENTE!");
        listSucursal.add (sucursal);

        sucursal = new Sucursal(
                "INSURGENTES 1722",
                sInsurgentes,
                "07:00", "22:00",
                INSURGENTES, 0,
                "abierto");
        listSucursal.add (sucursal);


        Collections.sort(listSucursal, new Comparator<Sucursal>() {
            @Override
            public int compare(Sucursal o1, Sucursal o2) {
                return o1.getDistancia () - o2.getDistancia ();
            }

        });

        return listSucursal;

    }

    //Te redirecciona la camara a la sucursal deseada
    public void irSucursal(ListViewSucursalesAdapter adapter, ListView sucursalesLV){
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                LatLng cordenadas;
                Sucursal sucursal = adapter.getItem(position);
                cordenadas = sucursal.getCordenadas ();
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom (cordenadas, 15);
                mMap.animateCamera (update);
                locationView.setVisibility (View.GONE);
                showBttn.setVisibility (View.VISIBLE);
                sucursalTV.setText ("TIERRA GARAT " + sucursal.getNombre ());


            }
        };
        sucursalesLV.setOnItemClickListener(itemListener);
    }



    //Se cambian las fuentes
    public void changeFonts(View view){
        Typeface typeFace = Typeface.createFromAsset (getActivity ().getAssets (), "fonts/gotham_light.ttf");
        text1.setTypeface (typeFace);

        typeFace= Typeface.createFromAsset(getActivity ().getAssets(),"fonts/courier.ttf");
        sucursalTV.setTypeface (typeFace);
        showBttn.setTypeface (typeFace);
        hideBttn.setTypeface (typeFace);

    }


    //Calcula las horas de abierto y cerrado para cada sucursal
    public  void statusTimeSucursal(){

        Date currentTime = Calendar.getInstance().getTime();
        Date abreTime = Calendar.getInstance().getTime();
        Date cierraTime = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String sTimeFormat = sdf.format(c.getTime());
        String sAbre, sCierra;

        //Obtiene distancia la hora de abierto y cerrado y compara con current time
        for (Sucursal sucursal : lista) {
            sAbre = sucursal.getAbre ();
            sCierra = sucursal.getCierra ();

            try {
                currentTime = sdf.parse(sTimeFormat);
                abreTime = sdf. parse (sAbre);
                cierraTime = sdf.parse(sCierra);


                if(sAbre.isEmpty()){
                    sucursal.setEstatus ("PRÓXIMAMENTE");

                }
                else if(currentTime.after(abreTime) && currentTime.before (cierraTime)){
                    sucursal.setEstatus ("ABIERTO");
                }
                else{
                    sucursal.setEstatus ("CERRADO");
                }

            } catch (ParseException e) {
                e.printStackTrace ();
            }

        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView ();
    }

    @Override
    public void cleanMemory() {
        text1 = null;
        sucursalTV = null;
        showBttn = null;
        hideBttn = null;
        currentLocation = null;
        lista = null;
        c = null;
        mMap =  null;
        mGoogleApiClient = null;
        mlocationRequest = null;
        mapFragment = null;
        showBttn.setOnClickListener(null);
        hideBttn.setOnClickListener (null);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        sucursalTV.setText ("TIERRA GARAT " + marker.getTitle());
        marker.showInfoWindow();
        return false;


    }


    /*@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory ();
        mapView.onLowMemory ();
    }*/


    /*
    private Bundle savedState;
    private boolean saved;
    private static final String _FRAGMENT_STATE = "FRAGMENT_STATE";

    @Override
    public void onSaveInstanceState(Bundle state) {
        if (getView() == null) {
            state.putBundle(_FRAGMENT_STATE, savedState);
        } else {
            Bundle bundle = saved ? savedState : getStateToSave();

            state.putBundle(_FRAGMENT_STATE, bundle);
        }

        saved = false;

        super.onSaveInstanceState(state);
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        if (state != null) {
            savedState = state.getBundle(_FRAGMENT_STATE);
        }
    }

    @Override
    public void onDestroyView() {
        savedState = getStateToSave();
        saved = true;

        super.onDestroyView();
    }

    protected Bundle getSavedState() {
        return savedState;
    }

    protected boolean hasSavedState() {
        return false;
    }

    protected Bundle getStateToSave() {
        return null;
    }*/


}
