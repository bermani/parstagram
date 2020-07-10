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

import java.util.List;

public class ProfileFragment extends HomeFragment {
    @Override
    protected ParseQuery<Post> buildQuery() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.setLimit(20);
        query.setSkip(posts.size());
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        return query;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
