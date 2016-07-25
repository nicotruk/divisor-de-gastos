package nico.divisorgastos;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class AddPersonaActivity extends Activity {
    private String nombre;
    private String detalle;
    private String monto;
    private static ArrayList<String> nombresCargados = new ArrayList<>();

    @Override
    protected  void  onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_persona);

        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.name_help);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, nombresCargados);
        setTheme(android.R.style.Theme);
        textView.setAdapter(adapter);

        Button listoButton = (Button) findViewById(R.id.add_button);
        listoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nombreBox = (EditText) findViewById(R.id.name_help);
                nombre = nombreBox.getText().toString();

                EditText detalleBox = (EditText) findViewById(R.id.gasto_detalle);
                detalle = detalleBox.getText().toString();

                EditText montoBox = (EditText) findViewById(R.id.gasto_monto);
                monto = montoBox.getText().toString();

                if(nombre.matches("") || detalle.matches("") || monto.matches("")){
                    Toast.makeText(getApplicationContext(), "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    if (!nombresCargados.contains(nombre)) {
                        nombresCargados.add(nombre);
                    }
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("nombre", nombre);
                    intent.putExtra("detalle", detalle);
                    intent.putExtra("monto", monto);
                    setResult(1, intent);
                    finish();
                }
            }
        });

    }
}
