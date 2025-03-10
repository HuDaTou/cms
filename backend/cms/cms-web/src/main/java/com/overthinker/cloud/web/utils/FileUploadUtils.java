package com.overthinker.cloud.web.utils;

import cn.hutool.core.io.file.FileNameUtil;
import com.overthinker.cloud.web.entity.constants.Const;
import com.overthinker.cloud.web.entity.enums.UploadEnum;
import com.overthinker.cloud.web.exception.FileUploadException;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @author overH
 * <p>
 * 创建时间：2023/12/26 19:16
 * 文件上传工具类
 */
@Slf4j
@Component
public class FileUploadUtils {

    @Resource
    private MinioClient client;

    @Value("${minio.bucketName}")
    private String bucketName;

    @Value("${minio.endpoint}")
    private String endpoint;

    /**
     * 上传文件
     *
     * @param uploadEnum 文件枚举
     * @param file       文件
     * @return 上传后的文件地址
     * @throws Exception 异常
     */
    public String uploadImage(UploadEnum uploadEnum, MultipartFile file) throws Exception {
        // 验证文件大小
        if (verifyTheFileSize(file.getSize(), uploadEnum.getLimitSize()))
            throw new FileUploadException("上传文件超过限制大小:" + uploadEnum.getLimitSize() + "MB");

        // 验证文件格式
        if (isFormatFile(file.getOriginalFilename(), uploadEnum.getFormat())) {

            InputStream stream = file.getInputStream();
            String name = UUID.randomUUID().toString();
            String originalFilename = file.getOriginalFilename();
            String fileExtension = null;
            if (originalFilename != null) {
                fileExtension = FileNameUtil.extName(originalFilename);
            }
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .headers(Map.of(Const.CONTENT_TYPE, Objects.requireNonNull(file.getContentType())))
                    .object(uploadEnum.getDir() + name + "." + fileExtension)
                    .stream(stream, file.getSize(), -1)
                    .build();
            client.putObject(args);
            return endpoint + "/" + bucketName + "/" + uploadEnum.getDir() + name + "." + fileExtension;
        }
        log.error("--------------------上传文件格式不正确--------------------");
        throw new FileUploadException("上传文件类型错误");
    }



    /**
     * 验证文件大小
     *
     * @param fileSize  文件大小
     * @param limitSize 限制大小
     * @return 是否超过限制大小
     */
    public Boolean verifyTheFileSize(Long fileSize, Double limitSize) {
        // 转为相同大小格式
        double formatFileSize = convertFileSizeToMB(fileSize);
        if (formatFileSize < limitSize) {
            return false;
        }
        return true;
    }

    /**
     * B 转 MB
     *
     * @param sizeInBytes 文件大小 B
     * @return 文件大小 MB
     */
    public double convertFileSizeToMB(long sizeInBytes) {
        double sizeInMB = (double) sizeInBytes / (1024 * 1024);
        String formatted = String.format("%.2f", sizeInMB);
        // String转为double
        return Double.parseDouble(formatted);
    }

    /**
     * 获取目录下的所有文件名称
     *
     * @param dir 目录
     * @return 所有文件全路径名称
     */
    public List<String> listFiles(String dir) {
        // 测试
        dir = dir.endsWith("/") ? dir : dir + "/";
        ListObjectsArgs listObjectsArgs = ListObjectsArgs.builder()
                .bucket(bucketName)
                .prefix(dir)
                .build();
        Iterable<Result<Item>> results = client.listObjects(listObjectsArgs);

        List<String> fileNames = new ArrayList<>();
        results.forEach(result -> {
            Item item;
            try {
                // 提取出文件名
                item = result.get();
                fileNames.add(item.objectName());
            } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                     InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                     XmlParserException e) {
                log.error("获取文件出现错误", e);
            }
        });

        return fileNames;
    }

    /**
     * 批量删除
     *
     * @param fileNames 文件名称
     * @return 是否成功
     * @throws Exception 异常
     */
    public boolean deleteFiles(List<String> fileNames) throws Exception {
        List<DeleteObject> deleteObjects = fileNames.stream().map(DeleteObject::new).toList();
        RemoveObjectsArgs removeObjectsArgs = RemoveObjectsArgs.builder()
                .bucket(bucketName)
                .objects(deleteObjects)
                .build();
        Iterable<Result<DeleteError>> results = client.removeObjects(removeObjectsArgs);
        for (Result<DeleteError> result : results) {
            DeleteError error = result.get();
            log.error("文件: " + error.objectName() + "删除错误; ", error.message());
            return false;
        }
        return true;
    }

    /**
     * 单文件删除
     *
     * @param fileName 文件名称
     * @param dir      文件目录
     * @return 是否成功, 成功：true, 失败：false
     */
    public boolean deleteFile(String dir, String fileName) {
        try {
            String objectName = dir + fileName; // 构建完整对象名
            if (!isFileExist(dir, fileName)) {
                log.error("文件 {} 不存在", fileName);
                return false;
            }
            // 执行删除操作
            client.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );

            log.info("文件 {} 已成功从 MinIO 中删除", objectName);
            return true;
        } catch (Exception e) {
            log.error("删除 MinIO 文件 {} 失败: {}", fileName, e.getMessage());
            return false;
        }
    }

    /**
     * 文件格式校验
     *
     * @param fileName 文件名称
     * @param format   支持的后辍
     * @return 是否支持
     */
    public boolean isFormatFile(String fileName, Set<String> format) {
        for (String s : format) {
            if (fileName.endsWith(s)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 判断文件是否存在
     *
     * @param dir      目录
     * @param fileName 文件名
     * @return 是否存在，存在：true，不存在：false
     */
    public boolean isFileExist(String dir, String fileName) {
        dir = dir.endsWith("/") ? dir : dir + "/";
        ListObjectsArgs listObjectsArgs = ListObjectsArgs.builder()
                .bucket(bucketName)
                .prefix(dir)
                .build();
        Iterable<Result<Item>> results = client.listObjects(listObjectsArgs);

        for (Result<Item> result : results) {
            Item item = null;
            try {
                item = result.get();
            } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                     InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                     XmlParserException e) {
                log.error("判断文件是否存在出现错误", e);
            }
            if (item != null && item.objectName().equals(dir + fileName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 完整路径中截取文件名
     *
     * @param path 完整路径
     * @return 文件名
     */
    public String getFileName(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }

    public String isbucketExists(String bucketName) {
        try {
            boolean isExist = client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (isExist) {

                return "Bucket already exists.";
            } else {
                MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder().bucket(bucketName).build();
                client.makeBucket(makeBucketArgs);
                return "Bucket does not exist.";
            }
        } catch (Exception e) {
            return "Error occurred: " + e;
        }
    }
}
