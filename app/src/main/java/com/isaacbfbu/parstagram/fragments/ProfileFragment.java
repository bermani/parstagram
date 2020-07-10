package com.isaacbfbu.parstagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.isaacbfbu.parstagram.Post;
import com.isaacbfbu.parstagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

public class ProfileFragment extends HomeFragment {

    private static final String PARSE_USER = "parseUser";

    private ParseUser parseUser;

    public static ProfileFragment newInstance(ParseUser parseUser) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(PARSE_USER, Parcels.wrap(parseUser));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected ParseQuery<Post> buildQuery(int skip) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, parseUser);
        query.setLimit(5);
        query.setSkip(skip);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        return query;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            parseUser = Parcels.unwrap(getArguments().getParcelable(PARSE_USER));
        } else {
            parseUser = ParseUser.getCurrentUser();
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_profile, menu);
        menu.findItem(R.id.miSettings).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                activity.goToSettings();
                return false;
            }
        });
    }

    public void pushPost(Post post) {
        if (posts != null && adapter != null) {
            posts.add(0, post);
            adapter.notifyItemInserted(0);
        }
    }
}
