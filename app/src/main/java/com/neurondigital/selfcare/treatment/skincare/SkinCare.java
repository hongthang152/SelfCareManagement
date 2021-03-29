package com.neurondigital.selfcare.treatment.skincare;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.neurondigital.selfcare.R;
import com.neurondigital.selfcare.treatment.manuallymphdrainagemassage.MLD;
import com.neurondigital.selfcare.treatment.manuallymphdrainagemassage.MLDList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class SkinCare extends AppCompatActivity {
    private Button addButton;
    private EditText userInput;
    private ListView lv;
    private Button clear;
    FloatingActionButton eventListBtn;
    String note = "N/A";
    private ArrayList<HashMap<String, String>> scList;
    SkinCareDatabase db = new SkinCareDatabase(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sc);

        Log.d("SkinCare.java", "onCreate: creating db");
        SkinCareDatabase db = new SkinCareDatabase(this);

        //initialize buttons from layout
        addButton = (Button)findViewById(R.id.addButton);
        userInput = (EditText)findViewById(R.id.enterNote);
        eventListBtn = findViewById(R.id.event_list_btn);

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
            }


        });


        eventListBtn.setOnClickListener(e -> {
            startActivity(new Intent(SkinCare.this, ScList.class));
        });

    }

    //remind user to enter note about the skincare they did
    public void promptUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Enter type of Skincare")
                .setMessage("Please enter type of skin care performed before saving");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                Log.d("SKINCARE", "notified user for input");
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


}

