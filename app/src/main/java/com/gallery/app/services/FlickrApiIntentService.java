package com.gallery.app.services;

import android.app.IntentService;
import android.content.Intent;

import com.gallery.app.FlickrServiceContract;
import com.gallery.app.constants.AppConstants;
import com.gallery.app.constants.UrlConstants;
import com.gallery.app.helpers.FlickrRestServiceGenerator;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class FlickrApiIntentService extends IntentService {

    public FlickrServiceContract flickrServiceContract = null;

    public FlickrApiIntentService() {
        super("FlickrApiService");
        flickrServiceContract = FlickrRestServiceGenerator.createRestApiServiceGenerator(FlickrServiceContract.class, UrlConstants.FLICKR_API_URL);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String searchText = intent.getStringExtra(AppConstants.SEARCH_TEXT);
        int pageNumber = intent.getIntExtra(AppConstants.PAGE_NUM, 1);
        getPhotos(searchText, pageNumber);
    }

    public void getPhotos(String searchText, int pageNumber) {
        String apiKey = AppConstants.FLICKR_API_KEY;
        String method = AppConstants.FLICKR_SEARCH_PHOTOS_API_METHOD;
        int perPage = AppConstants.PHOTOS_PER_PAGE;
        String format = AppConstants.JSON;

        Call<Object> call = flickrServiceContract.getPhotos(apiKey, method, searchText, searchText, perPage, pageNumber, format);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Response<Object> response, Retrofit retrofit) {
                Object registerMobileNumberResponse = response.body();
                if (registerMobileNumberResponse != null) {
//                    registerMobileNumberResponse.status = true;
//                    EventBus.getDefault().post(registerMobileNumberResponse);
                } else {
                    broadcastError();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                broadcastError();
            }

            private void broadcastError() {

            }
        });
    }
}
