package mx.digitalcoaster.tierra_garat_puntos.models;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public class Sucursal {

    String nombre;
    String direccion;
    String abre;
    String cierra;
    LatLng cordenadas;
    int distancia;
    String estatus;


    public Sucursal(){
        super();

    }

    public Sucursal(String nombre, String direccion, String abre, String cierra, LatLng cordenadas, int distancia, String estatus){
        this.nombre = nombre;
        this.direccion = direccion;
        this.abre = abre;
        this.cierra = cierra;
        this.cordenadas = cordenadas;
        this.distancia = distancia;
        this.estatus = estatus;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getAbre() {
        return abre;
    }

    public String getCierra() {
        return cierra;
    }

    public int getDistancia() {
        return distancia;
    }

    public LatLng getCordenadas() {
        return cordenadas;
    }

    public String getEstatus() {
        return estatus;
    }


    public void setCordenadas(LatLng cordenadas) {
        this.cordenadas = cordenadas;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public void setCierra(String cierra) {
        this.cierra = cierra;
    }

    public void setAbre(String abre) {
        this.abre = abre;
    }


}

