package com.example.ty.sexywifi;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by LeonA on 2/24/2018.
 */

public class NetworkConnectivity extends Activity {
    private ConnectivityManager connectivityManager;
    private NetworkInfo activeNetworkInfo;

    public NetworkConnectivity(){
        connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    };

    public boolean isConnected() {
        return activeNetworkInfo.isConnected();
    }

    public int getNetworkType(){
        return activeNetworkInfo.getType();
    }
}
