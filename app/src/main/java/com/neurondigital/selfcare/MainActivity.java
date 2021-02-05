package com.neurondigital.selfcare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.icu.util.ULocale;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.messaging.FirebaseMessaging;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.neurondigital.selfcare.treatment.TreatmentModuleActivity;
import com.neurondigital.selfcare.treatment.exercise.Exercise;
import com.neurondigital.selfcare.treatment.pneumatic.Pneumatic;
import com.neurondigital.selfcare.treatment.skincare.SkinCare;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    Drawer drawer;
    Context context;
    AppCompatActivity activity;

    //navigation drawer item identification numbers
    final int  NAV_INFO = 4, NAVSETTINGS = 6,  NAV_PROFILE = 8, NAV_LOGOUT = 9, NAV_CATEGORIES = 100, NAV_POLICY = 10, NAV_TERMS = 11;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        //enable/disable Firebase topic subscription
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPref.getBoolean("pref_enable_push_notifications", true))
            FirebaseMessaging.getInstance().subscribeToTopic(Configurations.FIREBASE_PUSH_NOTIFICATION_TOPIC);
        else
            FirebaseMessaging.getInstance().unsubscribeFromTopic(Configurations.FIREBASE_PUSH_NOTIFICATION_TOPIC);


        //set toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Generate the side navigation drawer
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withRootView(R.id.drawer_container)
                //.withDisplayBelowStatusBar(true)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(getDrawerItems(null))
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        //On click: open the required activity or fragment
                        Intent intent;
                        switch ((int) drawerItem.getIdentifier()) {

                            case NAV_INFO:
                                intent = new Intent(MainActivity.this, infoactivity.class);
                                startActivity(intent);
                                break;

                            case NAVSETTINGS:
                                intent = new Intent(context, SettingsActivity.class);
                                startActivity(intent);
                                break;

                            case NAV_PROFILE:
                                if (User.isUserLoggedInElseTry(activity)) {
                                    intent = new Intent(context, ProfileActivity.class);
                                    startActivity(intent);
                                }
                                break;
                            case NAV_LOGOUT:
                                User.logout(context);
                                break;
                            case NAV_POLICY:
                                intent = new Intent(context, PolicyActivity.class);
                                startActivity(intent);
                                break;
                            case NAV_TERMS:
                                intent = new Intent(context, TermsActivity.class);
                                startActivity(intent);
                                break;
                            default:
                                //opens the categories displayed in drawer
                                if (drawerItem.getIdentifier() > NAV_CATEGORIES) {
                                    Bundle b = new Bundle();
                                    b.putInt("Category_id", (int) (drawerItem.getIdentifier() - NAV_CATEGORIES));

                                }
                        }
                        drawer.closeDrawer();
                        return true;
                    }
                })
                .build();



        //load showauthorname
        Preference.load(context, "showauthorname", new Preference.onPreferenceDownloadedListener() {
            @Override
            public void onPreferenceDownloaded(String value) {
                //just load it. It is now cached
            }
        });
        //load showfeatureimage
        Preference.load(context, "showfeatureimage", new Preference.onPreferenceDownloadedListener() {
            @Override
            public void onPreferenceDownloaded(String value) {
                //just load it. It is now cached
            }
        });

    }


    public IDrawerItem[] getDrawerItems(List<ULocale.Category> categories) {
        List<IDrawerItem> drawerItems = new ArrayList<>();

        //TODO: You can change the order of the items in the Side Navigation Bar from here

        //add final 4 items
        if (Configurations.ENABLE_USER_SYSTEM)
            drawerItems.add(new SecondaryDrawerItem().withIdentifier(NAV_PROFILE).withName(R.string.profile_title).withIcon(FontAwesome.Icon.faw_user));
        drawerItems.add(new SecondaryDrawerItem().withIdentifier(NAV_INFO).withName(R.string.nav_info).withIcon(FontAwesome.Icon.faw_question));
//
        drawerItems.add(new SecondaryDrawerItem().withIdentifier(NAVSETTINGS).withName(R.string.nav_settings).withIcon(FontAwesome.Icon.faw_cog));
        if (Configurations.ENABLE_USER_SYSTEM) {
            if (User.isUserLoggedIn(activity))
                drawerItems.add(new SecondaryDrawerItem().withIdentifier(NAV_LOGOUT).withName(R.string.nav_logout).withIcon(FontAwesome.Icon.faw_sign_out));
        }
//
        return drawerItems.toArray(new IDrawerItem[0]);
    }
}
