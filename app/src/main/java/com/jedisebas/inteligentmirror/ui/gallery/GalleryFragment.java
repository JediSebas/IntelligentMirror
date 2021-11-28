package com.jedisebas.inteligentmirror.ui.gallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jedisebas.inteligentmirror.MainActivity;
import com.jedisebas.inteligentmirror.R;
import com.jedisebas.inteligentmirror.SignupActivity;

import java.io.InputStream;
import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;

    ImageView imageView1, imageView2, imageView3, imageView4, imageView5, imageView6, imageView7,
    imageView8, imageView9, imageView10, imageView11, imageView12, imageView13, imageView14,
    imageView15, imageView16;
    ArrayList<ImageView> al = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        imageView1 = root.findViewById(R.id.imageView1);
        imageView2 = root.findViewById(R.id.imageView2);
        imageView3 = root.findViewById(R.id.imageView3);
        imageView4 = root.findViewById(R.id.imageView4);
        imageView5 = root.findViewById(R.id.imageView5);
        imageView6 = root.findViewById(R.id.imageView6);
        imageView7 = root.findViewById(R.id.imageView7);
        imageView8 = root.findViewById(R.id.imageView8);
        imageView9 = root.findViewById(R.id.imageView9);
        imageView10 = root.findViewById(R.id.imageView10);
        imageView11 = root.findViewById(R.id.imageView11);
        imageView12 = root.findViewById(R.id.imageView12);
        imageView13 = root.findViewById(R.id.imageView13);
        imageView14 = root.findViewById(R.id.imageView14);
        imageView15 = root.findViewById(R.id.imageView15);
        imageView16 = root.findViewById(R.id.imageView16);

        al.add(imageView1);
        al.add(imageView2);
        al.add(imageView3);
        al.add(imageView4);
        al.add(imageView5);
        al.add(imageView6);
        al.add(imageView7);
        al.add(imageView8);
        al.add(imageView9);
        al.add(imageView10);
        al.add(imageView11);
        al.add(imageView12);
        al.add(imageView13);
        al.add(imageView14);
        al.add(imageView15);
        al.add(imageView16);

        for (int i=0; i<al.size(); i++){
            new DownloadImageTask(al.get(i)).execute("https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fi.pinimg.com%2Foriginals%2F95%2Fb2%2F72%2F95b272d0626b32ff03d92846047c4994.jpg&f=1&nofb=1");
        }

        return root;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}