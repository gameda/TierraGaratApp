package mx.digitalcoaster.tierra_garat_puntos.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentAdapter extends FragmentStatePagerAdapter {

    //Lista de Fragments
    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentTitleList = new ArrayList<>();


    //Constructor
    public FragmentAdapter(FragmentManager fm) {
        super (fm);
    }

    //Metodo para añadir fragmentos a una listas
    public void addFragment(Fragment fragment, String tittle){
        fragmentList.add (fragment);
        fragmentTitleList.add (tittle);
    }

    @Override
    public int getCount() {
        return fragmentList.size ();
    }

    @Override
    public Fragment getItem(int iPosition) {
        return  fragmentList.get(iPosition);}



}
