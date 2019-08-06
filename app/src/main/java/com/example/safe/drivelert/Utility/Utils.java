package com.example.safe.drivelert.Utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {


    public static boolean isInternetAvailable(Context context) {
         boolean flag = false,mobile=false,wifi=false;

        ConnectivityManager manager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activenetwork=manager.getActiveNetworkInfo();
        if(activenetwork!=null){
            mobile=activenetwork.getType()== ConnectivityManager.TYPE_MOBILE;
            wifi=activenetwork.getType()== ConnectivityManager.TYPE_WIFI;
            if(activenetwork.isConnected()||activenetwork.isConnectedOrConnecting()){
                if(wifi||mobile){
                    flag=true;
                }
            }
        }

        return flag;
    }

}
