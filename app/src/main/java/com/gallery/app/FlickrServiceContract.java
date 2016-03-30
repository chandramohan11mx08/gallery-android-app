package com.gallery.app;

import com.gallery.app.models.SearchPhotosResponse;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Query;

public interface FlickrServiceContract {

    @Headers({
            "Accept: application/json"
    })
    @GET("/services/rest")
    public Call<SearchPhotosResponse> getPhotos(@Query("api_key") String apiKey, @Query("method") String method, @Query("tags") String tags, @Query("text") String text, @Query("per_page") int perPage, @Query("page") int page, @Query("format") String format, @Query("nojsoncallback") int noJsonCallBack);
}
