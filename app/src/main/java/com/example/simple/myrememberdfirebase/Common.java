package com.example.simple.myrememberdfirebase;

import com.example.simple.myrememberdfirebase.Remote.APIService;
import com.example.simple.myrememberdfirebase.Remote.RetrofitClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.InstanceIdResult;

public class Common {

    public static String year = "";
    public static boolean flag_year = false;

    public static String currentToken = "";

    private static String baseUrl = "https://fcm.googleapis.com/";

    public static APIService getFCMClient(){
        return RetrofitClient.getClient(baseUrl).create(APIService.class);
    }

}//Common
