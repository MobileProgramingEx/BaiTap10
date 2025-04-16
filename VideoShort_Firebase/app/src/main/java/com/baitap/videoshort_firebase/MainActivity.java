package com.baitap.videoshort_firebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.baitap.videoshort_firebase.adapter.VideoFireBaseAdapter;
import com.baitap.videoshort_firebase.models.Video;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private VideoFireBaseAdapter videosAdapter;
    private static final String FIREBASE_DB_URL = "https://videoshortfirebase-815bb-default-rtdb.asia-southeast1.firebasedatabase.app";
    private ImageView imPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        viewPager2 = findViewById(R.id.vpager);

        // Gọi hàm lưu video demo vào Firebase (chỉ gọi một lần để test)
//        saveVideoUrlToFirebase();

//        saveMultipleVideosToFirebase();

        // Lấy video từ Firebase Database và hiển thị trên ViewPager2
        getVideos();
    }

    private void saveVideoUrlToFirebase() {
        String videoUrl = "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4";
        DatabaseReference databaseRef = FirebaseDatabase.getInstance(FIREBASE_DB_URL).getReference("videos");
        String newVideoKey = databaseRef.push().getKey();

        // Log videoKey để kiểm tra xem có được tạo ra hay không
        Log.d("Firebase", "Video key: " + newVideoKey);

        Map<String, Object> videoData = new HashMap<>();
        videoData.put("title", "Video Test OK");
        videoData.put("desc", "Video từ URL hợp lệ");
        videoData.put("url", videoUrl);

        // Log dữ liệu trước khi lưu vào Firebase
        Log.d("Firebase", "Video data: " + videoData);

        databaseRef.child(newVideoKey).setValue(videoData)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "Video saved successfully!");  // Log khi thành công
                    Toast.makeText(MainActivity.this, "Đã lưu video thành công!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Error: " + e.getMessage());  // Log khi thất bại
                    Toast.makeText(MainActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveMultipleVideosToFirebase() {
        List<Map<String, String>> videos = new ArrayList<>();

        videos.add(createVideo("Video 1", "Miêu tả video 1", "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"));
        videos.add(createVideo("Video 2", "Miêu tả video 2", "https://samplelib.com/lib/preview/mp4/sample-5s.mp4"));
        videos.add(createVideo("Video 3", "Miêu tả video 3", "https://samplelib.com/lib/preview/mp4/sample-10s.mp4"));
        videos.add(createVideo("Video 4", "Miêu tả video 4", "https://sample-videos.com/video123/mp4/720/big_buck_bunny_720p_1mb.mp4"));
        // Thêm nhiều video nếu muốn...

        DatabaseReference databaseRef = FirebaseDatabase.getInstance(FIREBASE_DB_URL).getReference("videos");

        for (Map<String, String> video : videos) {
            String newVideoKey = databaseRef.push().getKey();
            databaseRef.child(newVideoKey).setValue(video)
                    .addOnSuccessListener(aVoid -> Log.d("Firebase", "Saved: " + video.get("title")))
                    .addOnFailureListener(e -> Log.e("Firebase", "Error: " + e.getMessage()));
        }
    }

    private Map<String, String> createVideo(String title, String desc, String url) {
        Map<String, String> video = new HashMap<>();
        video.put("title", title);
        video.put("desc", desc);
        video.put("url", url);
        return video;
    }


    private void getVideos() {
        Log.d("Firebase", "Đang lấy dữ liệu từ Firebase...");
        // Kết nối đến Realtime Database node "videos"
        DatabaseReference mDataBase = FirebaseDatabase.getInstance(FIREBASE_DB_URL).getReference("videos");

        // Tạo tùy chọn cho FirebaseRecyclerAdapter
        FirebaseRecyclerOptions<Video> options =
                new FirebaseRecyclerOptions.Builder<Video>()
                        .setQuery(mDataBase, Video.class)
                        .build();

        // Gắn adapter cho ViewPager2
        videosAdapter = new VideoFireBaseAdapter(options);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL); // Vuốt dọc
        viewPager2.setAdapter(videosAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (videosAdapter != null) {
            videosAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (videosAdapter != null) {
            videosAdapter.stopListening();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (videosAdapter != null) {
            videosAdapter.notifyDataSetChanged();
        }
    }
}