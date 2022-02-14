package com.jedisebas.inteligentmirror.ui.gallery;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jedisebas.inteligentmirror.Loggeduser;
import com.jedisebas.inteligentmirror.MainActivity;
import com.jedisebas.inteligentmirror.R;
import com.jedisebas.inteligentmirror.SignupActivity;

//import org.apache.hc.client5.http.classic.HttpClient;
//import org.apache.hc.client5.http.classic.methods.HttpPost;
//import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
//import org.apache.hc.client5.http.impl.classic.HttpClients;
//import org.apache.hc.core5.http.HttpEntity;
//import org.apache.hc.core5.http.HttpResponse;
//import org.apache.hc.core5.http.NameValuePair;
//import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that let preview pictures and download that.
 */

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;

    private GridView gridView;
    private GridViewAdapter gridAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        gridView = (GridView) root.findViewById(R.id.gridview);
        gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, getData());
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener((parent, v, position, id) -> {
            ImageItem item = (ImageItem) parent.getItemAtPosition(position);
            //Create intent
            Intent intent = new Intent(String.valueOf(DetailsActivity.class));
            intent.putExtra("title", item.getTitle());
            intent.putExtra("image", item.getImage());

            //Start details activity
            startActivity(intent);
            });

        return root;
    }

    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        for (int i = 0; i < imgs.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
            imageItems.add(new ImageItem(bitmap, "Image#" + i));
        }
        return imageItems;
    }

}