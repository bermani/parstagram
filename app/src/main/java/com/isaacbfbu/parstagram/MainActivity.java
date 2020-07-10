package com.isaacbfbu.parstagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.isaacbfbu.parstagram.databinding.ActivityMainBinding;
import com.isaacbfbu.parstagram.databinding.ItemPostBinding;
import com.isaacbfbu.parstagram.fragments.ComposeFragment;
import com.isaacbfbu.parstagram.fragments.DetailFragment;
import com.isaacbfbu.parstagram.fragments.HomeFragment;
import com.isaacbfbu.parstagram.fragments.ProfileFragment;
import com.isaacbfbu.parstagram.fragments.SettingsFragment;
import com.parse.ParseUser;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FragmentManager fragmentManager;

    HomeFragment homeFragment;
    ComposeFragment composeFragment;
    ProfileFragment profileFragment;
    SettingsFragment settingsFragment;

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
        settingsFragment = new SettingsFragment();


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

    public void goToDetail(Post post) {
        DetailFragment fragment = DetailFragment.newInstance(post);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        transaction.replace(R.id.flContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void goToSettings() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        transaction.replace(R.id.flContainer, settingsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void goToProfile(Post post) {
        profileFragment.pushPost(post);
        binding.bottomNavigation.setSelectedItemId(R.id.action_profile);
    }

    public void goToUserProfile(ParseUser user) {
        Fragment fragment = ProfileFragment.newInstance(user);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        transaction.replace(R.id.flContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void goBack() {
        fragmentManager.popBackStackImmediate();
    }

    public void logout() {
        ParseUser.logOut();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
        Toast.makeText(this, "You have logged out successfully!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // This is the up button
            case android.R.id.home:
                goBack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
