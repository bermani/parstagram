package com.isaacbfbu.parstagram.fragments;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import com.isaacbfbu.parstagram.R;
import com.isaacbfbu.parstagram.databinding.FragmentComposeBinding;

public class ComposeFragment extends Fragment {

    FragmentComposeBinding binding;

    public ComposeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentComposeBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.ivUploaded.setMinimumHeight(Resources.getSystem().getDisplayMetrics().widthPixels);
        binding.etDescription.setImeOptions(EditorInfo.IME_ACTION_DONE);
        binding.etDescription.setRawInputType(InputType.TYPE_CLASS_TEXT);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}