package com.example.baitap09;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageView profileImage = findViewById(R.id.profileImage);
        Button btnLogout = findViewById(R.id.btnLogout);

        // Chuyển sang MainActivity khi nhấn vào ảnh đại diện
        profileImage.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // Thêm sự kiện đăng xuất (tuỳ ý)
        btnLogout.setOnClickListener(view -> {
            finish(); // Đóng Activity này
        });
    }
}