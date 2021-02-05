package com.neurondigital.selfcare;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.neurondigital.selfcare.treatment.TreatmentModuleActivity;

import static com.neurondigital.selfcare.Configurations.SHOW_SPLASH_SCREEN_BACKGROUND_IMAGE;

public class SplashScreenActivity extends AppCompatActivity {

    FragmentActivity activity;
    LoginButton loginButton;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;

        //go directly to main Activity if users are disabled
        if (!Configurations.ENABLE_USER_SYSTEM) {
            Intent intent = new Intent(this, TreatmentModuleActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        //go directly to main Activity if user already logged in.
        if (User.isUserLoggedIn(activity)) {
            Intent intent = new Intent(activity, TreatmentModuleActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        //set layout
        setContentView(R.layout.activity_splash_screen);

        //set background image
        ImageView background = (ImageView) findViewById(R.id.background);
        if (SHOW_SPLASH_SCREEN_BACKGROUND_IMAGE)
            background.setVisibility(View.VISIBLE);


        //skip button
        TextView skipBtn = (TextView) findViewById(R.id.skip_button);
        skipBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ((TextView) view).setTextColor(ContextCompat.getColor(activity, R.color.accent)); // white
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        ((TextView) view).setTextColor(0xFFFFFFFF); // lightblack
                        break;
                    case MotionEvent.ACTION_UP:
                        ((TextView) view).setTextColor(0xFFFFFFFF); // lightblack
                        break;
                }
                return false;
            }
        });
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, TreatmentModuleActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });


        //normal email password login
        final EditText emailView = (EditText) findViewById(R.id.email);
        final EditText passwordView = (EditText) findViewById(R.id.password);
        Button loginBtn = (Button) findViewById(R.id.login_button);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //login to server
                User.login(activity, emailView.getText().toString(), passwordView.getText().toString(), new User.onLoginListener() {
                    @Override
                    public void onLogin(String email) {
                        Intent intent = new Intent(activity, TreatmentModuleActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }



}
