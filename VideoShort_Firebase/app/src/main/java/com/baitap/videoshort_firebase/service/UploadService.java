package com.baitap.videoshort_firebase.service;

import java.io.File;

public interface UploadService {
    void uploadVideo(File file, String publicId, UploadCallback callback);
}
