package com.gallery.app.helpers;

import com.gallery.app.constants.UrlConstants;
import com.gallery.app.models.Photo;

public final class AppUtils {
    public static String getImageUrl(Photo photo) {
        String url = "https://farm" + photo.getFarm() + "." + UrlConstants.FLICKR_STATIC_URL + "/" + photo.getServer() + "/" + photo.getId() + "_" + photo.getSecret() + "_" + "q.jpg";
        return url;
    }
}
