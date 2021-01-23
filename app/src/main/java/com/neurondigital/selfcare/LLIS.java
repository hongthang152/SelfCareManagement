package com.neurondigital.selfcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class LLIS extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l_l_i_s);

        Button submitt = findViewById(R.id.submit);



        submitt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btn) {
                Intent it = new Intent(Intent.ACTION_SEND);
                it.setData(Uri.parse("mailto:"));
                it.setType("text/plain");

                it.putExtra(Intent.EXTRA_EMAIL, "hirdalwadi96@gmail.com");
                it.putExtra(Intent.EXTRA_SUBJECT,"LLIS Report");
                String s1 = loaddata();
                if(s1 != null) {
                    it.putExtra(Intent.EXTRA_TEXT, loaddata());
                    try {
                        startActivity(Intent.createChooser(it, "Send mail..."));
                        finish();
                        Log.i("Finished sending email.", "");
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(getApplicationContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
                    }


                }

            }
        });


    }
    public String loaddata(){
        TextView tv = (TextView) findViewById(R.id.textView);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.g1);
        TextView tv1 = (TextView) findViewById(R.id.textView1);
        RadioGroup radioGroup1 = (RadioGroup) findViewById(R.id.g2);TextView tv2 = (TextView) findViewById(R.id.textView2);
        RadioGroup radioGroup2 = (RadioGroup) findViewById(R.id.g3);TextView tv3 = (TextView) findViewById(R.id.textView3);
        RadioGroup radioGroup3 = (RadioGroup) findViewById(R.id.g4);TextView tv4 = (TextView) findViewById(R.id.textView4);
        RadioGroup radioGroup4 = (RadioGroup) findViewById(R.id.g5);TextView tv5 = (TextView) findViewById(R.id.textView5);
        RadioGroup radioGroup5 = (RadioGroup) findViewById(R.id.g6);TextView tv7 = (TextView) findViewById(R.id.textView7);
        RadioGroup radioGroup6 = (RadioGroup) findViewById(R.id.g7);TextView tv8 = (TextView) findViewById(R.id.textView8);
        RadioGroup radioGroup7 = (RadioGroup) findViewById(R.id.g8);TextView tv9 = (TextView) findViewById(R.id.textView9);
        RadioGroup radioGroup8 = (RadioGroup) findViewById(R.id.g9);TextView tv10 = (TextView) findViewById(R.id.textView11);
        RadioGroup radioGroup9 = (RadioGroup) findViewById(R.id.g11);TextView tv11 = (TextView) findViewById(R.id.textView12);
        RadioGroup radioGroup10 = (RadioGroup) findViewById(R.id.g12);TextView tv12 = (TextView) findViewById(R.id.textView13);
        RadioGroup radioGroup11= (RadioGroup) findViewById(R.id.g13);TextView tv13 = (TextView) findViewById(R.id.textView14);
        RadioGroup radioGroup12 = (RadioGroup) findViewById(R.id.g14);TextView tv14 = (TextView) findViewById(R.id.textView15);
        RadioGroup radioGroup13 = (RadioGroup) findViewById(R.id.g15);TextView tv15 = (TextView) findViewById(R.id.textView16);
        RadioGroup radioGroup14 = (RadioGroup) findViewById(R.id.g16);TextView tv16 = (TextView) findViewById(R.id.textView17);
        RadioGroup radioGroup15 = (RadioGroup) findViewById(R.id.g17);TextView tv17 = (TextView) findViewById(R.id.textView18);
        RadioGroup radioGroup16 = (RadioGroup) findViewById(R.id.g18);TextView tv18 = (TextView) findViewById(R.id.textView19);
        RadioGroup radioGroup17 = (RadioGroup) findViewById(R.id.g19);



        try {


            int selectedId = radioGroup.getCheckedRadioButtonId();


            int selectedId1 = radioGroup1.getCheckedRadioButtonId();
            int selectedId2 = radioGroup2.getCheckedRadioButtonId();
            int selectedId3 = radioGroup3.getCheckedRadioButtonId();

            int selectedId4 = radioGroup4.getCheckedRadioButtonId();
            int selectedId5 = radioGroup5.getCheckedRadioButtonId();
            int selectedId6 = radioGroup6.getCheckedRadioButtonId();
            int selectedId7 = radioGroup7.getCheckedRadioButtonId();

            int selectedId8 = radioGroup8.getCheckedRadioButtonId();
            int selectedId9 = radioGroup9.getCheckedRadioButtonId();
            int selectedId10 = radioGroup10.getCheckedRadioButtonId();
            int selectedId11 = radioGroup11.getCheckedRadioButtonId();

            int selectedId12 = radioGroup12.getCheckedRadioButtonId();
            int selectedId13 = radioGroup13.getCheckedRadioButtonId();
            int selectedId14 = radioGroup14.getCheckedRadioButtonId();
            int selectedId15 = radioGroup15.getCheckedRadioButtonId();

            int selectedId16 = radioGroup16.getCheckedRadioButtonId();
            int selectedId17 = radioGroup17.getCheckedRadioButtonId();


            // find the radiobutton by returned id
            RadioButton radioButton = (RadioButton) findViewById(selectedId);
            RadioButton radioButton1 = (RadioButton) findViewById(selectedId1);
            RadioButton radioButton2 = (RadioButton) findViewById(selectedId2);
            RadioButton radioButton3 = (RadioButton) findViewById(selectedId3);
            RadioButton radioButton4 = (RadioButton) findViewById(selectedId4);
            RadioButton radioButton5 = (RadioButton) findViewById(selectedId5);
            RadioButton radioButton6 = (RadioButton) findViewById(selectedId6);
            RadioButton radioButton7 = (RadioButton) findViewById(selectedId7);
            RadioButton radioButton8 = (RadioButton) findViewById(selectedId8);
            RadioButton radioButton9 = (RadioButton) findViewById(selectedId9);
            RadioButton radioButton10 = (RadioButton) findViewById(selectedId10);
            RadioButton radioButton11 = (RadioButton) findViewById(selectedId11);

            RadioButton radioButton12 = (RadioButton) findViewById(selectedId12);
            RadioButton radioButton13 = (RadioButton) findViewById(selectedId13);
            RadioButton radioButton14 = (RadioButton) findViewById(selectedId14);
            RadioButton radioButton15 = (RadioButton) findViewById(selectedId15);
            RadioButton radioButton16 = (RadioButton) findViewById(selectedId16);
            RadioButton radioButton17 = (RadioButton) findViewById(selectedId17);


            String String1 = tv.getText() + "  " + radioButton.getText() + "\n\n" +
                    tv1.getText() + "  " + radioButton1.getText() + "\n\n" +
                    tv2.getText() + "  " + radioButton2.getText() + "\n\n" +
                    tv3.getText() + "  " + radioButton3.getText() + "\n\n" +
                    tv4.getText() + "  " + radioButton4.getText() + "\n\n" +
                    tv5.getText() + "  " + radioButton5.getText() + "\n\n" +
                    tv7.getText() + "  " + radioButton6.getText() + "\n\n" +
                    tv8.getText() + "  " + radioButton7.getText() + "\n\n" +
                    tv9.getText() + "  " + radioButton8.getText() + "\n\n" +
                    tv10.getText() + "  " + radioButton9.getText() + "\n\n" +
                    tv11.getText() + "  " + radioButton10.getText() + "\n\n" +
                    tv12.getText() + "  " + radioButton11.getText() + "\n\n" +
                    tv13.getText() + "  " + radioButton12.getText() + "\n\n" +
                    tv14.getText() + "  " + radioButton13.getText() + "\n\n" +
                    tv15.getText() + "  " + radioButton14.getText() + "\n\n" +
                    tv16.getText() + "  " + radioButton15.getText() + "\n\n" +
                    tv17.getText() + "  " + radioButton16.getText() + "\n\n" +
                    tv18.getText() + "  " + radioButton17.getText();



            return String1;
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "There are some unanswered questions.", Toast.LENGTH_SHORT).show();

            return null;
        }
    }
}