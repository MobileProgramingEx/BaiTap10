package com.baitap.videoshort_firebase.adapter;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.RecyclerView;

import com.baitap.videoshort_firebase.LoginActivity;
import com.baitap.videoshort_firebase.R;
import com.baitap.videoshort_firebase.models.Video;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class VideoFireBaseAdapter extends FirebaseRecyclerAdapter<Video, VideoFireBaseAdapter.MyHolder> {

    private boolean isFav = false;

    public VideoFireBaseAdapter(@NonNull FirebaseRecyclerOptions<Video> options) {
        super(options);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_video_row, parent, false);
        return new MyHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyHolder holder, int position, @NonNull Video model) {
        holder.textVideoTitle.setText(model.getTitle());
        holder.textVideoDescription.setText(model.getDesc());

        // Hiện ProgressBar
        holder.videoProgressBar.setVisibility(View.VISIBLE);

        // Giải phóng player cũ (nếu có)
        if (holder.player != null) {
            holder.player.release();
        }

        // Khởi tạo ExoPlayer
        ExoPlayer player = new ExoPlayer.Builder(holder.itemView.getContext()).build();
        holder.playerView.setPlayer(player);

        // Tạo MediaItem từ URL
        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(model.getUrl()));
        player.setMediaItem(mediaItem);
        player.setRepeatMode(Player.REPEAT_MODE_ONE); // Lặp lại video

        // Chuẩn bị và play
        player.prepare();
        player.play();

        // Ẩn ProgressBar khi đã chuẩn bị xong
        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                if (state == Player.STATE_READY) {
                    holder.videoProgressBar.setVisibility(View.GONE);
                }
            }
        });

        holder.player = player; // giữ lại player để sau này release

        holder.imPerson.setOnClickListener(v -> {
            Log.d("VideoAdapter", "imPerson clicked!");
            Intent intent = new Intent(holder.itemView.getContext(), LoginActivity.class);
            holder.itemView.getContext().startActivity(intent);
        });

        // Xử lý nút Favorite (tim)
        holder.favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFav) {
                    holder.favorites.setImageResource(R.drawable.baseline_favorite_24);
                    isFav = true;
                } else {
                    holder.favorites.setImageResource(R.drawable.baseline_favorite_border_24);
                    isFav = false;
                }
            }
        });
    }


    // ViewHolder chứa các View cho mỗi item
    public static class MyHolder extends RecyclerView.ViewHolder {
        PlayerView playerView;
        ExoPlayer player;
        ProgressBar videoProgressBar;
        TextView textVideoTitle, textVideoDescription;
        ImageView imPerson, favorites, imShare, imMore;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            playerView = itemView.findViewById(R.id.playerView); // phải đúng ID trong XML
            videoProgressBar = itemView.findViewById(R.id.videoProgressBar);
            textVideoTitle = itemView.findViewById(R.id.textVideoTitle);
            textVideoDescription = itemView.findViewById(R.id.textVideoDescription);
            imPerson = itemView.findViewById(R.id.imPerson);
            favorites = itemView.findViewById(R.id.favorites);
            imShare = itemView.findViewById(R.id.imShare);
            imMore = itemView.findViewById(R.id.imMore);
        }
    }
}
