package com.isaacbfbu.parstagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.isaacbfbu.parstagram.Post;
import com.isaacbfbu.parstagram.PostsAdapter;
import com.isaacbfbu.parstagram.R;
import com.isaacbfbu.parstagram.databinding.FragmentComposeBinding;
import com.isaacbfbu.parstagram.databinding.FragmentHomeBinding;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    public static final String TAG = "HomeFragment";

    private FragmentHomeBinding binding;
    protected PostsAdapter adapter;
    protected List<Post> posts;
    private SwipeRefreshLayout swipeContainer;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rvPosts = binding.rvPosts;

        if (posts == null) {
            posts = new ArrayList<>();
        }
        if (adapter == null) {
            adapter = new PostsAdapter(getContext(), posts);
        }
        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeContainer = binding.swipeContainer;
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                queryPosts(buildQuery());
            }
        });
        if (posts.size() == 0) {
            queryPosts(buildQuery());
        }
    }

    protected ParseQuery<Post> buildQuery() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(20);
        query.setSkip(posts.size());
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        return query;
    }

    private void queryPosts(ParseQuery<Post> query) {
        if (swipeContainer != null) {
            swipeContainer.setRefreshing(true);
        }
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> newPosts, ParseException e) {
                if (swipeContainer != null) {
                    swipeContainer.setRefreshing(false);
                }
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for (Post post : newPosts) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", " + post.getUser().getUsername());
                }
                adapter.addAll(newPosts);
            }
        });
    }
}