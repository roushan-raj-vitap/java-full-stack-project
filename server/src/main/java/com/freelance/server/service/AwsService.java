package com.freelance.server.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;



import org.springframework.beans.factory.annotation.Value;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

@Service
public class AwsService {

	@Autowired
	S3Client s3Client;
	
	@Value("${cloud.aws.s3.bucket}")
	String bucketName ;
	
	@Value("${cloud.aws.region.static}")
	private String region;
	
	public String uploadFile(MultipartFile file) throws IOException {
		if (file == null || file.isEmpty()) {
	        throw new IllegalArgumentException("File is null or empty");
	    }
	    String key = "gig-images/" + UUID.randomUUID() + "-" + file.getOriginalFilename();

	    PutObjectRequest putRequest = PutObjectRequest.builder()
	            .bucket(bucketName)
	            .key(key)
	            .contentType(file.getContentType())
	            .build();
	    System.out.println("bucket"+bucketName+"key"+key);

	    try {
	        s3Client.putObject(putRequest, RequestBody.fromBytes(file.getBytes()));
	    } catch (Exception e) {
	        throw new RuntimeException("Failed to upload file to S3: " + e.getMessage(), e);
	    }

	    return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + key;
	}
	
	public void deleteFileFromS3(String imageUrl) {
	    // Extract the key from the image URL
	    String key = imageUrl.substring(imageUrl.indexOf("gig-images/"));  // e.g., gig-images/uuid-filename.jpg

	    DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
	        .bucket(bucketName)
	        .key(key)
	        .build();

	    s3Client.deleteObject(deleteRequest);
	}
}
