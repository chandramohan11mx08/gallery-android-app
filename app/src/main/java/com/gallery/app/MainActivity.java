package com.gallery.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @AfterViews
    public void init() {

        List<String> stringList = new ArrayList<>();
        stringList.add("1");
        stringList.add("2");
        stringList.add("3");
        stringList.add("4");
        stringList.add("5");

        GridViewAdapter gridViewAdapter = new GridViewAdapter(this, stringList);
        gridView.setAdapter(gridViewAdapter);
    }
}
