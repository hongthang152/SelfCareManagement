package com.neurondigital.selfcare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.icu.util.ULocale;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;

import com.google.firebase.messaging.FirebaseMessaging;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        this.context = this;

        //portrait only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



        //enable/disable Firebase topic subscription
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPref.getBoolean("pref_enable_push_notifications", true))
            FirebaseMessaging.getInstance().subscribeToTopic(Configurations.FIREBASE_PUSH_NOTIFICATION_TOPIC);
        else
            FirebaseMessaging.getInstance().unsubscribeFromTopic(Configurations.FIREBASE_PUSH_NOTIFICATION_TOPIC);


        //set toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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

        Button MLD = findViewById(R.id.MLD);
        Button LLIS = findViewById(R.id.LLIS);
        Button CT = findViewById(R.id.CT);
        Button skincare = findViewById(R.id.SC);
        Button Exe = findViewById(R.id.Exe);
        Button measure = findViewById(R.id.measure);


        MLD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btn) {
                Intent goToMLD = new Intent(MainActivity.this, com.neurondigital.selfcare.MLD.MLD.class);
                MainActivity.this.startActivity(goToMLD);
            }
        });

        LLIS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btn) {
                Intent goToLLIS = new Intent(MainActivity.this, LLIS.class);
                MainActivity.this.startActivity(goToLLIS);
            }
        });
        CT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btn) {
                Intent goToCT = new Intent(MainActivity.this, CongestionTherapy.class);
                MainActivity.this.startActivity(goToCT);
            }
        });
        skincare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btn) {
                Intent goToSC = new Intent(MainActivity.this, SkinCare.class);
                MainActivity.this.startActivity(goToSC);
            }
        });
        Exe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btn) {
                Intent goToExe = new Intent(MainActivity.this, Exe.class);
                MainActivity.this.startActivity(goToExe);
            }
        });
        measure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btn) {
                Intent goToMeasure = new Intent(MainActivity.this, Measurements.class);
                MainActivity.this.startActivity(goToMeasure);
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


    public void changeFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.commit();
        activity.invalidateOptionsMenu();
    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {

            super.onBackPressed();
        }
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null) {
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grants) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null) {
                    fragment.onRequestPermissionsResult(requestCode, permissions, grants);
                }
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {

        super.onDestroy();

    }

    @Override
    protected void onStop() {
        super.onStop();


    }

}
