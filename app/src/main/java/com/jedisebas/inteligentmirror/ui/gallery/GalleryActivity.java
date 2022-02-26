package com.jedisebas.inteligentmirror.ui.gallery;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.jedisebas.inteligentmirror.Loggeduser;
import com.jedisebas.inteligentmirror.MainActivity;
import com.jedisebas.inteligentmirror.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    GridView gridView;
    private ImageLoader imageLoader;
    private ArrayList<String> imageNames;
    private ArrayList<String> imageUrls;
    private ArrayList<Bitmap> imageList;
    private ArrayList<ImageItem> imageItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        gridView = findViewById(R.id.gridLayout);

        imageNames = new ArrayList<>();
        imageList = new ArrayList<>();
        imageUrls = new ArrayList<>();
//        imageUrls.add("https://www.wallpaperaccess.com/thumb/24528.png");
//        imageUrls.add("https://www.wallpaperaccess.com/thumb/10800.jpg");
//        imageUrls.add("https://www.wallpaperaccess.com/thumb/339922.jpg");
//        imageUrls.add("https://www.wallpaperaccess.com/thumb/2061.png");
//        imageUrls.add("https://www.wallpaperaccess.com/thumb/623883.jpg");
//        imageUrls.add("https://www.wallpaperaccess.com/thumb/3095697.jpg");
//        imageUrls.add("https://www.wallpaperaccess.com/thumb/3537227.jpg");
//        imageUrls.add("https://www.wallpaperaccess.com/thumb/4174175.jpg");
//        imageUrls.add("https://www.wallpaperaccess.com/thumb/6789524.jpg");
//        imageUrls.add("https://www.wallpaperaccess.com/thumb/1920884.png");
//        imageUrls.add("https://www.wallpaperaccess.com/thumb/1275034.jpg");
//        imageUrls.add("https://www.wallpaperaccess.com/full/2288412.jpg");
//        imageUrls.add("https://www.wallpaperaccess.com/full/248569.jpg");
//        imageUrls.add("https://www.wallpaperaccess.com/full/261233.jpg");
//        imageUrls.add("https://www.wallpaperaccess.com/full/6789526.jpg");

        JDBCGetPictures getPictures = new JDBCGetPictures();
        getPictures.t.start();

        try {
            getPictures.t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        imageItems = new ArrayList<>();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getBaseContext())
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs()
                .build();

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);

        for (int i=0; i<imageUrls.size(); i++) {
            imageItems.add(new ImageItem());
        }

        GridViewAdapter adapter = new GridViewAdapter(this, imageItems);
        gridView.setAdapter(adapter);

        Refresh refresh = new Refresh();
        refresh.execute();

        GettingBitmap gettingBitmap = new GettingBitmap();
        gettingBitmap.t.start();

        gridView.setOnItemClickListener((adapterView, view, i, l) -> {
            ImageItem item = (ImageItem) adapterView.getItemAtPosition(i);
            Intent intent = new Intent(GalleryActivity.this, DownloadActivity.class);
            DownloadActivity.bitmap = item.getImage();
            DownloadActivity.fileName = item.getImageName();
            startActivity(intent);
            gridView.invalidateViews();
        });

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


    private class GettingBitmap implements Runnable {

        public Thread t;

        GettingBitmap() {
            t = new Thread(this);
        }

        @Override
        public void run() {
            for (String s: imageUrls) {
                imageList.add(imageLoader.loadImageSync(s));
                for (int i=0; i<imageList.size(); i++) {
                    imageItems.get(i).setImage(imageList.get(i));
                    imageItems.get(i).setImageName(imageNames.get(i));
                }
            }
        }
    }

    private class JDBCGetPictures implements Runnable {

        Thread t;

        JDBCGetPictures() {
            t = new Thread(this);
        }

        @Override
        public void run() {
            String DB_URL = "jdbc:mysql://"+ Loggeduser.ip +"/mirror";
            String USER = "user";
            String PASS = "user"; // test password
            //TODO password hash
            String QUERY = "SELECT name FROM `pictures` WHERE userid=" + Loggeduser.id;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                System.out.println("DRIVER STILL WORKS BTW");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(QUERY);
                while (rs.next()) {
                    String n = rs.getString("name");
                    String n2 = "http://"+ Loggeduser.ip +"/mirror/" + n;
                    imageNames.add(n);
                    System.out.println(n2);
                    imageUrls.add(n2);
                }
            } catch (SQLException throwables) {
                System.out.println("HERE IS SOMETHING WRONG");
                throwables.printStackTrace();
            }
        }
    }

    private class Refresh extends AsyncTask<String, Void, View> {

        @Override
        protected View doInBackground(String... strings) {
            gridView.invalidateViews();
            return null;
        }
    }
}