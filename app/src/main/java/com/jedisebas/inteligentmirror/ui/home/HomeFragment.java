package com.jedisebas.inteligentmirror.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jedisebas.inteligentmirror.JDBCLogin;
import com.jedisebas.inteligentmirror.R;

/**
 * Class which welcome the user
 */

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        TextView hello = root.findViewById(R.id.helloTv);
        String welcome = "Hello" + JDBCLogin.name + " " + JDBCLogin.lastname;
        hello.setText(welcome);

        return root;
    }
}