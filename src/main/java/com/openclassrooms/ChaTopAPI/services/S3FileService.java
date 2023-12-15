package com.openclassrooms.ChaTopAPI.services;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.Data;

@Data
@Service
public class S3FileService {

    @Value("${s3.bucket.name}")
    private String s3BucketName;

    @Autowired
    private AmazonS3 amazonS3;

    /**
     * Upload images to the bucket
     * 
     * @param s3BucketName
     * @param objectName
     * @param objectToUpload
     * @return
     */
    public String uploadObject(String s3BucketName, String objectName, File objectToUpload) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(s3BucketName, objectName, objectToUpload);
        AccessControlList acl = new AccessControlList();
        acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
        putObjectRequest.setAccessControlList(acl);
        amazonS3.putObject(putObjectRequest);

        // amazonS3.putObject(s3BucketName, objectName, objectToUpload);
        return amazonS3.getUrl(s3BucketName, objectName).toString();
    }

    public String deleteObject(String s3BucketName, String objectName) {
        amazonS3.deleteObject(s3BucketName, objectName);
        return "Image deleted";
    }
}
