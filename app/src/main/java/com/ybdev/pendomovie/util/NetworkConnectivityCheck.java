package com.ybdev.pendomovie.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import static android.net.ConnectivityManager.TYPE_ETHERNET;
import static android.net.ConnectivityManager.TYPE_MOBILE;
import static android.net.ConnectivityManager.TYPE_WIFI;

public class NetworkConnectivityCheck {


    public static boolean checkNetworkConnectivity(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            Network network = connectivityManager.getActiveNetwork();

            //no network
            if (network == null)
                return false;

            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);

            if (networkCapabilities == null)
                return false;

            return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
        }
        else{
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            switch (networkInfo.getType()){
                case TYPE_WIFI:
                case TYPE_MOBILE:
                case TYPE_ETHERNET:
                    return true;
                default:
                    return false;
            }
        }
    }
}
