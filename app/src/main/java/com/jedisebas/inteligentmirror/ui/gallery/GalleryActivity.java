package com.jedisebas.inteligentmirror.ui.gallery;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.jedisebas.inteligentmirror.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    GridView gridView;
    private static ImageLoader imageLoader;
    private static ArrayList<String> imageUrls;
    private static ArrayList<Bitmap> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        gridView = findViewById(R.id.gridLayout);

        imageList = new ArrayList<>();
        imageUrls = new ArrayList<>();
        imageUrls.add("https://www.wallpaperaccess.com/thumb/24528.png");
        imageUrls.add("https://www.wallpaperaccess.com/thumb/10800.jpg");
        imageUrls.add("https://www.wallpaperaccess.com/thumb/339922.jpg");
        imageUrls.add("https://www.wallpaperaccess.com/thumb/2061.png");
        imageUrls.add("https://www.wallpaperaccess.com/thumb/623883.jpg");
        imageUrls.add("https://www.wallpaperaccess.com/thumb/3095697.jpg");
        imageUrls.add("https://www.wallpaperaccess.com/thumb/3537227.jpg");
        imageUrls.add("https://www.wallpaperaccess.com/thumb/4174175.jpg");
        imageUrls.add("https://www.wallpaperaccess.com/thumb/6789524.jpg");
        imageUrls.add("https://www.wallpaperaccess.com/thumb/1920884.png");
        imageUrls.add("https://www.wallpaperaccess.com/thumb/1275034.jpg");

        ArrayList<ImageItem> imageItems = new ArrayList<>();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getBaseContext())
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs()
                .build();

        imageLoader =  ImageLoader.getInstance();
        imageLoader.init(config);

        GettingBitmap gettingBitmap = new GettingBitmap();
        gettingBitmap.t.start();

        try {
            gettingBitmap.t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (Bitmap bitmap : imageList) {
            imageItems.add(new ImageItem(bitmap));
        }

        GridViewAdapter adapter = new GridViewAdapter(this, imageItems);
        gridView.setAdapter(adapter);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Gallery");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private static class GettingBitmap implements Runnable {

        public Thread t;

        GettingBitmap() {
            t = new Thread(this);
        }

        @Override
        public void run() {
            for (String s: imageUrls) {
                imageList.add(imageLoader.loadImageSync(s));
            }
        }
    }
}