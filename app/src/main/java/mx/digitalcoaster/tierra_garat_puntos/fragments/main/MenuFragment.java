package mx.digitalcoaster.tierra_garat_puntos.fragments.main;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mx.digitalcoaster.tierra_garat_puntos.R;
import mx.digitalcoaster.tierra_garat_puntos.activities.MainActivity;
import mx.digitalcoaster.tierra_garat_puntos.adapters.ExpandableListAdapter;
import mx.digitalcoaster.tierra_garat_puntos.models.Producto;
import mx.digitalcoaster.tierra_garat_puntos.preferences.PreferenceUtils;


public class MenuFragment extends Fragment implements ExpandableListView.OnChildClickListener {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> expandableListTitle;
    HashMap<String, List<Producto>> productList;
    List<String> itemList = new ArrayList<>();
    List<Object> productoList = new ArrayList<>();
    String  menu;
    String  beneficios;


    TextView menuTV;
    PDFView pdfView;     //Variable PDF


    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate (R.layout.fragment_menu, container, false);
        if(view != null){

            expListView = view.findViewById(R.id.lvExp);
            menuTV = view.findViewById (R.id.text2);
            changeFonts (view);
            //expListView.setDividerHeight(2);
            //expListView.setGroupIndicator(null);
            //expListView.setClickable(true);

            setGroupData ();
            setChildGroupData ();

            listAdapter = new ExpandableListAdapter (getActivity (), itemList, productList);
            expListView.setAdapter (listAdapter);

            // Listview Group click listener
            expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener () {

                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                    // Toast.makeText(getApplicationContext(),
                    // "Group Clicked " + listDataHeader.get(groupPosition),
                    // Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

            // Listview Group expanded listener
            expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener () {

                @Override
                public void onGroupExpand(int groupPosition) {
                    //Do something
                }
            });

            // Listview Group collasped listener
            expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener () {

                @Override
                public void onGroupCollapse(int groupPosition) {
                    //Do something


                }
            });

            // Listview on child click listener
            expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener () {

                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {
                    // TODO Auto-generated method stub

                    return false;
                }
            });


        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);
        /*view.setFocusableInTouchMode(true);
        view.requestFocus();*/

        menu = PreferenceUtils.getMenu (getContext ());
        beneficios = PreferenceUtils.getBeneficios (getContext ());

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


    public void menuParsing(){
        try {
            JSONObject jsonMenu = new JSONObject (menu);

           /* qrcode = jsonInfo.getJSONObject("user").getString("qrcode");
            puntos = jsonInfo.getJSONObject ("user").getString ("points");

            menuJson = jsonInfo.getJSONObject("menu");
            arrayBeneficios = jsonInfo.getJSONObject("user").getJSONArray("benefits");
*/



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //Nombre de cada categoría
    public void setGroupData() {
        itemList = new ArrayList<>();
        itemList.add(" - CAFÉ CON CAFÉ");
        itemList.add(" - CAFÉ CON CACAO");
        itemList.add(" - CACAO CON CACAO");
        itemList.add(" - TÉS DE LA CASA");
        itemList.add(" - FRAPPÉS");
        itemList.add(" - BEBIDAS SIN CAFÉ");
        itemList.add(" - SODAS TG");
        itemList.add(" - AGUAS FRESCAS");
        /*itemList.add(" - PANQUES");
        itemList.add(" - TARTAS");
        itemList.add(" - GALLETAS");
        itemList.add(" - PAN DULCE");
        itemList.add(" - FRUTA Y ENSALADA");
        itemList.add(" - PASTELES");
        itemList.add(" - BOTANAS");
        itemList.add(" - COYOTAS");
        itemList.add(" - OTROS");
        itemList.add(" - TOSTADOS");
        itemList.add(" - EMPAREDADOS");*/
        itemList.add(" - EMBOTELLADOS");
    }


    //Subcategorías con productos
    public void setChildGroupData() {

        Producto producto;
        productList = new HashMap<String, List<Producto>>();

        //CAFE CON CAFE
        List <Producto> cafeCafe = new ArrayList<Producto>();
        producto = new Producto ("", "", "", "", false, true, false, false, true );
        cafeCafe.add(producto);
        producto = new Producto ("ESPRESSO", "", "", "$29", false, true, false, false, false);
        cafeCafe.add(producto);
        producto = new Producto ("ESPRESSO DOBLE", "", "", "$33", false, true, false, false, false );
        cafeCafe.add(producto);
        producto = new Producto ("ESPRESSO CORTADO", "", "", "$31", false, true, false, false, false );
        cafeCafe.add(producto);
        producto = new Producto ("ESPRESSO DOBLE CORTADO", "", "", "$34", false, true, false, false, false );
        cafeCafe.add(producto);
        producto = new Producto ("ESPRESSO TIERRA GARAT", "", "", "$37", false, true, false, false, false );
        cafeCafe.add(producto);

        producto = new Producto ("", "CH", "M", "G", false, true, false, true, true );
        cafeCafe.add(producto);
        producto = new Producto ("EL JORNALERO (DEL DÍA)", "$24", "$28", "$32", false, true, false, true, false );
        cafeCafe.add(producto);
        producto = new Producto ("ESPRESSO AMERICANO", "$32", "$35", "$39", true, true, false, true, false );
        cafeCafe.add(producto);
        producto = new Producto ("CAPUCCINO", "$39", "$46", "$52", false, true, false, true, false );
        cafeCafe.add(producto);
        producto = new Producto ("CAFÉ CON LECHE MOCHA BLANCO ", "$39", "$46", "$52", true, true, false, true, false );
        cafeCafe.add(producto);
        producto = new Producto ("MOCHA BLANCO", "$50", "$56 ", "$61", true, true, false, true, false );
        cafeCafe.add(producto);

        producto = new Producto ("", "", "", "", false, true, false, false, true );
        cafeCafe.add(producto);
        producto = new Producto ("MÉTODOS ALTERNATIVOS (PRENSA FRANCESA)", "", "", "$65", false, false, false, false, false );
        cafeCafe.add(producto);


        //CAFE CON CACAO
        List <Producto> cafeCacao = new ArrayList<Producto>();
        producto = new Producto ("MOCHAS DE LA CASA", "CH", "M", "G", false, true, false, true, true );
        cafeCacao.add(producto);
        producto = new Producto ("CRIOLLO (CACAO NATURAL)", "$48", "$54", "$58", true, true, false, true, false );
        cafeCacao.add(producto);
        producto = new Producto ("CHILTEPIN (PIQUÍN, PIMIENTA GORDA Y ACHIOTE)", "$53", "$59", "$64", true, true, false, true, false );
        cafeCacao.add(producto);
        producto = new Producto ("NEGRA FLOR (VAINILLA)", "$53", "$59", "$64", true, true, false, true, false );
        cafeCacao.add(producto);
        producto = new Producto ("DULCE MADERA (CANELA Y PIMIENTA GORDA)", "$53", "$59", "$64", true, true, false, true, false );
        cafeCacao.add(producto);
        producto = new Producto ("CAFÉ CACAO (CAFÉ EN GRANO)", "$53", "$59", "$64", true, true, false, true, false );
        cafeCacao.add(producto);
        producto = new Producto ("NARANJO HUACAL (NARANJA Y JENGIBRE)", "$53", "$59", "$64", true, true, false, true, false );
        cafeCacao.add(producto);


        //CACAO CON CACAO
        List <Producto> cacaoCacao = new ArrayList<Producto>();
        producto = new Producto ("BASE DE CACAO NATURAL", "CH", "M", "G", false, true, false, true, true );
        cacaoCacao.add(producto);
        producto = new Producto ("CRIOLLO (CACAO NATURAL)", "$43", "$49", "$54", true, true, false, true, false );
        cacaoCacao.add(producto);
        producto = new Producto ("CHILTEPIN (PIQUÍN, PIMIENTA GORDA Y ACHIOTE)", "$47", "$54", "$59", true, true, false, true, false );
        cacaoCacao.add(producto);
        producto = new Producto ("NEGRA FLOR (VAINILLA)", "$47", "$54", "$59", true, true, false, true, false );
        cacaoCacao.add(producto);
        producto = new Producto ("DULCE MADERA (CANELA Y PIMIENTA GORDA)", "$47", "$54", "$59", true, true, false, true, false );
        cacaoCacao.add(producto);
        producto = new Producto ("CAFÉ CACAO (CAFÉ EN GRANO)", "$47", "$54", "$59", true, true, false, true, false );
        cacaoCacao.add(producto);
        producto = new Producto ("NARANJO HUACAL (NARANJA Y JENGIBRE)", "$47", "$54", "$59", true, true, false, true, false );
        cacaoCacao.add(producto);
        producto = new Producto ("CHOCOLATE BLANCO", "$47", "$54", "$59", true, true, false, true, false );
        cacaoCacao.add(producto);


        //TES DE LA CASA
        List <Producto> tes = new ArrayList<Producto>();
        producto = new Producto ("INFUSIONES HERBALES", "CH", "M", "G", false, true, false, true, true);
        tes.add(producto);
        producto = new Producto ("LIMONCILLO (LIMÓN Y JENGIBRE)", "$30", "$35", "$38", true, true, false, true, false );
        tes.add(producto);
        producto = new Producto ("CHAI (TÉ NEGRO, CANELA, CARDAMOMO Y JENGIBRE)", "$30", "$35", "$38", true, true, false, true, false );
        tes.add(producto);
        producto = new Producto ("MENTA CÍTRICA (TÉ VERDE, MENTA Y LIMÓN)", "$30", "$35", "$38", true, true, false, true, false );
        tes.add(producto);
        producto = new Producto ("PERA DEL BEY (TÉ NEGRO, BERGAMOTA Y LAVANDA)", "$30", "$35", "$38", true, true, false, true, false );
        tes.add(producto);
        producto = new Producto ("MENTA CACAO (MENTA VERDE, BLANCA Y CACAO)", "$30", "$35", "$38", true, true, false, true, false );
        tes.add(producto);

        producto = new Producto ("INFUSIONES FRUTALES", "CH", "M", "G", false, true, false, true, true );
        tes.add(producto);
        producto = new Producto ("SALVAJE (MORAS)", "$39", "$44", "$49", true, true, false, true, false );
        tes.add(producto);
        producto = new Producto ("EXÓTICA (KIWI Y MARACUYÁ)", "$39", "$44", "$49", true, true, false, true, false );
        tes.add(producto);
        producto = new Producto ("FRUTAL (DURAZNO, PIÑA Y MANGO)", "$39", "$44", "$49", true, true, false, true, false );
        tes.add(producto);
        producto = new Producto ("TROPICAL (PIÑA Y COCO)", "$39", "$44", "$49", true, true, false, true, false );
        tes.add(producto);


        //FRAPPES
        List <Producto> frappes = new ArrayList<Producto>();
        producto = new Producto ("DE LA CASA", "CH", "M", "G", false, false, false, true, true );
        frappes.add(producto);
        producto = new Producto ("CAFÉ", "$46", "$51", "$55", false, false, true, true, false );
        frappes.add(producto);
        producto = new Producto ("CHOCOLATE BLANCO", "$51", "$56", "$60", false, false, true, true, false );
        frappes.add(producto);
        producto = new Producto ("MOCHA", "$51", "$56", "$60", false, false, true, true, false );
        frappes.add(producto);
        producto = new Producto ("CARAMELO", "$51", "$56", "$60", false, false, true, true, false );
        frappes.add(producto);
        producto = new Producto ("VAINILLA", "$51", "$56", "$60", false, false, true, true, false );
        frappes.add(producto);
        producto = new Producto ("TARO", "$51", "$56", "$60", false, false, true, true, false );
        frappes.add(producto);
        producto = new Producto ("CHAI", "$52", "$57", "$61", false, false, true, true, false );
        frappes.add(producto);
        producto = new Producto ("CAFÉ LIGHT", "$52", "$57", "$61", false, false, true, true, false );
        frappes.add(producto);
        producto = new Producto ("MOCHA LIGHT", "$52", "$57", "$61", false, false, true, true, false );
        frappes.add(producto);

        producto = new Producto ("BASE DE CACAO NATURAL", "CH", "M", "G", false, false, false, true, true );
        frappes.add(producto);
        producto = new Producto ("CRIOLLO (CACAO NATURAL)", "$48", "$54", "$59", false, false, true, true, false );
        frappes.add(producto);
        producto = new Producto ("CHILTEPIN (PIQUÍN, PIMIENTA GORDA Y ACHIOTE)", "$52", "$58", "$64", false, false, true, true, false );
        frappes.add(producto);
        producto = new Producto ("NEGRA FLOR (VAINILLA)",  "$52", "$58", "$64", false, false, true, true, false );
        frappes.add(producto);
        producto = new Producto ("DULCE MADERA (CANELA Y PIMIENTA GORDA)", "$52", "$58", "$64", false, false, true, true, false );
        frappes.add(producto);
        producto = new Producto ("CAFÉ CACAO (CAFÉ EN GRANO)", "$52", "$58", "$64", false, false, true, true, false );
        frappes.add(producto);
        producto = new Producto ("NARANJO HUACAL (NARANJA Y JENGIBRE)", "$52", "$58", "$64", false, false, true, true, false );
        frappes.add(producto);
        //producto = new Producto ("CHOCOLATE BLANCO", "$52", "$58", "$64", false, false, true, true, false );
        //frappes.add(producto);

        producto = new Producto ("BASE INFUSIONES HERBALES", "CH", "M", "G", false, true, false, true, true );
        frappes.add(producto);
        producto = new Producto ("LIMONCILLO (LIMÓN Y JENGIBRE)", "$37", "$42", "$45", false, false, true, true, false );
        frappes.add(producto);
        producto = new Producto ("CHAI (TÉ NEGRO, CANELA, CARDAMOMO Y JENGIBRE)", "$37", "$42", "$45", false, false, true, true, false );
        frappes.add(producto);
        producto = new Producto ("MENTA CÍTRICA (TÉ VERDE, MENTA Y LIMÓN)", "$37", "$42", "$45", false, false, true, true, false );
        frappes.add(producto);
        producto = new Producto ("PERA DEL BEY (TÉ NEGRO, BERGAMOTA Y LAVANDA)", "$37", "$42", "$45", false, false, true, true, false );
        frappes.add(producto);
        producto = new Producto ("MENTA CACAO (MENTA VERDE, BLANCA Y CACAO)", "$37", "$42", "$45", false, false, true, true, false );
        frappes.add(producto);

        producto = new Producto ("INFUSIONES FRUTALES", "CH", "M", "G", false, false, false, true, true );
        frappes.add(producto);
        producto = new Producto ("SALVAJE (MORAS)", "$45", "$50", "$55", false, false, true, true, false );
        frappes.add(producto);
        producto = new Producto ("EXÓTICA (KIWI Y MARACUYÁ)", "$45", "$50", "$55", false, false, true, true, false );
        frappes.add(producto);
        producto = new Producto ("FRUTAL (DURAZNO, PIÑA Y MANGO)", "$45", "$50", "$55", false, false, true, true, false );
        frappes.add(producto);
        producto = new Producto ("TROPICAL (PIÑA Y COCO)", "$45", "$50", "$55", false, false, true, true, false );
        frappes.add(producto);


        List <Producto> sin_cafe = new ArrayList<Producto>();
        producto = new Producto ("", "CH", "M", "G", false, true, false, true, true );
        sin_cafe.add(producto);
        producto = new Producto ("CHAI", "$47", "$53", "$58", true, true, false, true, false );
        sin_cafe.add(producto);
        producto = new Producto ("TARO", "$48", "$54", "$59", true, true, false, true, false );
        sin_cafe.add(producto);


        List <Producto> sodas = new ArrayList<Producto>();
        producto = new Producto ("", "CH", "M", "G", false, true, false, true, true );
        sodas.add(producto);
        producto = new Producto ("TAMARINDO", "$34", "$38", "$41", true, false, false, true, false );
        sodas.add(producto);
        producto = new Producto ("MANGO", "$34", "$38", "$41", true, false, false, true, false );
        sodas.add(producto);

        List <Producto> aguas_frescas = new ArrayList<Producto>();
        producto = new Producto ("", "CH", "M", "G", false, true, false, true, true );
        aguas_frescas.add(producto);
        producto = new Producto ("LIMON CON CHÍA", "$35", "$39", "$42", true, false, false, true, false );
        aguas_frescas.add(producto);
        producto = new Producto ("GUAYABA CON CHÍA", "$35", "$39", "$42", true, false, false, true, false );
        aguas_frescas.add(producto);

        List <Producto> embotellados = new ArrayList<Producto>();
        producto = new Producto ("", "", "", "", false, true, false, false, true );
        embotellados.add(producto);
        producto = new Producto ("AGUA PURIFICADA TIERRA GARAT 473ML", "", "", "$27", false, false, false, false, false);
        embotellados.add(producto);
        producto = new Producto ("AGUA MINERAL TIERRA GARAT 473ML", "", "", "$31", false, false, false, false, false);
        embotellados.add(producto);
        producto = new Producto ("COCA COLA 237ML", "", "", "$23", false, false, false, false, false);
        embotellados.add(producto);
        producto = new Producto ("COCA COLA ZERO 237ML", "", "", "$23", false, false, false, false, false);
        embotellados.add(producto);


        productList.put (itemList.get (0), cafeCafe);
        productList.put (itemList.get (1), cafeCacao);
        productList.put (itemList.get (2), cacaoCacao);
        productList.put (itemList.get (3), tes);
        productList.put (itemList.get (4), frappes);
        productList.put (itemList.get (5), sin_cafe);
        productList.put (itemList.get (6), sodas);
        productList.put (itemList.get (7), aguas_frescas);
        productList.put (itemList.get (8), embotellados);



    }




    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        return false;
    }

    public void changeFonts(View view){
        Typeface typeFace = Typeface.createFromAsset (getActivity ().getAssets (), "fonts/gotham_light.ttf");
        typeFace= Typeface.createFromAsset(getActivity ().getAssets(),"fonts/courier.ttf");
        menuTV.setTypeface (typeFace);


    }

    @Override
    public void onDestroy() {
        super.onDestroy ();
        cleanMemory ();
    }


    public void cleanMemory() {

    }
}
