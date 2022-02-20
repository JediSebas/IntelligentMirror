package com.jedisebas.inteligentmirror.ui.gallery;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jedisebas.inteligentmirror.R;

public class ImageItem {

    private Bitmap image;

    public ImageItem() {
        image = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.ic_launcher_background);
    }

    public ImageItem(Bitmap image) {
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
