package com.gallery.app;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.gallery.app.adapters.GridViewAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

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

    @AfterViews
    public void init() {

        List<String> stringList = new ArrayList<>();
        stringList.add("1");
        stringList.add("2");
        stringList.add("3");
        stringList.add("4");
        stringList.add("5");

        setLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(this,
                R.animator.card_flip_left_in);
        setLeftOut = (AnimatorSet) AnimatorInflater.loadAnimator(this,
                R.animator.card_flip_left_out);
        setRightIn = (AnimatorSet) AnimatorInflater.loadAnimator(this,
                R.animator.card_flip_right_in);
        setRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(this,
                R.animator.card_flip_right_out);

        GridViewAdapter gridViewAdapter = new GridViewAdapter(this, stringList);
        gridView.setAdapter(gridViewAdapter);
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
}
