package com.baitap.webviewclient;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Gán layout
        setContentView(R.layout.activity_main);

        // Ánh xạ WebView
        webView = findViewById(R.id.webView);

        // Kích hoạt JavaScript và chế độ responsive
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // nếu trang cần
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDomStorageEnabled(true); // cần nếu trang có lưu trữ cục bộ

        // Đảm bảo mở liên kết trong WebView, không ra trình duyệt ngoài
        webView.setWebViewClient(new WebViewClient());

        // Tải trang web
        webView.loadUrl("http://10.0.2.2:5500/login.html"); // thay bằng trang web responsive của bạn
    }

    // Xử lý khi nhấn nút back
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack(); // quay lại trang trước trong webview
        } else {
            super.onBackPressed(); // thoát ứng dụng
        }
    }
}