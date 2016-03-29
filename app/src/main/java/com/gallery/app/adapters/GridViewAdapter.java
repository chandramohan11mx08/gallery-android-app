package com.gallery.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gallery.app.MainActivity;
import com.gallery.app.R;

import java.util.List;


public class GridViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> dataList;
    LayoutInflater inflater;

    public GridViewAdapter(Context c, List<String> sentDays) {
        mContext = c;
        dataList = sentDays;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = inflater.inflate(R.layout.grid_view_item, null);
        int height = mContext.getResources().getDimensionPixelSize(R.dimen.grid_view_height);
        view.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, height));
        ImageView textView = (ImageView) view.findViewById(R.id.label);
        textView.setTag(MainActivity.FRONT_SIDE);
        return view;
    }
}
