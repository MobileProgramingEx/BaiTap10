package com.baitap.videoshort_firebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private FirebaseAuth mAuth;
    TextView textViewSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);
        textViewSignup = findViewById(R.id.textViewSignup);

        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            Log.d("LoginActivity", "onComplete được gọi");
                            if (task.isSuccessful()) {
                                Log.d("LoginActivity", "Đăng nhập thành công");
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Log.d("LoginActivity", "Đăng nhập thất bại: " + task.getException());
                                Toast.makeText(LoginActivity.this, "Đăng nhập thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        })
                        .addOnFailureListener(e -> {
                            Log.e("LoginActivity", "onFailure: ", e);
                            Toast.makeText(LoginActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        });
            } catch (Exception e) {
                Log.e("LoginActivity", "Lỗi xảy ra khi gọi signIn", e);
            }
        });

        textViewSignup.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }
}
