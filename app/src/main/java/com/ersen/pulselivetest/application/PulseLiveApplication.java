package com.ersen.pulselivetest.application;

import android.app.Application;

import com.ersen.pulselivetest.constants.PulseLiveConstants;
import com.ersen.pulselivetest.network.PulseLiveAPI;

import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

public class PulseLiveApplication extends Application {
    private static PulseLiveApplication sInstance; //Get this application instance
    private Retrofit mRetrofitClient;
    private PulseLiveAPI mNetworkAPI;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static PulseLiveApplication getInstance() {
        return sInstance;
    }

    public PulseLiveAPI getPulseLiveAPI(){
        if(mNetworkAPI == null){
            mNetworkAPI = getRetrofitClient().create(PulseLiveAPI.class);
        }
        return mNetworkAPI;
    }

    private Retrofit getRetrofitClient(){
        if (mRetrofitClient == null){
            mRetrofitClient = new Retrofit.Builder()
                    .baseUrl(PulseLiveConstants.URLConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return mRetrofitClient;
    }
}
