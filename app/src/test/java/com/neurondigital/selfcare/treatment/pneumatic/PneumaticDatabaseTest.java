package com.neurondigital.selfcare.treatment.pneumatic;

import org.junit.Test;

import static org.junit.Assert.*;
import org.junit.Test;

import static org.junit.Assert.*;

import org.junit.Test;
import static org.junit.Assert.*;
import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

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
public class PneumaticDatabaseTest {

    PneumaticDatabase db;
    List<PneumaticModel> pnList;

    @Before
    public void setUp() throws Exception {
        db = new PneumaticDatabase(ApplicationProvider.getApplicationContext());
        pnList = new ArrayList();
    }


    @Test
    public void addmodel() {
        PneumaticModel model = new PneumaticModel();

        model.setDuration("30");
        model.setStartTime("2021-04-02");
        model.setEndTime("2021-04-03");
        db.addModel(model);

        pnList = db.getAll();
        assertEquals(pnList.size(), 1 );

    }

    @Test
    public void update() {
        PneumaticModel model = new PneumaticModel();

        model.setDuration("30");
        model.setStartTime("2021-04-02");
        model.setEndTime("2021-04-03");
        db.addModel(model);

        pnList = db.getAll();
        assertEquals(pnList.get(0).getStartTime(), "2021-04-02");
        assertEquals(pnList.get(0).getEndTime(), "2021-04-03");
        assertEquals(pnList.get(0).getDuration(), "30");

        PneumaticModel modelToUpdate = pnList.get(0);
        modelToUpdate.setStartTime("newStart");
        modelToUpdate.setEndTime("newEnd");
        modelToUpdate.setDuration("60");

        db.update(modelToUpdate);

        pnList = db.getAll();
        assertEquals(pnList.get(0).getStartTime(), "newStart");
        assertEquals(pnList.get(0).getEndTime(), "newEnd");
        assertEquals(pnList.get(0).getDuration(), "60");
    }

    @Test
    public void getAll() {
        pnList = db.getAll();
        assertEquals(pnList.size(), 0 );

        PneumaticModel model = new PneumaticModel();
        PneumaticModel model2 = new PneumaticModel();
        model.setDuration("30");
        model.setStartTime("2021-04-02");
        model.setEndTime("2021-04-03");
        model2.setDuration("35");
        model2.setStartTime("2021-04-02");
        model2.setEndTime("2021-04-03");

        db.addModel(model);
        db.addModel(model2);

        pnList = db.getAll();
        assertEquals(pnList.size(), 2 );

    }


    @Test
    public void recordDelete() {
        pnList = db.getAll();
        assertEquals(pnList.size(), 0 );

        PneumaticModel model = new PneumaticModel();
        PneumaticModel model2 = new PneumaticModel();
        model.setDuration("30");
        model.setStartTime("2021-04-02");
        model.setEndTime("2021-04-03");
        model2.setDuration("35");
        model2.setStartTime("2021-04-02");
        model2.setEndTime("2021-04-03");

        db.addModel(model);
        db.addModel(model2);

        pnList = db.getAll();
        assertEquals(pnList.size(), 2 );

        db.recordDelete(model);

        pnList = db.getAll();
        //assertEquals(pnList.size(), 1 );
        //assertEquals(pnList.get(0), model2);
    }
}