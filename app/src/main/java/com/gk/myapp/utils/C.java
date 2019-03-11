package com.gk.myapp.utils;

/**
 * Created by abc on 5/10/2016.
 */
public class C {

    //    image url
    public static final String IMAGE_BASE_URL = "http://www.tiwana.in/admin/application/upload/user_image/";
    public static final String BASE_URL_FILES = "http://www.tiwana.in/admin/application/upload/files/";
    //    public static final String BASE_URL_PRODUCT_IMAGES = "http://www.tiwana.in/admin/application/upload/product_image/";
    public static final String BASE_URL_PRODUCT_IMAGES = "http://tiwana.in/admin/application/upload/product_image/";

    public static final String GM = "1"; //9001
    public static final String ASM = "2"; //9002
    public static final String SO = "3"; //9003
    public static final String IT_ADMIN = "4"; //itGujrat@gmail.com
    public static final String OTHER_USER = "7"; //100011   123456789
    public static final String CONSUMER = "9";  //g@gmail.com qwerty  user_id: 35

    //leave status
    public static final String PENDING = "1";
    public static final String APPROVE = "2";
    public static final String CANCEL = "3"; //Rejected
    public static final String CANCEL_BY_ME = "4"; //cancelled

    //notification type
    public static final String SALES = "1";
    public static final String FINANCE = "2";

    //payment modes
    public static final String CASH = "3";
    public static final String CHEQUE = "1";
    public static final String NEFT = "2";
    public static final String IMPS = "5";
    public static final String ONLINE = "4";
    public static final String RTGS = "6";

    //    Job status
    public static final int JOB_STARTED = 1;
    public static final int JOB_STOPPED = 2;
    public static final int JOB_PAUSED = 3;
    public static final int JOB_RESUMED = 4;

    //order status
    public static final String ORDER_PENDING = "1";
    public static final String ORDER_COMPLETE = "2";
    public static final String ORDER_CANCELLED = "3";

    //designs
    public static final float BUTTON_HEIGHT = 3f;
    public static final float BUTTON_WIDTH = 3f;
    public static final int BUTTON_MARGIN = 18;

    //Location Service time
    public static final int LOCATION_UPDATE_TIME = 2 * 60 * 1000;

    //retrofit
    public static final int API_TIMEOUT=10*60;

    //fonts
    public static final String LATO_LIGHT = "fonts/Lato-Light.ttf";
    public static final String ROBOTO_ITALIC = "fonts/roboto_light_italic.ttf";
    public static final String ROBOTO = "fonts/roboto_regular.ttf";
    public static final String TITIL_SEMI_BOLD = "fonts/TitilliumWeb-SemiBold.ttf";
    public static final String TITIL = "fonts/TitilliumWeb-Regular.ttf";

    public static String KG = " KG";
}

