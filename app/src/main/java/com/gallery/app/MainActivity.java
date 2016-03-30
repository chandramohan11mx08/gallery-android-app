package com.gallery.app;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.gallery.app.adapters.GridViewAdapter;
import com.gallery.app.constants.AppConstants;
import com.gallery.app.models.SearchPhotosResponse;
import com.gallery.app.models.response.SearchPhotosResponseEvent;
import com.gallery.app.services.FlickrApiIntentService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import de.greenrobot.event.EventBus;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    @ViewById(R.id.grid_view)
    GridView gridView;

    AnimatorSet setLeftIn;
    AnimatorSet setLeftOut;
    AnimatorSet setRightIn;
    AnimatorSet setRightOut;

    public static final int FRONT_SIDE = 1;
    public static final int BACK_SIDE = 2;

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

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                View label1 = view.findViewById(R.id.label);
                View label2 = view.findViewById(R.id.description_layout);
                int mode = (int) label1.getTag();
                if (mode == FRONT_SIDE) {
                    setRightIn.setTarget(label2);
                    setRightOut.setTarget(label1);
                    setRightIn.start();
                    setRightOut.start();
                    label1.setTag(BACK_SIDE);
                } else {
                    setLeftIn.setTarget(label1);
                    setLeftOut.setTarget(label2);
                    setLeftIn.start();
                    setLeftOut.start();
                    label1.setTag(FRONT_SIDE);
                }
            }
        });
    }

    public void onEventMainThread(SearchPhotosResponseEvent searchPhotosResponseEvent) {
        if (searchPhotosResponseEvent.status) {
            SearchPhotosResponse searchPhotosResponse = searchPhotosResponseEvent.searchPhotosResponse;
            GridViewAdapter gridViewAdapter = new GridViewAdapter(this, searchPhotosResponse.getPhotos().getPhoto());
            gridView.setAdapter(gridViewAdapter);
        }else{

        }
    }
}
