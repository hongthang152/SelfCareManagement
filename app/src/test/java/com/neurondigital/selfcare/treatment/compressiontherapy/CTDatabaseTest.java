package com.neurondigital.selfcare.treatment.compressiontherapy;

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
public class CTDatabaseTest {

    CTDatabase db;
    List<CTRecord> ctList;

    @Before
    public void setUp() throws Exception {
        db = new CTDatabase(ApplicationProvider.getApplicationContext());
        ctList = new ArrayList();
    }


    @Test
    public void addCTRecord() {
        CTRecord model = new CTRecord();

        model.setDuration("30");
        model.setStartTime("2021-04-02");
        model.setEndTime("2021-04-03");
        db.addCTRecord(model);

        ctList = db.getAllCTRecords();
        assertEquals(ctList.size(), 1 );

    }

    @Test
    public void update() {
        CTRecord model = new CTRecord();

        model.setDuration("30");
        model.setStartTime("2021-04-02");
        model.setEndTime("2021-04-03");
        db.addCTRecord(model);

        ctList = db.getAllCTRecords();
        assertEquals(ctList.get(0).getStartTime(), "2021-04-02");
        assertEquals(ctList.get(0).getEndTime(), "2021-04-03");
        assertEquals(ctList.get(0).getDuration(), "30");

        CTRecord modelToUpdate = ctList.get(0);
        modelToUpdate.setStartTime("newStart");
        modelToUpdate.setEndTime("newEnd");
        modelToUpdate.setDuration("60");

        db.updateCTRecord(modelToUpdate);

        ctList = db.getAllCTRecords();
        assertEquals(ctList.get(0).getStartTime(), "newStart");
        assertEquals(ctList.get(0).getEndTime(), "newEnd");
        assertEquals(ctList.get(0).getDuration(), "60");
    }

    @Test
    public void getAll() {
        ctList = db.getAllCTRecords();
        assertEquals(ctList.size(), 0 );

        CTRecord model = new CTRecord();
        CTRecord model2 = new CTRecord();
        model.setDuration("30");
        model.setStartTime("2021-04-02");
        model.setEndTime("2021-04-03");
        model2.setDuration("35");
        model2.setStartTime("2021-04-02");
        model2.setEndTime("2021-04-03");

        db.addCTRecord(model);
        db.addCTRecord(model2);

        ctList = db.getAllCTRecords();
        assertEquals(ctList.size(), 2 );

    }


}