package com.isaacbfbu.parstagram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.isaacbfbu.parstagram.databinding.ItemPostBinding;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent ,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(posts.get(position));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemPostBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemPostBinding.bind(itemView);
        }

        public void bind(final Post post) {
            // Bind post data to view elements
            binding.tvDescription.setText(post.getDescription());
            binding.tvUsername.setText(post.getUser().getUsername());
            binding.tvDate.setText(post.getDateString());
            binding.tvLikes.setText(post.getLikes() + " likes");

            post.getUsersLiked().getQuery().findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    ArrayList<String> ids = new ArrayList<>();
                    for (ParseUser object : objects) {
                        ids.add(object.getObjectId());
                    }
                    if (ids.contains(ParseUser.getCurrentUser().getObjectId())) {
                        binding.ivHeart.setImageResource(R.drawable.ufi_heart_active);
                    } else {
                        binding.ivHeart.setImageResource(R.drawable.ufi_heart);
                    }
                }
            });

            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(binding.ivPost);
            }
            binding.ivPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity) context).goToDetail(post, binding);
                }
            });

            binding.ivHeart.setOnClickListener(new View.OnClickListener() {
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
                                binding.ivHeart.setImageResource(R.drawable.ufi_heart);
                            } else {
                                post.setLikes(post.getLikes() + 1);
                                post.getUsersLiked().add(ParseUser.getCurrentUser());
                                binding.ivHeart.setImageResource(R.drawable.ufi_heart_active);
                            }
                            binding.tvLikes.setText(post.getLikes() + " likes");
                            post.saveInBackground();
                        }
                    });

                }
            });
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }
}
