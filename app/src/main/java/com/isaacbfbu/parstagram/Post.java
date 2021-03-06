package com.isaacbfbu.parstagram;

import android.text.format.DateUtils;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


@ParseClassName("Post")
public class Post extends ParseObject {

    public Post() {
    }

    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_CREATED_AT = "createdAt";
    public static final String KEY_LIKES = "likes";
    public static final String KEY_USERS_LIKED = "usersLiked";

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public String getDateString() {
        return getRelativeTimeAgo(getCreatedAt().toString());
    }

    public int getLikes() {
        return getNumber(KEY_LIKES).intValue();
    }

    public void setLikes(int likes) {
        put(KEY_LIKES, likes);
    }

    public void addUserLiked() {
        ParseRelation<ParseUser> relation = getRelation(KEY_USERS_LIKED);
        relation.add(ParseUser.getCurrentUser());
    }

    public void removeUserLiked() {
        ParseRelation<ParseUser> relation = getRelation(KEY_USERS_LIKED);
        relation.remove(ParseUser.getCurrentUser());
    }

    public ParseRelation<ParseUser> getUsersLiked() {
        return getRelation(KEY_USERS_LIKED);
    }


    // get the relative time ago in a nicely formatted string given a timestamp
    private static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
