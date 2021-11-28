package com.jedisebas.inteligentmirror.ui.progressbar;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jedisebas.inteligentmirror.R;

public class ProgressbarFragment extends Fragment {

    private ProgressbarViewModel progressbarViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        progressbarViewModel =
                new ViewModelProvider(this).get(ProgressbarViewModel.class);

        View root = inflater.inflate(R.layout.fragment_progressbar, container, false);

        return root;
    }
}