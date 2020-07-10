package com.isaacbfbu.parstagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.isaacbfbu.parstagram.EndlessRecyclerViewScrollListener;
import com.isaacbfbu.parstagram.MainActivity;
import com.isaacbfbu.parstagram.Post;
import com.isaacbfbu.parstagram.PostsAdapter;
import com.isaacbfbu.parstagram.PostsGridAdapter;
import com.isaacbfbu.parstagram.R;
import com.isaacbfbu.parstagram.databinding.FragmentHomeBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private static final String PARSE_USER = "parseUser";

    private ParseUser parseUser;

    protected PostsGridAdapter gridAdapter;

    public static ProfileFragment newInstance(ParseUser parseUser) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(PARSE_USER, Parcels.wrap(parseUser));
        fragment.setArguments(args);
        return fragment;
    }

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

    public static final String TAG = "ProfileFragment";

    protected FragmentHomeBinding binding;
    protected PostsGridAdapter adapter;
    protected List<Post> posts;
    protected SwipeRefreshLayout swipeContainer;
    protected EndlessRecyclerViewScrollListener scrollListener;


    MainActivity activity;

    public ProfileFragment() {
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rvPosts = binding.rvPosts;

        activity = (MainActivity) getActivity();
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        if (posts == null) {
            posts = new ArrayList<>();
        }
        if (adapter == null) {
            adapter = new PostsGridAdapter(getContext(), posts);
        }
        rvPosts.setAdapter(adapter);
        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(), 3);
        rvPosts.setLayoutManager(manager);

        swipeContainer = binding.swipeContainer;
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                queryPosts(buildQuery(0));
            }
        });

        scrollListener = new EndlessRecyclerViewScrollListener(manager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                queryPosts(buildQuery(totalItemsCount));
            }
        };
        rvPosts.addOnScrollListener(scrollListener);

        if (posts.size() == 0) {
            queryPosts(buildQuery(0));
        }
        if (getArguments() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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