package mx.digitalcoaster.tierra_garat_puntos.models;

public class Producto {

    String nombre;
    String precioChico;
    String precioMediano;
    String precioGrande;
    Boolean bFrio;
    Boolean bCaliente;
    Boolean bFrape;
    Boolean bTamaños;
    Boolean bMargen;

    public Producto() {
        super ();
    }
    public Producto(String nombre, String precioChico, String precioMediano, String precioGrande, Boolean bFrio, Boolean bCaliente, Boolean bFrape, Boolean bTamaños, Boolean bMargen){
        this.nombre = nombre;
        this.precioChico = precioChico;
        this.precioMediano = precioMediano;
        this.precioGrande = precioGrande;
        this.bFrio = bFrio;
        this.bCaliente = bCaliente;
        this.bFrape = bFrape;
        this.bTamaños = bTamaños;
        this.bMargen = bMargen;

    }

    public String getNombre() {
        return nombre;
    }

    public Boolean getbCaliente() {
        return bCaliente;
    }

    public Boolean getbFrape() {
        return bFrape;
    }

    public Boolean getbFrio() {
        return bFrio;
    }

    public Boolean getbTamaños() {
        return bTamaños;
    }

    public String getPrecioChico() {
        return precioChico;
    }

    public String getPrecioGrande() {
        return precioGrande;
    }

    public String getPrecioMediano() {
        return precioMediano;
    }

    public Boolean getbMargen() {
        return bMargen;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecioChico(String precioChico) {
        this.precioChico = precioChico;
    }

    public void setPrecioMediano(String precioMediano) {
        this.precioMediano = precioMediano;
    }

    public void setbCaliente(Boolean bCaliente) {
        this.bCaliente = bCaliente;
    }

    public void setbFrape(Boolean bFrape) {
        this.bFrape = bFrape;
    }

    public void setbFrio(Boolean bFrio) {
        this.bFrio = bFrio;
    }

    public void setbTamaños(Boolean bTamaños) {
        this.bTamaños = bTamaños;
    }

    public void setPrecioGrande(String precioGrande) {
        this.precioGrande = precioGrande;
    }

    public void setbMargen(Boolean bMargen) {
        this.bMargen = bMargen;
    }
}

