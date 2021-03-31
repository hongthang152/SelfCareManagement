package com.neurondigital.selfcare;

/**
 * Created by melvin on 25/09/2016.
 */
public class Configurations {


    public static String SERVER_URL = "http://10.0.2.2:3000/";
//    public static String SERVER_URL = "http://cityguide.neurondigital.com/";

    public final static boolean ENABLE_USER_SYSTEM = true;

    //enable news
    public final static boolean ENABLE_NEWS = true;
    public final static boolean SHOW_SPLASH_SCREEN_BACKGROUND_IMAGE = false;

    public final static int LIST_2COLUMNS = 1, LIST_1COLUMNS = 2;
    public final static int LIST_MENU_TYPE = LIST_2COLUMNS;

    public final static String FIREBASE_PUSH_NOTIFICATION_TOPIC = "news";

    public final static String[] TEST_DEVICES = new String[]{};

}
