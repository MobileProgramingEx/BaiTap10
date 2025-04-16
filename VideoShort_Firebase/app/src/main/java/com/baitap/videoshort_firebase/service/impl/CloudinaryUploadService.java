package com.baitap.videoshort_firebase.service.impl;

import com.baitap.videoshort_firebase.service.UploadCallback;
import com.baitap.videoshort_firebase.service.UploadService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.File;
import java.util.Map;

public class CloudinaryUploadService implements UploadService {
    private Cloudinary cloudinary;

    public CloudinaryUploadService() {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dcxgx3ott",
                "api_key", "353197162924271",
                "api_secret", "czNR6EQoPSG0_P3P_Sp1bKMWVdM"
        ));
    }

    @Override
    public void uploadVideo(File file, String publicId, UploadCallback callback) {
        new Thread(() -> {
            try {
                Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.asMap(
                        "resource_type", "video",
                        "public_id", publicId,
                        "overwrite", true
                ));
                String videoUrl = uploadResult.get("secure_url").toString();
                callback.onUploadSuccess(videoUrl);
            } catch (Exception e) {
                e.printStackTrace();
                callback.onUploadFailure(e.getMessage());
            }
        }).start();
    }
}
