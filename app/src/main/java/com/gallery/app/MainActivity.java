package com.gallery.app;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.gallery.app.adapters.GridViewAdapter;
import com.gallery.app.constants.AppConstants;
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
    @ViewById(R.id.grid_view)
    GridView gridView;

    AnimatorSet setLeftIn;
    AnimatorSet setLeftOut;
    AnimatorSet setRightIn;
    AnimatorSet setRightOut;

    List<Photo> photos = new ArrayList<>();
    GridViewAdapter gridViewAdapter;

    EventBus eventBus = EventBus.getDefault();

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        eventBus.register(this);
    }

    @Override
    public void onDestroy(){
        eventBus.unregister(this);
        super.onDestroy();
    }

    @Click(R.id.search)
    public void search() {
        Intent intent = new Intent(this, FlickrApiIntentService.class);
        intent.putExtra(AppConstants.PAGE_NUM, 1);
        intent.putExtra(AppConstants.SEARCH_TEXT, "birds");
        startService(intent);
    }

    @AfterViews
    public void init() {

        setLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(this,
                R.animator.card_flip_left_in);
        setLeftOut = (AnimatorSet) AnimatorInflater.loadAnimator(this,
                R.animator.card_flip_left_out);
        setRightIn = (AnimatorSet) AnimatorInflater.loadAnimator(this,
                R.animator.card_flip_right_in);
        setRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(this,
                R.animator.card_flip_right_out);

        gridViewAdapter = new GridViewAdapter(this, photos);
        gridView.setAdapter(gridViewAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Photo photo = photos.get(position);

                View label1 = view.findViewById(R.id.image);
                View label2 = view.findViewById(R.id.description_layout);
                boolean isPhotoVisible = photo.isPhotoVisible();
                if (isPhotoVisible) {
                    setRightIn.setTarget(label2);
                    setRightOut.setTarget(label1);
                    setRightIn.start();
                    setRightOut.start();
                } else {
                    setLeftIn.setTarget(label1);
                    setLeftOut.setTarget(label2);
                    setLeftIn.start();
                    setLeftOut.start();
                }
                photo.setPhotoVisible(!isPhotoVisible);
            }
        });
    }

    public void onEventMainThread(SearchPhotosResponseEvent searchPhotosResponseEvent) {
        if (searchPhotosResponseEvent.status) {
            SearchPhotosResponse searchPhotosResponse = searchPhotosResponseEvent.searchPhotosResponse;
            photos.addAll(searchPhotosResponse.getPhotos().getPhoto());
            gridViewAdapter.notifyDataSetChanged();
        }else{
            Toast.makeText(this, "Something went wrong",Toast.LENGTH_SHORT).show();
        }
    }
}
