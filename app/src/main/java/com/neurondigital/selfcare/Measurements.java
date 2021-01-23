package com.neurondigital.selfcare;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Measurements extends AppCompatActivity {
    MeasureDatabase db = new MeasureDatabase(Measurements.this);
    private ListView listView;

    ArrayList<MLDModel> userList1 = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurements);
        Button yes = findViewById(R.id.yes);


        MeasureDatabase db = new MeasureDatabase(this);
        loadDataFromDatabase(); //get any previously saved Contact objects




        ListView lv = (ListView) findViewById(R.id.measurement_list);
        ArrayList<HashMap<String, String>> measurelist = db.getAll();

        final SimpleAdapter adapter = new SimpleAdapter(Measurements.this, measurelist, R.layout.measurelist, new String[]{"type"}, new int[]{R.id.Date});

        lv.setAdapter(adapter);


        yes.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View btn) {

                AlertDialog alertDialog = new AlertDialog.Builder( Measurements.this)
//set icon
                        .setIcon(android.R.drawable.ic_dialog_alert)
//set title
                        .setTitle("Are you sure?")
//set message
                        .setMessage("")
//set positive button
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //set what would happen when positive button is clicked
                                // finish();
                                MeasureDatabase db = new MeasureDatabase(Measurements.this);
                                String date = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());

                                db.addmodel(new MeasureClass(date));
                                loadDataFromDatabase();

                            }
                        })
//set negative button
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //set what should happen when negative button is clicked
                                Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG).show();
                            }
                        })
                        .show();


            }
        });

    }
    public void loadDataFromDatabase() {
        MeasureDatabase db = new MeasureDatabase(this);
        ArrayList<HashMap<String, String>> userList = db.getAll();
        ListView lv = (ListView) findViewById(R.id.measurement_list);
        ListAdapter adapter = new SimpleAdapter(Measurements.this, userList, R.layout.measurelist, new String[]{"date"}, new int[]{R.id.Date});
        lv.setAdapter(adapter);

    }

}