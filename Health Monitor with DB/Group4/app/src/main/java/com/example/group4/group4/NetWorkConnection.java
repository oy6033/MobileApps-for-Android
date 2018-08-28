package com.example.group4.group4;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class NetWorkConnection {
    public static boolean NetWrokConnection(Context context){
        try {
            ConnectivityManager connMgrCheck = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = connMgrCheck.getActiveNetworkInfo();
            if (netInfo!=null&&netInfo.isConnected())
                return true;
            }
         catch (Exception e) {
             e.printStackTrace();
         }
        return false;
    }

}
