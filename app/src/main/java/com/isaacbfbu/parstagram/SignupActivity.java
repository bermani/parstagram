package com.isaacbfbu.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.isaacbfbu.parstagram.databinding.ActivitySignupBinding;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {

    public static final String TAG = "SignupActivity";

    ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }

        binding = com.isaacbfbu.parstagram.databinding.ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void onClickSignupButton(View v) {
        Log.i(TAG, "onClick sign up button");
        String username = binding.etUsername.getText().toString();
        String password = binding.etPassword.getText().toString();
        String confirmPassword = binding.etConfirmPassword.getText().toString();

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);

        Log.i(TAG, "Attempting to sign up user " + username);
        binding.pbLoading.setVisibility(View.VISIBLE);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                binding.pbLoading.setVisibility(View.INVISIBLE);
                if (e != null) {
                    Log.e(TAG, "Issue with sign up", e);
                    Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                // navigate to the main activity of the user has signed in properly
                goMainActivity();
                Toast.makeText(SignupActivity.this, "Success!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}