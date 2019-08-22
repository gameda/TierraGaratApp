package mx.digitalcoaster.tierra_garat_puntos.models;
import android.support.annotation.NonNull;

import java.io.Serializable;

public class User implements Serializable{

    private String sName;
    private String sSex;
    private String sTel;
    private String sEmail;
    private String sPassword;
    private String sBirth;
    private String sAddress;
    private String sCity;
    private int iNumE;
    private int iNumI;
    private int iCP;
    private int iPuntos;
    private String sCodigoQR;


    //Constructor de usuario.
    public User(){
        super();
    }

    public User(String name, String sex, String email, String password, String birth, String address,
                int numE, int numI, int cp, String city, int puntos, String codigo){
        this.sName = name;
        this.sSex = sex;
        this.sEmail = email;
        this.sPassword = password;
        this.sBirth = birth;
        this.sAddress = address;
        this.iNumE = numE;
        this.iNumI = numI;
        this.iCP = cp;
        this.sCity = city;
        this.iPuntos = puntos;
        this.sCodigoQR = codigo;

    }

    public String getName() {
        return sName;
    }

    public String getSex() {
        return sSex;
    }

    public String getsTel() {
        return sTel;
    }

    public String getEmail() {
        return sEmail;
    }

    public String getPassword() {
        return sPassword;
    }

    public String getBirth() {
        return sBirth;
    }

    public String getAddress() {
        return sAddress;
    }

    public int getNumE() {
        return iNumE;
    }

    public int getNumI() {
        return iNumI;
    }

    public int getCP() {
        return iCP;
    }

    public String getCity() {
        return sCity;
    }

    public int getPuntos() {
        return iPuntos;
    }

    public String getCodigoQR() {
        return sCodigoQR;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public void setsSex(String sSex) {
        this.sSex = sSex;
    }

    public void setsTel(String sTel) {
        this.sTel = sTel;
    }

    public void setsEmail(String sEmail) {
        this.sEmail = sEmail;
    }

    public void setsPassword(String sPassword) {
        this.sPassword = sPassword;
    }

    public void setsBirth(String dBirth) {
        this.sBirth = dBirth;
    }

    public void setsAddress(String sAddress) {
        this.sAddress = sAddress;
    }

    public void setiNumE(int iNumE) {
        this.iNumE = iNumE;
    }

    public void setiNumI(int iNumI) {
        this.iNumI = iNumI;
    }

    public void setiCP(int iCP) {
        this.iCP = iCP;
    }

    public void setsCity(String sCity) {
        this.sCity = sCity;
    }

    public void setPuntos(int iPuntos) {
        this.iPuntos = iPuntos;
    }

    public void setsCodigoQR(String sCodigoQR) {
        this.sCodigoQR = sCodigoQR;
    }


}

