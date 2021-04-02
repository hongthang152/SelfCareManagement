package com.neurondigital.selfcare.treatment.exercise;

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
public class ExerciseDatabaseTest {

    ExerciseDatabase db;
    List<ExerciseModel> exList;

    @Before
    public void setUp() throws Exception {
        db = new ExerciseDatabase(ApplicationProvider.getApplicationContext());
        exList = new ArrayList();
    }


    @Test
    public void addmodel() {
        ExerciseModel model = new ExerciseModel();

        model.setDuration("30");
        model.setStartTime("2021-04-02");
        model.setEndTime("2021-04-03");
        db.addmodel(model);

        exList = db.getAll();
        assertEquals(exList.size(), 1 );

    }

    @Test
    public void update() {
        ExerciseModel model = new ExerciseModel();

        model.setDuration("30");
        model.setStartTime("2021-04-02");
        model.setEndTime("2021-04-03");
        db.addmodel(model);

        exList = db.getAll();
        assertEquals(exList.get(0).getStartTime(), "2021-04-02");
        assertEquals(exList.get(0).getEndTime(), "2021-04-03");
        assertEquals(exList.get(0).getDuration(), "30");

        ExerciseModel modelToUpdate = exList.get(0);
        modelToUpdate.setStartTime("newStart");
        modelToUpdate.setEndTime("newEnd");
        modelToUpdate.setDuration("60");

        db.update(modelToUpdate);

        exList = db.getAll();
        assertEquals(exList.get(0).getStartTime(), "newStart");
        assertEquals(exList.get(0).getEndTime(), "newEnd");
        assertEquals(exList.get(0).getDuration(), "60");
    }

    @Test
    public void getAll() {
        exList = db.getAll();
        assertEquals(exList.size(), 0 );

        ExerciseModel model = new ExerciseModel();
        ExerciseModel model2 = new ExerciseModel();
        model.setDuration("30");
        model.setStartTime("2021-04-02");
        model.setEndTime("2021-04-03");
        model2.setDuration("35");
        model2.setStartTime("2021-04-02");
        model2.setEndTime("2021-04-03");

        db.addmodel(model);
        db.addmodel(model2);

        exList = db.getAll();
        assertEquals(exList.size(), 2 );

    }


    @Test
    public void deleterow() {
        exList = db.getAll();
        assertEquals(exList.size(), 0 );

        ExerciseModel model = new ExerciseModel();
        ExerciseModel model2 = new ExerciseModel();
        model.setDuration("30");
        model.setStartTime("2021-04-02");
        model.setEndTime("2021-04-03");
        model2.setDuration("35");
        model2.setStartTime("2021-04-02");
        model2.setEndTime("2021-04-03");

        db.addmodel(model);
        db.addmodel(model2);

        exList = db.getAll();
        assertEquals(exList.size(), 2 );

        int id1 = exList.get(0).getID();
        int id2 = exList.get(1).getID();

        db.deleterow(id1);

        exList = db.getAll();
        assertEquals(exList.size(), 1 );
        assertEquals(exList.get(0).getID(), id2);
    }
}