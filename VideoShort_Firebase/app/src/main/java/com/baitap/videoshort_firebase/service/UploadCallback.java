package com.baitap.videoshort_firebase.service;

public interface UploadCallback {
    void onUploadSuccess(String videoUrl);
    void onUploadFailure(String errorMessage);
}
