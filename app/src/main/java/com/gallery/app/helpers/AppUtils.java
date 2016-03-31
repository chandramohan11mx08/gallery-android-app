package com.gallery.app.helpers;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.gallery.app.GalleryApplication;
import com.gallery.app.constants.UrlConstants;
import com.gallery.app.models.Photo;

public final class AppUtils {
    public static String getImageUrl(Photo photo) {
        String url = "https://farm" + photo.getFarm() + "." + UrlConstants.FLICKR_STATIC_URL + "/" + photo.getServer() + "/" + photo.getId() + "_" + photo.getSecret() + "_" + "q.jpg";
        return url;
    }

    public static void hideKeyboard(Activity activity) {
        if (activity == null) {
            return;
        }

        InputMethodManager inputManager =
                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        View focusedView = activity.getCurrentFocus();
        if (focusedView != null) {
            inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) GalleryApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return ((networkInfo != null) && networkInfo.isConnected());
    }
}