package com.jedisebas.inteligentmirror.ui.raspberry;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jedisebas.inteligentmirror.R;

public class RaspberryFragment extends Fragment {

    private RaspberryViewModel mViewModel;

    public static RaspberryFragment newInstance() {
        return new RaspberryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_raspberry, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RaspberryViewModel.class);
        // TODO: Use the ViewModel
    }

}