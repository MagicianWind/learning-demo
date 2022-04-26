package com.wind.demo.minio.util;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * minio 操作
 *
 * @author wind
 */
@Component
@RequiredArgsConstructor
public class MinioTemplate {

    private final MinioClient minioClient;

    // **********************************文件操作API**********************************
    /**
     * 上传文件(文件流)
     */
    public String putObject(String objectName, InputStream in, Long ObjectSize) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        objectName = UUID.randomUUID().toString().replaceAll("-", "") + objectName.substring(objectName.lastIndexOf("."));
        String bucketName = new SimpleDateFormat("yyyyMMdd").format(new Date());
        createBucket(bucketName);
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .stream(in, ObjectSize, -1)
                .build());
        return getObjectUrl(bucketName, objectName);
    }

    /**
     * 上传文件(文件地址)
     */
    public String uploadObject(String objectName, String objectUrl) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        objectName = UUID.randomUUID().toString().replaceAll("-", "") + objectName.substring(objectName.lastIndexOf("."));
        String bucketName = new SimpleDateFormat("yyyyMMdd").format(new Date());
        createBucket(bucketName);
        minioClient.uploadObject(UploadObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .filename(objectUrl)
                .build());
        return getObjectUrl(bucketName, objectName);
    }

    /**
     * 删除文件
     */
    public void deleteObject(String bucketName, String objectName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build());
    }

    /**
     * 获取文件信息
     */
    public StatObjectResponse getObjectInfo(String bucketName, String objectName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return minioClient.statObject(StatObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build());
    }

    /**
     * 查询文件是否存在
     */
    public boolean existObject(String bucketName, String objectName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        StatObjectResponse objectInfo = getObjectInfo(bucketName, objectName);
        if (objectInfo == null) {
            return false;
        }
        return true;
    }

    /**
     * 获取文件流
     */
    public InputStream getInputStream(String bucketName, String objectName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build());
    }

    /**
     * 下载文件
     */
    public void download(String bucketName, String objectName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        minioClient.downloadObject(DownloadObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build());
    }

    /**
     * 获取文件访问地址
     */
    public String getObjectUrl(String bucketName, String objectName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
       return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucketName)
                .object(objectName)
                .build());
    }

    // **********************************桶操作API**********************************
    /**
     * 不存在则创建桶
     */
    public void createBucket(String bucketName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        if (! existBucket(bucketName)) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        }
    }

    /**
     * 删除桶
     */
    public void deleteBucket(String bucketName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        minioClient.removeBucket(RemoveBucketArgs.builder()
                .bucket(bucketName)
                .build());
    }

    /**
     * 查询桶是否存在
     */
    public boolean existBucket(String bucketName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(bucketName)
                .build());
    }

    /**
     * 查询桶列表
     */
    public List<Bucket> listBucket() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return minioClient.listBuckets();
    }
}
