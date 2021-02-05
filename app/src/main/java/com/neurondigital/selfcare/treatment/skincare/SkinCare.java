package com.neurondigital.selfcare.treatment.skincare;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.neurondigital.selfcare.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class SkinCare extends AppCompatActivity {
    private Button addButton;
    private EditText userInput;
    private ListView lv;
    private Button clear;
    String note = "N/A";
    private ArrayList<HashMap<String, String>> scList;
    SkinCareDatabase db = new SkinCareDatabase(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin_care);

        Log.d("SkinCare.java", "onCreate: creating db");
        SkinCareDatabase db = new SkinCareDatabase(this);

        //get any previously saved skincare objects when the page loads
        loadDataFromDatabase(db);
        //initialize buttons from layout
        addButton = (Button)findViewById(R.id.addButton);
        userInput = (EditText)findViewById(R.id.enterNote);
        clear = (Button)findViewById(R.id.clear);
        lv = findViewById(R.id.scList);

        //onclick for add Button -
        addButton.setOnClickListener(click -> {
            String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());

            SkinCareModel sc = new SkinCareModel();
            sc.setDate(date);

            if (userInput.getText().toString().isEmpty() || userInput.getText().toString()==""){
                Log.d("SKINCARE", "prompt user ");
                this.promptUser();
            }
            else{
                InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            }

            sc.setNote(userInput.getText().toString());

            if(!userInput.getText().toString().isEmpty()){
                db.addmodel(sc);
                userInput.setText("");
                loadDataFromDatabase(db);
            }


        });

        //set onclick listener for deleting all rows
        clear.setOnClickListener(click -> {
            this.confirmDelAll();

        });


        //set onlcick listener for long click to delete clicked row
        lv.setOnItemLongClickListener((adapterView, view, pos, l) -> {

            final  SimpleAdapter adapter = new SimpleAdapter(this, scList, R.layout.activity_skincare_row, new String[]{"Date", "Note"}, new int[]{R.id.Date, R.id.Note});
            this.confirmDel(pos);
            loadDataFromDatabase(db);
            adapter.notifyDataSetChanged();

            return true;

        });

    }

    //remind user to enter note about the skincare they did
    public void promptUser(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Enter type of Skincare")
                .setMessage("Please enter type of skin care performed and then click 'Log Skincare'");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                Log.d("SKINCARE", "notified user for input");
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    //confirm user wants to delete long clicked row
    public void confirmDel(int pos){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Delete this skincare Activity?")
                .setMessage("Are you sure you want to delete this skincare activity?");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                Log.d("SKINCARE", "confirmed delete at id: "+pos);
                HashMap<String, String> map = new HashMap<>();
                map = scList.get(pos);
                db.deleterow(Integer.parseInt(map.get("_id")));
                loadDataFromDatabase(db);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    //confirm user wants to delete all rows
    public void confirmDelAll(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Clear the list?")
                .setMessage("Are you sure you want to delete all logged skincare?");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                Log.d("SKINCARE", "confirmed delete");
                db.deleteAll();
                loadDataFromDatabase(db);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }



    //get everything from database, pass the list to simple adapter
    public void loadDataFromDatabase(SkinCareDatabase db) {

        scList = db.getAll();
        ListView lv = (ListView) findViewById(R.id.scList);
        SimpleAdapter adapter = new SimpleAdapter(this, scList, R.layout.activity_skincare_row, new String[]{"Date", "Note"}, new int[]{R.id.Date, R.id.Note});

        lv.setAdapter(adapter);
        db.close();
        adapter.notifyDataSetChanged();
    }






}

