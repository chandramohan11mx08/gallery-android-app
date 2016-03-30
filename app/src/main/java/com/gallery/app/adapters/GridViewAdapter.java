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
import com.gallery.app.helpers.AppUtils;
import com.gallery.app.models.Photo;
import com.squareup.picasso.Picasso;

import java.util.List;


public class GridViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<Photo> dataList;
    LayoutInflater inflater;

    public GridViewAdapter(Context c, List<Photo> photos) {
        mContext = c;
        dataList = photos;
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
        ImageView vImageView = (ImageView) view.findViewById(R.id.image);
        TextView vPhotoName = (TextView) view.findViewById(R.id.photo_name);

        Photo photo = dataList.get(i);
        String imageUrl = AppUtils.getImageUrl(photo);
        Picasso.with(mContext)
                .load(imageUrl)
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .into(vImageView);
        vImageView.setTag(MainActivity.FRONT_SIDE);
        vPhotoName.setText(photo.getTitle());
        return view;
    }
}
