package com.arturkowalczyk300.mvvmlivedataexampleproject.ModelApi;

public class ConnectionModel{
    private int type;
    private boolean isConnected;

    public ConnectionModel(int type, boolean isConnected) {
        this.type = type;
        this.isConnected = isConnected;
    }

    public int getType() {
        return type;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public static final int WifiData = 1;
    public static final int MobileData = 2;
}
