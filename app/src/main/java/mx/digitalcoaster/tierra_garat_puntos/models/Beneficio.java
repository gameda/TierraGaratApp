package mx.digitalcoaster.tierra_garat_puntos.models;

import java.util.Date;

public class Beneficio {

    String sNombre;
    String sDescripcion;
    int iPuntos;
    Date dCaducidad;
    String sType;
    String sCodigo;
    int iUsed;

    public Beneficio(){
        super();

    }

    public Beneficio(String nombre, String descripcion, int puntos, Date caducidad, String beneficio, String codigo){
        sNombre = nombre;
        sDescripcion = descripcion;
        iPuntos = puntos;
        dCaducidad = caducidad;
        sType = beneficio;
        sCodigo = codigo;
    }


    public Beneficio(String nombre, String descripcion,  String type){
        sNombre = nombre;
        sDescripcion = descripcion;
        sType = type;

    }

    public Beneficio(String nombre, String descripcion,  String type, String codigo, int used){
        sNombre = nombre;
        sDescripcion = descripcion;
        sType = type;
        sCodigo = codigo;
        iUsed = used;

    }

    public String getsNombre() {
        return sNombre;
    }

    public String getsDescripcion() {
        return sDescripcion;
    }

    public int getiPuntos() {
        return iPuntos;
    }

    public Date getdCaducidad() {
        return dCaducidad;
    }

    public String getsType() {
        return sType;
    }

    public String getsCodigo() {
        return sCodigo; }

    public int getiUsed() {
        return iUsed;
    }

    public void setsNombre(String sNombre) {
        this.sNombre = sNombre;
    }

    public void setsDescripcion(String sDescripcion) {
        this.sDescripcion = sDescripcion;
    }
    public void setiPuntos(int iPuntos) {
        this.iPuntos = iPuntos;
    }

    public void setdCaducidad(Date dCaducidad) {
        this.dCaducidad = dCaducidad;
    }

    public void setsType(String bBeneficio) {
        this.sType = bBeneficio;
    }


    public void setsCodigo(String sCodigo) {
        this.sCodigo = sCodigo;
    }

    public void setiUsed(int iUsed) {
        this.iUsed = iUsed;
    }
}
