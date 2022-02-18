package com.jedisebas.inteligentmirror.ui.gallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jedisebas.inteligentmirror.R;

import java.util.ArrayList;

public class GridViewAdapter extends ArrayAdapter {

    public GridViewAdapter(@NonNull Context context, ArrayList<ImageItem> imageItems) {
        super(context, 0, imageItems);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item, parent, false);
        }

        ImageItem imageItem = (ImageItem) getItem(position);
        ImageView imageView = convertView.findViewById(R.id.gridImage);

        imageView.setImageBitmap(imageItem.getImage());

        return convertView;
    }

}
