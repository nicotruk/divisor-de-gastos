package nico.divisorgastos.modelo;

import java.util.ArrayList;

public class Persona {
    private ArrayList<Gasto> gastos = new ArrayList<Gasto>();
    private String nombre;
    private float montoAcreedor;
    private float montoDeudor;

    public Persona(String nombre){
        this.nombre = nombre;
    }

    public void agregarGasto(Gasto unGasto){
        gastos.add(unGasto);
    }

    public String getNombre() {
        return this.nombre;
    }

    public float getGastoTotal(){
        float total = 0;
        for(int i=0;i<gastos.size();i++){
            float parcial = gastos.get(i).getMonto();
            total += parcial;
        }
        return total;
    }

    public ArrayList<String> getDetalleGastos(){
        ArrayList<String> resultado = new ArrayList<>();
        for(int i=0;i<gastos.size();i++){
            resultado.add(gastos.get(i).getDetalle());
        }
        return resultado;
    }

    public Gasto getGasto(int index) {
        return gastos.get(index);
    }

    public void setGasto(Gasto ungasto) {
        gastos.add(ungasto);
    }

    public void setMontoAcreedor(float monto){
        this.montoAcreedor = monto;
    }

    public float getMontoAcreedor(){
        return this.montoAcreedor;
    }

    public void setMontoDeudor(float monto) {
        this.montoDeudor = monto;
    }

    public float getMontoDeudor(){
        return this.montoDeudor;
    }

    public String toListString() {
        String result = "";
        if (montoDeudor > 0) {
            result = "+" + this.nombre + " debe pagar $" + this.montoDeudor;
        } else if (montoAcreedor > 0) {
            result = "-" + this.nombre + " debe llevarse $" + this.montoAcreedor;
        }
        return result;
    }



}
