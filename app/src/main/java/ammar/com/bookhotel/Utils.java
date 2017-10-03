package ammar.com.bookhotel;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import ammar.com.bookhotel.Object.ProfilePriceObject;
import ammar.com.bookhotel.Object.Touch_objects;
import ammar.com.bookhotel.Object.cart_object;
import ammar.com.bookhotel.Object.rooms_object;


public class Utils{
    public static int cart_time =0;
    public static Boolean iseasy = false;
    public static List<ProfilePriceObject> hotel_list=new ArrayList<ProfilePriceObject>();
    public static List<rooms_object> rooms_list=new ArrayList<rooms_object>();
    public static List<ProfilePriceObject> rooms_quantity_list=new ArrayList<ProfilePriceObject>();
    public static List<ProfilePriceObject> adult_list=new ArrayList<ProfilePriceObject>();
    public static List<ProfilePriceObject> child_list=new ArrayList<ProfilePriceObject>();
    public static int time_on_easy_and_difficult = 0;
    public static String easy_user_name = "" ;
    public static int register_time = 0;
    public static int login = 0;
    public static List<Touch_objects> splash=new ArrayList<Touch_objects>();
    public static List<Touch_objects> easy_difficult=new ArrayList<Touch_objects>();
    public static List<Touch_objects> login_touches=new ArrayList<Touch_objects>();
    public static List<Touch_objects> register_touches=new ArrayList<Touch_objects>();
    public static int no_of_clicks_email = 0;
    public static int no_of_clicks_password = 0;
    public static int no_of_clicks_login_button = 0;
    public static int no_of_clicks_register_button = 0;
    public static double latitude;
    public static double longitude;
    public static List<cart_object> crt_obj = new ArrayList<>();
    public static int Total_hotels_book=0;
    public static int Total_hotels_book_deleted=0;
    public static boolean firstone = true;

    public static String start_scrn = "";
    public static String end_scrn = "";

    public static int mint,hour;
    public static String sap = "";

}