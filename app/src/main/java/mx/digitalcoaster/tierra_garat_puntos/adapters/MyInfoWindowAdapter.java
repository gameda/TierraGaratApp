package mx.digitalcoaster.tierra_garat_puntos.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.List;
import java.util.zip.Inflater;

import mx.digitalcoaster.tierra_garat_puntos.R;
import mx.digitalcoaster.tierra_garat_puntos.fragments.main.LocationFragment;
import mx.digitalcoaster.tierra_garat_puntos.models.Sucursal;

public class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Context context;
    List<Sucursal> lista;


    public MyInfoWindowAdapter(Context context, List<Sucursal> lista){
        this.context = context;
        this.lista = lista;

    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View v = ((Activity)context).getLayoutInflater().inflate(R.layout.window_marker, null);

        TextView title = v.findViewById(R.id.nameTV);
        title.setText(marker.getTitle());

        TextView address = v.findViewById(R.id.addressTV);
        address.setText(marker.getSnippet());

        TextView status= v.findViewById(R.id.statusTV);


        for(Sucursal sucursal : lista){
            if(sucursal.getNombre().matches(marker.getTitle())){
                status.setText(sucursal.getEstatus());
            }

        }

        Button irBttn = v.findViewById(R.id.irButton);


        Typeface typeFace = Typeface.createFromAsset (context.getAssets (), "fonts/gotham_light.ttf");
        address.setTypeface (typeFace);

        typeFace = Typeface.createFromAsset (context.getAssets (), "fonts/gotham_bold.ttf");
        status.setTypeface(typeFace);

        typeFace = Typeface.createFromAsset (context.getAssets (), "fonts/courier.ttf");
        title.setTypeface(typeFace);
        irBttn.setTypeface(typeFace);

        if(status.getText().toString().equals("ABIERTO")){
            status.setTextColor (Color.parseColor ("#1eb600"));
        }
        else if(status.getText().toString().equals("¡PRÓXIMAMENTE!")){
            status.setTextColor (Color.parseColor ("#C97457"));
        }
        else{
            status.setTextColor (Color.parseColor ("#d60e0e"));

        }

       /* irBttn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + currentLocation.latitude + "," + currentLocation.longitude +
                        "?q=" + marker.getPosition().latitude + "," + marker.getPosition().longitude + marker.getTitle()));
                startActivity(intent);
            }
        });*/

        return v;
    }
}

/*  //Cutomize marker window
    @Override
    public View getInfoWindow(Marker marker) {
        inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View v = inflater.inflate(R.layout.window_marker, null);

        TextView title = v.findViewById(R.id.nameTV);
        title.setText(marker.getTitle());

        TextView address = v.findViewById(R.id.addressTV);
        address.setText(marker.getSnippet());

        TextView status= v.findViewById(R.id.statusTV);
        status.setText(sucursalStatus(marker.getTitle()));

        Button irBttn = v.findViewById(R.id.irButton);


        Typeface typeFace = Typeface.createFromAsset (getActivity ().getAssets (), "fonts/gotham_light.ttf");
        address.setTypeface (typeFace);

        typeFace = Typeface.createFromAsset (getActivity ().getAssets (), "fonts/courier_bold.ttf");
        title.setTypeface(typeFace);
        status.setTypeface(typeFace);
        irBttn.setTypeface(typeFace);

        if(status.getText().toString().matches("ABIERTO")){
            status.setTextColor (Color.parseColor ("#1eb600"));
        }
        else{
            status.setTextColor (Color.parseColor ("#d60e0e"));

        }

        irBttn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + currentLocation.latitude + "," + currentLocation.longitude +
                        "?q=" + marker.getPosition().latitude + "," + marker.getPosition().longitude + marker.getTitle()));
                startActivity(intent);
            }
        });

        return v;

    }



    @Override
    public View getInfoContents(Marker marker) {
        inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View v = inflater.inflate(R.layout.window_marker, null);

        TextView title = v.findViewById(R.id.nameTV);
        title.setText(marker.getTitle());

        TextView address = v.findViewById(R.id.addressTV);
        address.setText(marker.getSnippet());

        TextView status= v.findViewById(R.id.statusTV);
        status.setText(sucursalStatus(marker.getTitle()));

        Button irBttn = v.findViewById(R.id.irButton);


        Typeface typeFace = Typeface.createFromAsset (getActivity ().getAssets (), "fonts/gotham_light.ttf");
        address.setTypeface (typeFace);

        typeFace = Typeface.createFromAsset (getActivity ().getAssets (), "fonts/courier_bold.ttf");
        title.setTypeface(typeFace);
        status.setTypeface(typeFace);
        irBttn.setTypeface(typeFace);

        if(status.getText().toString().matches("ABIERTO")){
            status.setTextColor (Color.parseColor ("#1eb600"));
        }
        else{
            status.setTextColor (Color.parseColor ("#d60e0e"));

        }

        irBttn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + currentLocation.latitude + "," + currentLocation.longitude +
                        "?q=" + marker.getPosition().latitude + "," + marker.getPosition().longitude + marker.getTitle()));
                startActivity(intent);
            }
        });

        return v;
    }*/
