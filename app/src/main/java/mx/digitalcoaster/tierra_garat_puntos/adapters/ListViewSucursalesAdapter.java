package mx.digitalcoaster.tierra_garat_puntos.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.sql.Array;
import java.util.List;

import mx.digitalcoaster.tierra_garat_puntos.R;
import mx.digitalcoaster.tierra_garat_puntos.models.Beneficio;
import mx.digitalcoaster.tierra_garat_puntos.models.Sucursal;

public class ListViewSucursalesAdapter extends ArrayAdapter<Sucursal> {


    TextView sucursalTV, distanciaTV, direccionTV, estatusTV;
    private Context context;
    int layoutResourceId;
    List<Sucursal> listaSucursales;
    Typeface typeFace;


    //Constructor
    public ListViewSucursalesAdapter(Context context, int position, List <Sucursal> sucursales){
        super(context, position, sucursales);
        this.context = context;
        this.layoutResourceId = position;
        this.listaSucursales = sucursales;
    }

    //Identifica si ya existe un renglon sino lo crea
    @SuppressLint("SetTextI18n")
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;      //Obtiene las referencias a los objetos del renglón.

        //Obtiene las referencias a los objetos del renglón.

        if(row == null ){  //identifica si ya existe la vista del renglón, sino lo crea.
            LayoutInflater inflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE );
            row = inflater.inflate (layoutResourceId, parent, false);
        }

        sucursalTV = row.findViewById (R.id.sucursalTV);
        distanciaTV = row.findViewById (R.id.distanciaTV);
        direccionTV = row.findViewById (R.id.direccionTV);
        estatusTV = row.findViewById (R.id.estatusTV);
        changeFonts (convertView);


        Sucursal sucursal = listaSucursales.get (position);
        sucursalTV.setText (sucursal.getNombre ());
        distanciaTV.setText (String.valueOf (sucursal.getDistancia ()));
        direccionTV.setText (sucursal.getDireccion ());
        estatusTV.setText (sucursal.getEstatus ());
        convertorKilometros (sucursal);
        labelStatus(sucursal);

        return row;
    }

    public void changeFonts(View view){
        typeFace = Typeface.createFromAsset (context.getAssets (), "fonts/gotham_light.ttf");
        direccionTV.setTypeface (typeFace);
        estatusTV.setTypeface (typeFace);

        typeFace= Typeface.createFromAsset(context.getAssets(), "fonts/gotham_bold.ttf");
        sucursalTV.setTypeface (typeFace);
        distanciaTV.setTypeface (typeFace);
    }

    public void convertorKilometros (Sucursal sucursal){
        if(sucursal.getDistancia () < 999){
            distanciaTV.setText (String.valueOf (sucursal.getDistancia ()) + " \nmetros");

        }else{
            int iKm = sucursal.getDistancia () / 1000;
            distanciaTV.setText (String.valueOf (iKm) + "\nkm");
        }
    }

    public void labelStatus(Sucursal sucursal){
        if(sucursal.getEstatus ().equals ("ABIERTO")){
            estatusTV.setTextColor (Color.parseColor ("#1eb600"));
        }
        else if(sucursal.getEstatus ().equals ("¡PRÓXIMAMENTE!")){
            estatusTV.setTextColor (Color.parseColor ("#C97457"));
        }
        else{
            estatusTV.setTextColor (Color.parseColor ("#d60e0e"));

        }
    }

}
