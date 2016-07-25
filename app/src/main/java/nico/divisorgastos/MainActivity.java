package nico.divisorgastos;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nico.divisorgastos.modelo.Gasto;
import nico.divisorgastos.modelo.Persona;

public class MainActivity extends AppCompatActivity {

    private Map<String, Persona> personas = new HashMap<>();
    private float gastoTotal;
    private float gastoPerCapita;
    private TextView gastoTotalText;
    private TextView gastoPerCapitaText;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gastoTotalText = (TextView) findViewById(R.id.total);
        gastoPerCapitaText = (TextView) findViewById(R.id.totalPerCapita);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddPersonaActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        // preparing list data
        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int lastExpandedPosition = -1;
            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    expListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        expListView.collapseGroup(0);
        expListView.collapseGroup(1);
        expListView.collapseGroup(2);

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                String nombre = extras.getString("nombre");
                String detalle = extras.getString("detalle");
                String montoString = extras.getString("monto");
                float monto = 0;
                if(montoString!=null) {
                    monto = Float.valueOf(montoString);
                }
                Persona unaPersona = new Persona(nombre);
                Gasto unGasto = new Gasto(monto, detalle);
                unaPersona.setGasto(unGasto);
                agregarNuevoDato(unaPersona);
            }
            calcularGastoTotal();
            gastoTotalText.setText("$" + Float.toString(gastoTotal));
            calcularGastoPerCapita();
            gastoPerCapitaText.setText("$" + Float.toString(gastoPerCapita));


            calcularDeudoresYAcreedores();
            updateDetalleDeCompras();
            updatePagan();
            updateCobran();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding Headers
        listDataHeader.add("Detalle de Compras");
        listDataHeader.add("Pagan al Pozo");
        listDataHeader.add("Cobran del Pozo");

        // Adding Child Data
        listDataChild.put(listDataHeader.get(0), new ArrayList<String>());
        listDataChild.put(listDataHeader.get(1), new ArrayList<String>());
        listDataChild.put(listDataHeader.get(2), new ArrayList<String>());
    }

    public void calcularGastoTotal() {
        float total = 0;
        for(String nombre : personas.keySet()) {
            float parcial = personas.get(nombre).getGastoTotal();
            total+=parcial;
        }
        gastoTotal = total;
    }

    public void calcularGastoPerCapita() {
        float cantidadDePersonas = personas.size();

        if(cantidadDePersonas == 0){
            gastoPerCapita = 0;
        } else {
            gastoPerCapita = gastoTotal/cantidadDePersonas;
        }
    }

    public void clearDeudoresYAcreedores() {
        for(String nombre : personas.keySet()) {
            personas.get(nombre).setMontoAcreedor(0);
            personas.get(nombre).setMontoDeudor(0);
        }
    }

    public void calcularDeudoresYAcreedores() {
        clearDeudoresYAcreedores();
        for(String nombre : personas.keySet()) {
            Persona persona = personas.get(nombre);
            if(persona.getGastoTotal() < gastoPerCapita) {
                persona.setMontoDeudor(gastoPerCapita - persona.getGastoTotal());
            } else if(persona.getGastoTotal() > gastoPerCapita) {
                persona.setMontoAcreedor(persona.getGastoTotal() - gastoPerCapita);
            }
        }
    }

    public void updateDetalleDeCompras(){
        ArrayList<String> detalles = new ArrayList<>();
        for(String nombre : personas.keySet()){
            ArrayList<String> unDetalles = personas.get(nombre).getDetalleGastos();
            for(int i=0;i<unDetalles.size();i++){
                detalles.add(unDetalles.get(i));
            }
            listDataChild.put(listDataHeader.get(0), detalles);
        }
    }

    public void updatePagan(){
        ArrayList<String> pagan = new ArrayList<>();
        for(String nombre : personas.keySet()){
            Persona unaPersona = personas.get(nombre);
            if(unaPersona.getMontoDeudor() > 0) {
                pagan.add(unaPersona.toListString());
            }
        }
        listDataChild.put(listDataHeader.get(1), pagan);
    }

    public void updateCobran(){
        ArrayList<String> cobran = new ArrayList<>();
        for(String nombre : personas.keySet()){
            Persona unaPersona = personas.get(nombre);
            if(unaPersona.getMontoAcreedor() > 0) {
                cobran.add(unaPersona.toListString());
            }
        }
        listDataChild.put(listDataHeader.get(2), cobran);
    }

    public void agregarNuevoDato(Persona unaPersona) {
        if(personas.containsKey(unaPersona.getNombre())) {
            personas.get(unaPersona.getNombre()).agregarGasto(unaPersona.getGasto(0));
        } else {
            personas.put(unaPersona.getNombre(), unaPersona);
        }
    }



}
