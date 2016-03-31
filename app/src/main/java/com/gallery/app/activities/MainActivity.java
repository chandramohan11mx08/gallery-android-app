package com.gallery.app.activities;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.gallery.app.R;
import com.gallery.app.adapters.GridViewAdapter;
import com.gallery.app.constants.AppConstants;
import com.gallery.app.helpers.AppUtils;
import com.gallery.app.models.Photo;
import com.gallery.app.models.SearchPhotosResponse;
import com.gallery.app.models.response.SearchPhotosResponseEvent;
import com.gallery.app.services.FlickrApiIntentService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @ViewById(R.id.search_edit)
    EditText vEditText;

    @ViewById(R.id.grid_view)
    GridView gridView;

    List<Photo> photos = new ArrayList<>();
    GridViewAdapter gridViewAdapter;
    int pageNumber = 0;
    EventBus eventBus = EventBus.getDefault();
    Context context;
    String searchText;

    int prevIndex = 0;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
        super.onDestroy();
    }

    @Click(R.id.search)
    public void search() {
        vEditText.clearFocus();
        AppUtils.hideKeyboard(this);
        resetSearchContext();
        searchText = vEditText.getText().toString();

        if (AppUtils.isNetworkAvailable()) {
            callPhotosSearchApi();
        } else {
            Toast.makeText(this, "Network not available", Toast.LENGTH_SHORT).show();
        }
    }

    public void resetSearchContext() {
        searchText = "";
        pageNumber = 1;
        prevIndex = 0;
        photos.clear();
        gridViewAdapter.notifyDataSetChanged();
    }

    public void callPhotosSearchApi() {
        Intent intent = new Intent(this, FlickrApiIntentService.class);
        intent.putExtra(AppConstants.PAGE_NUM, pageNumber);
        intent.putExtra(AppConstants.SEARCH_TEXT, searchText);
        startService(intent);
    }

    @AfterViews
    public void init() {
        context = this;
        gridViewAdapter = new GridViewAdapter(this, photos);
        gridView.setAdapter(gridViewAdapter);

        setClickListenerForGridItem();
        setScrollListenerForGridView();
    }

    private void setScrollListenerForGridView() {
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int i = firstVisibleItem + visibleItemCount;
                if (prevIndex == i) {
                    return;
                }

                boolean isEndReached = (i == (totalItemCount - 20));
                if (isEndReached && pageNumber != 0) {
                    pageNumber++;
                    callPhotosSearchApi();
                    prevIndex = i;
                }
            }
        });
    }

    private void setClickListenerForGridItem() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Photo photo = photos.get(position);

                View vImageView = view.findViewById(R.id.image);
                View vDescriptionLayout = view.findViewById(R.id.description_layout);
                boolean isPhotoVisible = photo.isPhotoVisible();
                if (isPhotoVisible) {
                    showPhotoDescription(vImageView, vDescriptionLayout);
                } else {
                    hidePhotoDescription(vImageView, vDescriptionLayout);
                }
                photo.setPhotoVisible(!isPhotoVisible);
            }
        });
    }

    private void hidePhotoDescription(View view1, View view2) {
        AnimatorSet setLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(context,
                R.animator.card_flip_left_in);
        AnimatorSet setLeftOut = (AnimatorSet) AnimatorInflater.loadAnimator(context,
                R.animator.card_flip_left_out);
        setLeftIn.setTarget(view1);
        setLeftOut.setTarget(view2);
        setLeftIn.start();
        setLeftOut.start();
    }

    private void showPhotoDescription(View view1, View view2) {
        AnimatorSet setRightIn = (AnimatorSet) AnimatorInflater.loadAnimator(context,
                R.animator.card_flip_right_in);
        AnimatorSet setRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(context,
                R.animator.card_flip_right_out);
        setRightIn.setTarget(view2);
        setRightOut.setTarget(view1);
        setRightIn.start();
        setRightOut.start();
    }

    public void onEventMainThread(SearchPhotosResponseEvent searchPhotosResponseEvent) {
        if (searchPhotosResponseEvent.status) {
            SearchPhotosResponse searchPhotosResponse = searchPhotosResponseEvent.searchPhotosResponse;
            photos.addAll(searchPhotosResponse.getPhotos().getPhoto());
            gridViewAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }
}
