package mx.digitalcoaster.tierra_garat_puntos.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mx.digitalcoaster.tierra_garat_puntos.R;
import mx.digitalcoaster.tierra_garat_puntos.models.Producto;

public class ExpandableListAdapter extends BaseExpandableListAdapter {



  private List<String> itemList;
  private HashMap<String, List<Producto>> productoList;
  private Activity activity;
  Context context;
  Typeface typeFace;

  LinearLayout margenLL, productoLL;
  TextView productoTV, chicoTV, medTV, grandeTV, subcategoriaTV, chTV, mTV, gTV, categoriaTV;
  ImageView frioIV, calienteIV, frapeIV;
  ImageView dotLine;

  SpannableString string = new SpannableString("Text with absolute size span");
  //string.setSpan(new AbsoluteSizeSpan(55, true), 10, 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);



  public ExpandableListAdapter (Context context, List<String> itemList, HashMap<String, List<Producto>> childItem){
    this.itemList = itemList;
    this.productoList = childItem;
    this.context = context;
  }

  @Override
  public Object getChild(int groupPosition, int childPosition) {
    return productoList.get(itemList.get(groupPosition)).get(childPosition);
  }
  @Override
  public long getChildId(int groupPosition, int childPosition) {
    return childPosition;
  }
  @Override
  public int getChildrenCount(int groupPosition) {
    return this.productoList.get(this.itemList.get(groupPosition)).size();
  }

  @Override
  public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
    Producto producto = (Producto) getChild(groupPosition, childPosition);

    if (convertView == null) {
      LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      convertView = infalInflater.inflate(R.layout.row_child, parent,false);
    }

    margenLL = convertView.findViewById (R.id.margenLL);
    productoLL = convertView.findViewById (R.id.productoLL);

    productoTV =  convertView.findViewById(R.id.productoTV);
    chicoTV =  convertView.findViewById(R.id.chicoTV);
    medTV =  convertView.findViewById(R.id.medianoTV);
    grandeTV =  convertView.findViewById(R.id.grandeTV);
    frioIV =  convertView.findViewById(R.id.frio);
    calienteIV =  convertView.findViewById(R.id.caliente);
    frapeIV =  convertView.findViewById(R.id.frape);
    dotLine = convertView.findViewById (R.id.dotV);


    subcategoriaTV = convertView.findViewById (R.id.subcategoriaTV);
    chTV = convertView.findViewById (R.id.smallTV);
    mTV = convertView.findViewById (R.id.mediTV);
    gTV = convertView.findViewById (R.id.bigTV);
    changeFonts ();




    //Verifica cuando es margen o producto
    if(producto.getbMargen ()){
      margenLL.setVisibility (View.VISIBLE);
      productoLL.setVisibility (View.GONE);
      dotLine.setVisibility (View.GONE);

      if(producto.getNombre ().isEmpty ()) {
        subcategoriaTV.setVisibility (View.GONE);
        sizeTAG(producto);
      }

      else {
        subcategoriaTV.setText (producto.getNombre ());
        sizeTAG (producto);
      }

    }else{
      margenLL.setVisibility (View.GONE);
      productoLL.setVisibility (View.VISIBLE);
      dotLine.setVisibility (View.VISIBLE);

      productoTV.setText(resizeText(producto));
      sizePrice (producto);

    }

    //verifica los tipos de bebidas
    //Bebidas calientes y frias
    if(producto.getbCaliente ()){
      calienteIV.setVisibility (View.VISIBLE);
      frioIV.setVisibility (View.INVISIBLE);
      frapeIV.setVisibility (View.GONE);

      if(producto.getbFrio ()){
        frioIV.setVisibility (View.VISIBLE);
      }

      //Solo bebidas frias
    } else if(producto.getbFrio()){
      calienteIV.setVisibility (View.INVISIBLE);
      frioIV.setVisibility (View.VISIBLE);
      frapeIV.setVisibility (View.GONE);

      //Sin tipo de bebida
    } else {
      calienteIV.setVisibility (View.GONE);
      frioIV.setVisibility (View.GONE);
      frapeIV.setVisibility(View.GONE);
    }

    //Solo bebidas frape
    if(producto.getbFrape ()){
      calienteIV.setVisibility (View.INVISIBLE);
      frioIV.setVisibility (View.GONE);
      frapeIV.setVisibility (View.VISIBLE);
    }

    return convertView;
  }

  @Override
  public Object getGroup(int groupPosition) {
    return itemList.get(groupPosition);
  }
  @Override
  public int getGroupCount() {
    return itemList.size ();
  }

  @Override
  public long getGroupId(int groupPosition) {
    return groupPosition;
  }


  @Override
  public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

    String headerTitle = itemList.get(groupPosition);
    if (convertView == null) {
      LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      convertView = infalInflater.inflate(R.layout.row_item, parent, false);
    }
    categoriaTV = convertView.findViewById(R.id.categoriaTV);


    //categoriaTV.setTypeface(null, Typeface.BOLD);
    Typeface typeFace2= Typeface.createFromAsset(context.getAssets(),"fonts/courier.ttf");
    categoriaTV.setTypeface(typeFace2);
    categoriaTV.setText(headerTitle);
    return convertView;
  }

  @Override
  public boolean hasStableIds() {
    return false;
  }
  @Override
  public boolean isChildSelectable(int groupPosition, int childPosition) {
    return true;
  }


  private void sizePrice(Producto producto){
    if(producto.getbTamaños ()){
      //En caso con contar varios tamaños
      chicoTV.setText (producto.getPrecioChico ());
      medTV.setText (producto.getPrecioMediano ());
      grandeTV.setText (producto.getPrecioGrande ());
    }else {
      //Un solo tamaño
      chicoTV.setText (producto.getPrecioChico ());
      medTV.setText (producto.getPrecioMediano ());
      grandeTV.setText (producto.getPrecioGrande ());
    }
  }

  public void sizeTAG(Producto producto){
    if(!producto.getbTamaños ()){
      chTV.setVisibility (View.GONE);
      mTV.setVisibility (View.GONE);
      gTV.setVisibility (View.GONE);
    }

  }

  public void changeFonts(){
    typeFace = Typeface.createFromAsset (context.getAssets (), "fonts/gotham_light_regular.otf");
    productoTV.setTypeface(typeFace);

    typeFace = Typeface.createFromAsset (context.getAssets (), "fonts/gotham_bold.ttf");
    subcategoriaTV.setTypeface(typeFace);

    typeFace= Typeface.createFromAsset(context.getAssets(),"fonts/courier_bold.ttf");
    chicoTV.setTypeface(typeFace);
    medTV.setTypeface(typeFace);
    grandeTV.setTypeface(typeFace);
    chTV.setTypeface(typeFace);
    mTV.setTypeface(typeFace);
    gTV.setTypeface(typeFace);


  }

  public SpannableString resizeText(Producto producto){
    String description = producto.getNombre();
    SpannableString string = new SpannableString(description);
    if(description.contains("(")){
      int iPos = description.indexOf("(");
      int iPos2 = description.indexOf(")");
      string.setSpan(new AbsoluteSizeSpan(10, true), iPos, iPos2+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    return string;
  }



}
