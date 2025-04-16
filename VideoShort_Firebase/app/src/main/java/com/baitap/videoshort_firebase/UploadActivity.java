package com.baitap.videoshort_firebase;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.util.Map;

public class UploadActivity extends AppCompatActivity {
    Cloudinary cloudinary;
    private static final int PICK_VIDEO_REQUEST = 1;
    private Uri videoUri;

    private Button btnChooseVideo, btnUploadVideo;
    private ProgressBar uploadProgressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        mAuth = FirebaseAuth.getInstance();

        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dcxgx3ott",
                "api_key", "353197162924271",
                "api_secret", "czNR6EQoPSG0_P3P_Sp1bKMWVdM"
        ));

        btnChooseVideo = findViewById(R.id.btnChooseVideo);
        btnUploadVideo = findViewById(R.id.btnUploadVideo);
        uploadProgressBar = findViewById(R.id.uploadProgressBar);

        btnChooseVideo.setOnClickListener(v -> openFileChooser());

        btnUploadVideo.setOnClickListener(v -> {
            if (videoUri != null) {
                uploadVideoToFirebase();
            } else {
                Toast.makeText(this, "Vui lòng chọn video", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("IntentReset")
    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*"); // Đặt type TRƯỚC
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // startActivity sau khi intent đã sẵn sàng
        startActivityForResult(Intent.createChooser(intent, "Chọn video"), PICK_VIDEO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            videoUri = data.getData();
            Toast.makeText(this, "Đã chọn video", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadVideoToFirebase() {
        uploadProgressBar.setVisibility(View.VISIBLE);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
            uploadProgressBar.setVisibility(View.GONE);
            return;
        }

        String userId = user.getUid();
        String fileName = "video_" + System.currentTimeMillis();

        new Thread(() -> {
            try {
                File file = new File(getRealPathFromURI(videoUri));

                Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.asMap(
                        "resource_type", "video",
                        "public_id", "Android/" + userId + "/" + fileName,
                        "overwrite", true
                ));

                String videoUrl = uploadResult.get("secure_url").toString();

                runOnUiThread(() -> {
                    uploadProgressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Upload thành công!\n" + videoUrl, Toast.LENGTH_LONG).show();
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    uploadProgressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Lỗi upload: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Video.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        return contentUri.getPath();
    }
}
