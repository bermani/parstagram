package com.isaacbfbu.parstagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.isaacbfbu.parstagram.databinding.ActivityMainBinding;
import com.isaacbfbu.parstagram.databinding.ItemPostBinding;
import com.isaacbfbu.parstagram.fragments.ComposeFragment;
import com.isaacbfbu.parstagram.fragments.DetailFragment;
import com.isaacbfbu.parstagram.fragments.HomeFragment;
import com.isaacbfbu.parstagram.fragments.ProfileFragment;
import com.parse.ParseUser;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FragmentManager fragmentManager;

    MenuItem miActionProgressItem;

    HomeFragment homeFragment;
    ComposeFragment composeFragment;
    ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fragmentManager = getSupportFragmentManager();

        // define your fragments here
        homeFragment = new HomeFragment();
        composeFragment = new ComposeFragment();
        profileFragment = new ProfileFragment();

        // handle navigation selection
        binding.bottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment fragment;
                        switch (item.getItemId()) {
                            case R.id.action_home:
                                fragment = homeFragment;
                                break;
                            case R.id.action_compose:
                                fragment = composeFragment;
                                break;
                            case R.id.action_profile:
                            default:
                                fragment = profileFragment;
                                break;
                        }
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.flContainer, fragment);
                        transaction.commit();
                        return true;
                    }
                });
        // Set default selection
        binding.bottomNavigation.setSelectedItemId(R.id.action_home);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Store instance of the menu item containing progress
        miActionProgressItem = menu.findItem(R.id.miActionProgress);

        return super.onPrepareOptionsMenu(menu);
    }

    public void showProgressBar() {
        // Show progress item
        miActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        // Hide progress item
        miActionProgressItem.setVisible(false);
    }

    public void goToDetail(Post post, ItemPostBinding binding) {
        DetailFragment fragment = DetailFragment.newInstance(post);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        transaction.replace(R.id.flContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void goBack() {
        fragmentManager.popBackStackImmediate();
    }

    public void goToProfile(Post post) {
        profileFragment.pushPost(post);
        binding.bottomNavigation.setSelectedItemId(R.id.action_profile);
    }

    public void logout(View v) {
        ParseUser.logOut();
    }
}