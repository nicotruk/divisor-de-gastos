package nico.divisorgastos.modelo;

public class Gasto {
    private float monto;
    private String detalle;

    public Gasto(float unMonto, String unDetalle){
        this.monto = unMonto;
        this.detalle = unDetalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getDetalle() {
        return this.detalle;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }

    public float getMonto(){
        return this.monto;
    }

}
