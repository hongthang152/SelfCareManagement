package com.neurondigital.selfcare.treatment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.icu.util.ULocale;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.neurondigital.helpers.Utility;
import com.neurondigital.selfcare.BuildConfig;
import com.neurondigital.selfcare.Configurations;
import com.neurondigital.selfcare.CongestionTherapy;
import com.neurondigital.selfcare.Measurements;
import com.neurondigital.selfcare.PolicyActivity;
import com.neurondigital.selfcare.Preference;
import com.neurondigital.selfcare.ProfileActivity;
import com.neurondigital.selfcare.R;
import com.neurondigital.selfcare.SettingsActivity;
import com.neurondigital.selfcare.TermsActivity;
import com.neurondigital.selfcare.User;
import com.neurondigital.selfcare.infoactivity;
import com.neurondigital.selfcare.treatment.compressiontherapy.CTDatabase;
import com.neurondigital.selfcare.treatment.compressiontherapy.CTRecord;
import com.neurondigital.selfcare.treatment.exercise.Exercise;
import com.neurondigital.selfcare.treatment.exercise.ExerciseDatabase;
import com.neurondigital.selfcare.treatment.exercise.ExerciseModel;
import com.neurondigital.selfcare.treatment.manuallymphdrainagemassage.MLDDatabase;
import com.neurondigital.selfcare.treatment.manuallymphdrainagemassage.MLDModel;
import com.neurondigital.selfcare.treatment.pneumatic.Pneumatic;
import com.neurondigital.selfcare.treatment.pneumatic.PneumaticDatabase;
import com.neurondigital.selfcare.treatment.pneumatic.PneumaticModel;
import com.neurondigital.selfcare.treatment.skincare.SkinCare;
import com.neurondigital.selfcare.treatment.skincare.SkinCareDatabase;
import com.neurondigital.selfcare.treatment.skincare.SkinCareModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class TreatmentModuleFragment extends Fragment {

    Toolbar toolbar;
    Drawer drawer;
    Context context;
    View view;

    MLDDatabase mldDB;
    PneumaticDatabase pneumaticDB;
    SkinCareDatabase skinCareDB;
    CTDatabase compressionTherapyDB;
    ExerciseDatabase exerciseDB;

    LinearLayout latestMassageDetailContainer;
    TextView lastMassageDuration;
    TextView massageDaysAgoTextView;
    TextView lastMassageDetailedDurationTextView;

    //navigation drawer item identification numbers
    final int  NAV_INFO = 4, NAVSETTINGS = 6,  NAV_PROFILE = 8, NAV_LOGOUT = 9, NAV_CATEGORIES = 100, NAV_POLICY = 10, NAV_TERMS = 11;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_treatment_module, container, false);
        mldDB = new MLDDatabase(getContext());
        pneumaticDB = new PneumaticDatabase(getContext());
        skinCareDB = new SkinCareDatabase(getContext());
        compressionTherapyDB = new CTDatabase(getContext());
        exerciseDB = new ExerciseDatabase(getContext());

        context = getActivity();

        latestMassageDetailContainer = view.findViewById(R.id.latest_massage_detail_container);
        lastMassageDuration = view.findViewById(R.id.last_massage_duration);
        massageDaysAgoTextView = view.findViewById(R.id.massage_days_ago);
        lastMassageDetailedDurationTextView = view.findViewById(R.id.last_massage_detailed_duration);
        //enable/disable Firebase topic subscription
//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        if (sharedPref.getBoolean("pref_enable_push_notifications", true))
//            FirebaseMessaging.getInstance().subscribeToTopic(Configurations.FIREBASE_PUSH_NOTIFICATION_TOPIC);
//        else
//            FirebaseMessaging.getInstance().unsubscribeFromTopic(Configurations.FIREBASE_PUSH_NOTIFICATION_TOPIC);


        //set toolbar
//        toolbar = view.findViewById(R.id.toolbar);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//
//
//        //Generate the side navigation drawer
//        drawer = new DrawerBuilder()
//                .withActivity(getActivity())
//                .withToolbar(toolbar)
//                .withRootView(R.id.drawer_container)
//                //.withDisplayBelowStatusBar(true)
//                .withActionBarDrawerToggle(true)
//                .withActionBarDrawerToggleAnimated(true)
//                .addDrawerItems(getDrawerItems(null))
//                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
//                    @Override
//                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
//                        //On click: open the required activity or fragment
//                        Intent intent;
//                        switch ((int) drawerItem.getIdentifier()) {
//
//                            case NAV_INFO:
//                                intent = new Intent(getActivity(), infoactivity.class);
//                                startActivity(intent);
//                                break;
//
//                            case NAVSETTINGS:
//                                intent = new Intent(context, SettingsActivity.class);
//                                startActivity(intent);
//                                break;
//
//                            case NAV_PROFILE:
//                                if (User.isUserLoggedInElseTry(getActivity())) {
//                                    intent = new Intent(context, ProfileActivity.class);
//                                    startActivity(intent);
//                                }
//                                break;
//                            case NAV_LOGOUT:
//                                User.logout(context);
//                                break;
//                            case NAV_POLICY:
//                                intent = new Intent(context, PolicyActivity.class);
//                                startActivity(intent);
//                                break;
//                            case NAV_TERMS:
//                                intent = new Intent(context, TermsActivity.class);
//                                startActivity(intent);
//                                break;
//                            default:
//                                //opens the categories displayed in drawer
//                                if (drawerItem.getIdentifier() > NAV_CATEGORIES) {
//                                    Bundle b = new Bundle();
//                                    b.putInt("Category_id", (int) (drawerItem.getIdentifier() - NAV_CATEGORIES));
//
//                                }
//                        }
//                        drawer.closeDrawer();
//                        return true;
//                    }
//                })
//                .build();



        //load showauthorname
//        Preference.load(context, "showauthorname", new Preference.onPreferenceDownloadedListener() {
//            @Override
//            public void onPreferenceDownloaded(String value) {
//                //just load it. It is now cached
//            }
//        });
//        //load showfeatureimage
//        Preference.load(context, "showfeatureimage", new Preference.onPreferenceDownloadedListener() {
//            @Override
//            public void onPreferenceDownloaded(String value) {
//                //just load it. It is now cached
//            }
//        });

        RelativeLayout MLD = view.findViewById(R.id.MLD);
        Button LLIS = view.findViewById(R.id.LLIS);
        Button CT = view.findViewById(R.id.CT);
        Button skincare = view.findViewById(R.id.SC);
        Button Exe = view.findViewById(R.id.Exe);
        Button measure = view.findViewById(R.id.measure);
//        Button pneumaticbtn = view.findViewById(R.id.pneumatic);
//        Button sendDataEmail = view.findViewById(R.id.send_data_email);


        MLD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btn) {
                Intent goToMLD = new Intent(getActivity(), com.neurondigital.selfcare.treatment.manuallymphdrainagemassage.MLD.class);
                TreatmentModuleFragment.this.startActivity(goToMLD);
            }
        });

        MLDModel latestMLD = mldDB.getLatest();
        if(latestMLD == null) {
            latestMassageDetailContainer.setVisibility(View.INVISIBLE);
            lastMassageDetailedDurationTextView.setVisibility(View.INVISIBLE);
        } else {
            try {
                lastMassageDuration.setText(Utility.getReadableDuration(latestMLD.getDuration()));
                Date latestStartTime = MLDModel.DATE_FORMATTER.parse(latestMLD.getStartTime());
                massageDaysAgoTextView.setText(Utility.getDaysAgoStr(latestStartTime));
                lastMassageDetailedDurationTextView.setText(latestMLD.getDuration());
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }


//        LLIS.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View btn) {
//                Intent goToLLIS = new Intent(getActivity(), com.neurondigital.selfcare.LLIS.class);
//                TreatmentModuleFragment.this.startActivity(goToLLIS);
//            }
//        });

//        sendDataEmail.setOnClickListener((View view) -> {
//            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//            StrictMode.setVmPolicy(builder.build());
////            verifyStoragePermissions(getActivity());
//
//            Document document = new Document();
//            File encryptedPdfFile;
//            try {
//                encryptedPdfFile = new File(context.getFilesDir() + File.separator + "directory" + File.separator + "health-encrypted.pdf");
//                if(encryptedPdfFile.exists()) encryptedPdfFile.delete();
//                encryptedPdfFile.getParentFile().mkdirs();
//                encryptedPdfFile.createNewFile();
//                String key = Utility.generateRandomKey();
//
//                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(encryptedPdfFile));
//                writer.createXmpMetadata();
//                writer.setEncryption(key.getBytes(), key.getBytes(), PdfWriter.ALLOW_COPY, PdfWriter.STANDARD_ENCRYPTION_40);
//
//                document.open();
//                document.add(new Paragraph("Manual Lymph Drainage Massage sessions: "));
//
//                for(MLDModel mld : mldDB.getAll()) {
//                    document.add(new Paragraph(mld.getStartTime() + " - " + mld.getEndTime() + ". Duration: " + mld.getDuration()));
//                }
//                document.add(new Paragraph("\n"));
//
//                document.add(new Paragraph("Pneumatic Compression Pump sessions: "));
//                for(PneumaticModel pneumaticModel : pneumaticDB.getAll()) {
//                    document.add(new Paragraph(pneumaticModel.getStartTime() + " - " + pneumaticModel.getEndTime() + ". Duration: " + pneumaticModel.getDuration()));
//                }
//                document.add(new Paragraph("\n"));
//
//                document.add(new Paragraph("Skin Care sessions"));
//                for(Map<String, String> sc : skinCareDB.getAll()) {
//                    document.add(new Paragraph("Date/Time: " + sc.get("Date") + ". Note: " + sc.get("Note")));
//                }
//                document.add(new Paragraph("\n"));
//
//                document.add(new Paragraph("Exercise sessions"));
//                for(ExerciseModel exercise : exerciseDB.getAll()) {
//                    document.add(new Paragraph(exercise.getStartTime() + " - " + exercise.getEndTime() + ". Duration: " + exercise.getDuration()));
//                }
//                document.add(new Paragraph("\n"));
//
//                document.add(new Paragraph("Compression Therapy sessions"));
//                for(CTRecord ct : compressionTherapyDB.getAllCTRecords()) {
//                    document.add(new Paragraph(ct.getDaynightTime() + ". " + ct.getStartTime() + " - " + ct.getEndTime() + ". Duration: " + ct.getDuration()));
//                }
//
//
//
//                document.close();
//
//                new AlertDialog.Builder(context)
//                        .setTitle(getString(R.string.file_password))
//                        .setMessage(getString(R.string.file_password_message) + "\n\n" + key)
//                        .setPositiveButton(R.string.OK, (DialogInterface dialog, int which) -> {
//                            Intent it = new Intent(Intent.ACTION_SEND);
//                            it.setData(Uri.parse("mailto:"));
//                            it.setType("text/pdf");
//
//                            it.putExtra(Intent.EXTRA_EMAIL, new String[]{"nguy0817@algonquinlive.com"});
//                            it.putExtra(Intent.EXTRA_SUBJECT,"Selfcare Management Report");
//                            it.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(getContext(), "com.neurondigital.selfcare", encryptedPdfFile));
//                            startActivity(it);
//                        })
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .show();
//
//            } catch (DocumentException e) {
//                e.printStackTrace();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });

//        CT.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View btn) {
//                Intent goToCT = new Intent(getActivity(), CongestionTherapy.class);
//                TreatmentModuleFragment.this.startActivity(goToCT);
//            }
//        });
//        skincare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View btn) {
//                Intent goToSC = new Intent(getActivity(), SkinCare.class);
//                TreatmentModuleFragment.this.startActivity(goToSC);
//            }
//        });
//        Exe.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View btn) {
//                Intent goToExe = new Intent(getActivity(), Exercise.class);
//                TreatmentModuleFragment.this.startActivity(goToExe);
//            }
//        });
//        measure.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View btn) {
//                Intent goToMeasure = new Intent(getActivity(), Measurements.class);
//                TreatmentModuleFragment.this.startActivity(goToMeasure);
//
//
//
//            }
//        });

//        pneumaticbtn.setOnClickListener(v ->{
//            Intent pn = new Intent(getActivity(), Pneumatic.class);
//            TreatmentModuleFragment.this.startActivity(pn);
//        });


        return view;
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
            if (User.isUserLoggedIn(getActivity()))
                drawerItems.add(new SecondaryDrawerItem().withIdentifier(NAV_LOGOUT).withName(R.string.nav_logout).withIcon(FontAwesome.Icon.faw_sign_out));
        }
//
        return drawerItems.toArray(new IDrawerItem[0]);
    }


//    public void changeFragment(Fragment fragment) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.commit();
//        activity.invalidateOptionsMenu();
//    }
//
//
//    @Override
//    public void onBackPressed() {
//        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
//
//            super.onBackPressed();
//        }
//    }




//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        List<Fragment> fragments = getSupportFragmentManager().getFragments();
//        if (fragments != null) {
//            for (Fragment fragment : fragments) {
//                if (fragment != null) {
//                    fragment.onActivityResult(requestCode, resultCode, data);
//                }
//            }
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grants) {
//        List<Fragment> fragments = getSupportFragmentManager().getFragments();
//        if (fragments != null) {
//            for (Fragment fragment : fragments) {
//                if (fragment != null) {
//                    fragment.onRequestPermissionsResult(requestCode, permissions, grants);
//                }
//            }
//        }
//    }

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
    public void onStop() {
        super.onStop();

    }



}
