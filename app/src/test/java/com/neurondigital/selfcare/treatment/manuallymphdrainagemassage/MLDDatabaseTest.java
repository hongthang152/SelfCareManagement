package com.neurondigital.selfcare.treatment.manuallymphdrainagemassage;

import org.junit.Test;

import static org.junit.Assert.*;
import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.neurondigital.selfcare.treatment.skincare.SkinCareDatabase;

import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class MLDDatabaseTest {

    MLDDatabase db;
    List<MLDModel> mldList;

    @Before
    public void setUp() throws Exception {
        db = new MLDDatabase(ApplicationProvider.getApplicationContext());
        mldList = new ArrayList();
    }


    @Test
    public void addmodel() {
        MLDModel model = new MLDModel();

        model.setDuration("30");
        model.setStartTime("2021-04-02");
        model.setEndTime("2021-04-03");
        db.addmodel(model);

        mldList = db.getAll();
        assertEquals(mldList.size(), 1 );

    }

    @Test
    public void update() {
        MLDModel model = new MLDModel();

        model.setDuration("30");
        model.setStartTime("2021-04-02");
        model.setEndTime("2021-04-03");
        db.addmodel(model);

        mldList = db.getAll();
        assertEquals(mldList.get(0).getStartTime(), "2021-04-02");
        assertEquals(mldList.get(0).getEndTime(), "2021-04-03");
        assertEquals(mldList.get(0).getDuration(), "30");

        MLDModel modelToUpdate = mldList.get(0);
        modelToUpdate.setStartTime("newStart");
        modelToUpdate.setEndTime("newEnd");
        modelToUpdate.setDuration("60");

        db.update(modelToUpdate);

        mldList = db.getAll();
        assertEquals(mldList.get(0).getStartTime(), "newStart");
        assertEquals(mldList.get(0).getEndTime(), "newEnd");
        assertEquals(mldList.get(0).getDuration(), "60");
    }

    @Test
    public void getAll() {
        mldList = db.getAll();
        assertEquals(mldList.size(), 0 );

        MLDModel model = new MLDModel();
        MLDModel model2 = new MLDModel();
        model.setDuration("30");
        model.setStartTime("2021-04-02");
        model.setEndTime("2021-04-03");
        model2.setDuration("35");
        model2.setStartTime("2021-04-02");
        model2.setEndTime("2021-04-03");

        db.addmodel(model);
        db.addmodel(model2);

        mldList = db.getAll();
        assertEquals(mldList.size(), 2 );

    }


    @Test
    public void deleterow() {
        mldList = db.getAll();
        assertEquals(mldList.size(), 0 );

        MLDModel model = new MLDModel();
        MLDModel model2 = new MLDModel();
        model.setDuration("30");
        model.setStartTime("2021-04-02");
        model.setEndTime("2021-04-03");
        model2.setDuration("35");
        model2.setStartTime("2021-04-02");
        model2.setEndTime("2021-04-03");

        db.addmodel(model);
        db.addmodel(model2);

        mldList = db.getAll();
        assertEquals(mldList.size(), 2 );

        int id1 = mldList.get(0).getID();
        int id2 = mldList.get(1).getID();

        db.deleterow(id1);

        mldList = db.getAll();
        assertEquals(mldList.size(), 1 );
        assertEquals(mldList.get(0).getID(), id2);
    }
}