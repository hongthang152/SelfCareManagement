package com.neurondigital.selfcare.treatment.skincare;
import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.HashMap;
@RunWith(RobolectricTestRunner.class)

public class SkinCareDatabaseTest {

    SkinCareDatabase db;
    ArrayList<HashMap<String, String>> scList;

    @Before
    public void setUp() throws Exception {
        db = new SkinCareDatabase(ApplicationProvider.getApplicationContext());
        scList = new ArrayList();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void addmodel() {
        SkinCareModel model = new SkinCareModel();

        model.setDate("2021-03-31");
        model.setNote("Lotion");
        db.addmodel(model);

        scList = db.getAll();
        assertEquals(scList.size(), 1 );

    }

    @Test
    public void getAll() {
        scList = db.getAll();
        assertEquals(scList.size(), 0 );

        SkinCareModel model = new SkinCareModel();
        SkinCareModel model2 = new SkinCareModel();
        model.setNote("Note");
        model.setDate("testDate");
        model2.setNote("Note");
        model2.setDate("testDate");

        db.addmodel(model);
        db.addmodel(model2);

        scList = db.getAll();
        assertEquals(scList.size(), 2 );

    }

    @Test
    public void getModel() {
        SkinCareModel model = new SkinCareModel();
        model.setDate("2021-03-31");
        model.setNote("Lotion");
        db.addmodel(model);

        scList = db.getAll();
        String id = scList.get(0).get("_id");

        assertEquals(db.getModel(Integer.parseInt(id)).getDate(), "2021-03-31");
        assertEquals(db.getModel(Integer.parseInt(id)).getNote(), "Lotion");

    }

    @Test
    public void deleterow() {
        scList = db.getAll();
        assertEquals(scList.size(), 0 );

        SkinCareModel model = new SkinCareModel();
        SkinCareModel model2 = new SkinCareModel();
        model.setNote("Note");
        model.setDate("testDate");
        model2.setNote("Note");
        model2.setDate("testDate");

        db.addmodel(model);
        db.addmodel(model2);

        scList = db.getAll();
        assertEquals(scList.size(), 2 );

        String id1 = scList.get(0).get("_id");
        String id2 = scList.get(1).get("_id");

        db.deleterow(Integer.parseInt(id1));

        scList = db.getAll();
        assertEquals(scList.size(), 1 );
        assertEquals(scList.get(0).get("_id"), id2);

    }


    @Test
    public void update() {
        SkinCareModel model = new SkinCareModel();
        model.setDate("2021-03-31");
        model.setNote("Lotion");
        db.addmodel(model);

        scList = db.getAll();
        assertEquals(scList.get(0).get("Date"), "2021-03-31");
        assertEquals(scList.get(0).get("Note"), "Lotion");

        String id = scList.get(0).get("_id");

        SkinCareModel modelToUpdate = db.getModel(Integer.parseInt(id));
        modelToUpdate.setDate("updatedDate");
        modelToUpdate.setNote("updatedNote");

        db.update(modelToUpdate);

        scList = db.getAll();
        assertEquals(scList.get(0).get("Date"), "updatedDate");
        assertEquals(scList.get(0).get("Note"), "updatedNote");
    }

    @Test
    public void deleteAll() {
        scList = db.getAll();
        assertEquals(scList.size(), 0 );

        SkinCareModel model = new SkinCareModel();
        SkinCareModel model2 = new SkinCareModel();
        model.setNote("Note");
        model.setDate("testDate");
        model2.setNote("Note");
        model2.setDate("testDate");

        db.addmodel(model);
        db.addmodel(model2);

        scList = db.getAll();
        assertEquals(scList.size(), 2 );

        db.deleteAll();

        scList = db.getAll();
        assertEquals(scList.size(), 0);
    }

}