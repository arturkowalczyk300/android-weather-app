package com.arturkowalczyk300.mvvmlivedataexampleproject;

import android.content.Context;
import android.net.ConnectivityManager;

import androidx.annotation.NonNull;

public class Utils {
    public static boolean isInternetConnectionAvailable(@NonNull Context context)
    {
        return ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo() != null;
    }
}
