package com.gallery.app.helpers;


import com.facebook.stetho.okhttp.StethoInterceptor;
import com.gallery.app.BuildConfig;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class FlickrRestServiceGenerator {

    private static int HTTP_CONNECTION_TIMEOUT_SECONDS = 15;
    private static int HTTP_READ_TIMEOUT_SECONDS = 15;

    private FlickrRestServiceGenerator(){}

    public static <T> T createRestApiServiceGenerator(Class<T> serviceClass,
                                                      String baseUrl){
        OkHttpClient okHttpClient = new OkHttpClient();
        if (BuildConfig.DEBUG) {
            okHttpClient.networkInterceptors().add(new StethoInterceptor());
        }

        okHttpClient.setConnectTimeout(HTTP_CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(HTTP_READ_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        return retrofit.create(serviceClass);
    }
}
