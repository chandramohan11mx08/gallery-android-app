package com.gallery.app.services;

import android.app.IntentService;
import android.content.Intent;

import com.gallery.app.FlickrServiceContract;
import com.gallery.app.constants.AppConstants;
import com.gallery.app.constants.UrlConstants;
import com.gallery.app.helpers.FlickrRestServiceGenerator;
import com.gallery.app.models.SearchPhotosResponse;
import com.gallery.app.models.response.SearchPhotosResponseEvent;

import de.greenrobot.event.EventBus;
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
        int noJsonCallBack = 1;

        Call<SearchPhotosResponse> call = flickrServiceContract.getPhotos(apiKey, method, searchText, searchText, perPage, pageNumber, format, noJsonCallBack);
        call.enqueue(new Callback<SearchPhotosResponse>() {
            @Override
            public void onResponse(Response<SearchPhotosResponse> response, Retrofit retrofit) {
                SearchPhotosResponse searchPhotosResponse =  response.body();
                if (searchPhotosResponse != null) {
                    SearchPhotosResponseEvent searchPhotosResponseEvent = new SearchPhotosResponseEvent();
                    searchPhotosResponseEvent.searchPhotosResponse = searchPhotosResponse;
                    searchPhotosResponseEvent.status = true;
                    EventBus.getDefault().post(searchPhotosResponseEvent);
                } else {
                    broadcastError();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                broadcastError();
            }

            private void broadcastError() {
                SearchPhotosResponseEvent searchPhotosResponseEvent = new SearchPhotosResponseEvent();
                EventBus.getDefault().post(searchPhotosResponseEvent);
            }
        });
    }
}
