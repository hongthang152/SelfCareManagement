package com.neurondigital.selfcare.treatment.skincare;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.neurondigital.selfcare.R;
import com.neurondigital.selfcare.treatment.manuallymphdrainagemassage.MLD;
import com.neurondigital.selfcare.treatment.manuallymphdrainagemassage.MLDList;
import com.neurondigital.selfcare.treatment.manuallymphdrainagemassage.MLDRecordDetail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ScList extends AppCompatActivity {

    private ListView lv;
    String note = "N/A";
    private ArrayList<HashMap<String, String>> scList;
    SkinCareDatabase db = new SkinCareDatabase(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sc_list);

        Log.d("SkinCare.java", "onCreate: creating db");
        SkinCareDatabase db = new SkinCareDatabase(this);

        //get any previously saved skincare objects when the page loads
        loadDataFromDatabase(db);
        //initialize listview
        lv = findViewById(R.id.scList);



        //set onclick listener for deleting all rows
        /* Only needed for testing purposes
        clear.setOnClickListener(click -> {
            this.confirmDelAll();

        }); */


        //set onlcick listener for long click to delete clicked row
        lv.setOnItemLongClickListener((adapterView, view, pos, l) -> {

            final  SimpleAdapter adapter = new SimpleAdapter(this, scList, R.layout.activity_skincare_row, new String[]{"Date", "Note"}, new int[]{R.id.Date, R.id.Note});
            this.confirmDel(pos);
            loadDataFromDatabase(db);
            adapter.notifyDataSetChanged();

            return true;

        });

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
    /* Only used for testing purposes
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

    }*/

    //get everything from database, pass the list to simple adapter
    public void loadDataFromDatabase(SkinCareDatabase db) {

        scList = db.getAll();
        ListView lv = (ListView) findViewById(R.id.scList);
        SimpleAdapter adapter = new SimpleAdapter(this, scList, R.layout.activity_skincare_row, new String[]{"Date", "Note"}, new int[]{R.id.Date, R.id.Note});
        lv.setAdapter(adapter);

        //set onclicklistener to go to SCrecorddetail when item from list is clicked
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ScList.this, SCRecordDetail.class);
                intent.putExtra(SCRecordDetail.RECORD_EXTRA, scList.get(position));
                startActivityForResult(intent, 0);
            }
        });

        db.close();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataFromDatabase(db);
    }
}
