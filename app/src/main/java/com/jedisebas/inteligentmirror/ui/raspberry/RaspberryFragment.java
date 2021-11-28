package com.jedisebas.inteligentmirror.ui.raspberry;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jedisebas.inteligentmirror.R;

public class RaspberryFragment extends Fragment {

    private RaspberryViewModel raspberryViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        raspberryViewModel =
                new ViewModelProvider(this).get(RaspberryViewModel.class);

        View root = inflater.inflate(R.layout.fragment_raspberry, container, false);

        return root;
    }
}