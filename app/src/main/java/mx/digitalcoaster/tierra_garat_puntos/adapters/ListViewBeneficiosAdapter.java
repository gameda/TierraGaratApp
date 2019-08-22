package mx.digitalcoaster.tierra_garat_puntos.adapters;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import 	android.graphics.Paint.Style;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mx.digitalcoaster.tierra_garat_puntos.R;
import mx.digitalcoaster.tierra_garat_puntos.models.Beneficio;

public class ListViewBeneficiosAdapter extends ArrayAdapter<Beneficio> implements ListAdapter {

    private Context context;
    int layoutResourceId;
    List <Beneficio> listaBeneficios;
    Typeface typeFace;
    TextView descripcion, titulo;
    View beneficioLL;

    //Constructor
    public ListViewBeneficiosAdapter(Context context, int position, List <Beneficio> beneficios){
        super(context, position, beneficios);
        this.context = context;
        this.layoutResourceId = position;
        this.listaBeneficios = beneficios;
    }

    //Identifica si ya existe un renglon sino lo crea
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;      //Obtiene las referencias a los objetos del renglón.

        //Obtiene las referencias a los objetos del renglón.
        if(row == null ){  //identifica si ya existe la vista del renglón, sino lo crea.
            LayoutInflater inflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE );
            row = inflater.inflate (layoutResourceId, parent, false);

        }

        beneficioLL = row.findViewById (R.id.beneficioView);
        titulo = row.findViewById (R.id.titleTV);
        descripcion = row.findViewById (R.id.descriptionTV);
        changeFonts (row);

        Beneficio beneficio = listaBeneficios.get (position);

        if(beneficio.getsType().equals("beneficio") && beneficio.getiUsed() == 0){
            titulo.setText (beneficio.getsNombre ());
            descripcion.setText (beneficio.getsDescripcion ());

        }
        else {
            beneficioLL.setVisibility(View.GONE);
        }

        return row;
    }


    public void changeFonts(View view){
        typeFace = Typeface.createFromAsset (context.getAssets (), "fonts/gotham_light.ttf");
        descripcion.setTypeface (typeFace);

        typeFace= Typeface.createFromAsset(context.getAssets(),"fonts/courier_bold.ttf");
        titulo.setTypeface (typeFace);

    }


}
