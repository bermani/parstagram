package com.isaacbfbu.parstagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.isaacbfbu.parstagram.MainActivity;
import com.isaacbfbu.parstagram.Post;
import com.isaacbfbu.parstagram.R;
import com.isaacbfbu.parstagram.databinding.FragmentDetailBinding;
import com.parse.ParseFile;

import org.parceler.Parcels;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class DetailFragment extends Fragment {

    private FragmentDetailBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String POST = "post";

    // TODO: Rename and change types of parameters
    private Post post;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param post post parameter.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(Post post) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(POST, Parcels.wrap(post));
        fragment.setArguments(args);
        return fragment;
    }

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            post = Parcels.unwrap(getArguments().getParcelable(POST));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).goBack();
            }
        });

        // Bind post data to view elements
        binding.post.tvDescription.setText(post.getDescription());
        binding.post.tvUsername.setText(post.getUser().getUsername());
        binding.post.tvDate.setText(post.getDateString());


        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(getActivity()).load(image.getUrl()).into(binding.post.ivPost);
        }
    }
}