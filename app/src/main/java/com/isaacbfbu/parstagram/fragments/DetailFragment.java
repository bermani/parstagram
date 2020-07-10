package com.isaacbfbu.parstagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.isaacbfbu.parstagram.MainActivity;
import com.isaacbfbu.parstagram.Post;
import com.isaacbfbu.parstagram.R;
import com.isaacbfbu.parstagram.databinding.FragmentDetailBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class DetailFragment extends Fragment {

    private FragmentDetailBinding binding;

    MainActivity activity;

    private static final String POST = "post";

    private Post post;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param post post parameter.
     * @return A new instance of fragment DetailFragment.
     */
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
        activity = (MainActivity) getActivity();
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

        // Bind post data to view elements
        binding.post.tvDescription.setText(post.getDescription());
        binding.post.tvUsername.setText(post.getUser().getUsername());
        binding.post.tvDate.setText(post.getDateString());
        binding.post.tvLikes.setText(post.getLikes() + " likes");

        post.getUsersLiked().getQuery().findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                ArrayList<String> ids = new ArrayList<>();
                for (ParseUser object : objects) {
                    ids.add(object.getObjectId());
                }
                if (ids.contains(ParseUser.getCurrentUser().getObjectId())) {
                    binding.post.ivHeart.setImageResource(R.drawable.ufi_heart_active);
                } else {
                    binding.post.ivHeart.setImageResource(R.drawable.ufi_heart);
                }
            }
        });

        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(activity).load(image.getUrl()).into(binding.post.ivPost);
        }

        binding.post.ivHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post.getUsersLiked().getQuery().findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        ArrayList<String> ids = new ArrayList<>();
                        for (ParseUser object : objects) {
                            ids.add(object.getObjectId());
                        }
                        if (ids.contains(ParseUser.getCurrentUser().getObjectId())) {
                            post.setLikes(post.getLikes() - 1);
                            post.getUsersLiked().remove(ParseUser.getCurrentUser());
                            binding.post.ivHeart.setImageResource(R.drawable.ufi_heart);
                        } else {
                            post.setLikes(post.getLikes() + 1);
                            post.getUsersLiked().add(ParseUser.getCurrentUser());
                            binding.post.ivHeart.setImageResource(R.drawable.ufi_heart_active);
                        }
                        binding.post.tvLikes.setText(post.getLikes() + " likes");
                        post.saveInBackground();
                    }
                });
            }
        });

        binding.post.tvUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.goToUserProfile(post.getUser());
            }
        });
    }
}