package backend.services;

import backend.DTO.FileResponse;
import backend.DTO.ImportDTO;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveBucketArgs;
import io.minio.RemoveObjectsArgs;
import io.minio.Result;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import backend.configuration.MinioConfig;
import backend.model.Import;
import backend.model.ImportStatus;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MinioService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MinioService.class);

    @Autowired
    private final MinioClient minioClient;
    private final MinioConfig minioProperties;


    @SneakyThrows
    public boolean bucketExists(String bucketName) {

        boolean found =
                minioClient.bucketExists(
                        BucketExistsArgs.builder().
                                bucket(bucketName).
                                build());

        return found;
    }

    public void uploadFile(String bucketName, String objectName, InputStream inputStream, String contentType) {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            minioClient.putObject(
                PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(
                        inputStream, inputStream.available(), -1)
                        .contentType(contentType)
                        .build());
        } catch (Exception e) {
            throw new RuntimeException("Error occurred: " + e.getMessage());
        }
    }

    @SneakyThrows
    public boolean makeBucket(String bucketName) {

        LOGGER.info("MinioUtil | makeBucket is called");

        boolean flag = bucketExists(bucketName);

        LOGGER.info("MinioUtil | makeBucket | flag : " + flag);

        if (!flag) {
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(bucketName)
                            .build());

            return true;
        } else {
            return false;
        }
    }

    @SneakyThrows
    public List<String> listBucketNames() {

        LOGGER.info("MinioUtil | listBucketNames is called");

        List<Bucket> bucketList = listBuckets();

        LOGGER.info("MinioUtil | listBucketNames | bucketList size : " + bucketList.size());

        List<String> bucketListName = new ArrayList<>();
        for (Bucket bucket : bucketList) {
            bucketListName.add(bucket.name());
        }

        LOGGER.info("MinioUtil | listBucketNames | bucketListName size : " + bucketListName.size());

        return bucketListName;
    }

    @SneakyThrows
    public List<Bucket> listBuckets() {
        LOGGER.info("MinioUtil | listBuckets is called");

        return minioClient.listBuckets();
    }

    @SneakyThrows
    public Iterable<Result<Item>> listObjects(String bucketName) {

        LOGGER.info("MinioUtil | listObjects is called");

        boolean flag = bucketExists(bucketName);

        LOGGER.info("MinioUtil | listObjects | flag : " + flag);

        if (flag) {
            return minioClient.listObjects(
                    ListObjectsArgs.builder().bucket(bucketName).build());
        }
        return null;
    }

    @SneakyThrows
    public boolean removeBucket(String bucketName) {

        LOGGER.info("MinioUtil | removeBucket is called");

        boolean flag = bucketExists(bucketName);
        LOGGER.info("MinioUtil | removeBucket | flag : " + flag);

        if (flag) {
            Iterable<Result<Item>> myObjects = listObjects(bucketName);

            for (Result<Item> result : myObjects) {
                Item item = result.get();
                //  Delete failed when There are object files in bucket

                LOGGER.info("MinioUtil | removeBucket | item size : " + item.size());

                if (item.size() > 0) {
                    return false;
                }
            }

            //  Delete bucket when bucket is empty
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
            flag = bucketExists(bucketName);

            LOGGER.info("MinioUtil | removeBucket | flag : " + flag);
            if (!flag) {
                return true;
            }
        }
        return false;
    }

    @SneakyThrows
    public List<String> listObjectNames(String bucketName) {

        LOGGER.info("MinioUtil | listObjectNames is called");

        List<String> listObjectNames = new ArrayList<>();
        boolean flag = bucketExists(bucketName);

        LOGGER.info("MinioUtil | listObjectNames | flag : " + flag);

        if (flag) {
            Iterable<Result<Item>> myObjects = listObjects(bucketName);
            for (Result<Item> result : myObjects) {
                Item item = result.get();
                listObjectNames.add(item.objectName());
            }
        } else {
            listObjectNames.add(" Bucket does not exist ");
        }

        LOGGER.info("MinioUtil | listObjectNames | listObjectNames size : " + listObjectNames.size());

        return listObjectNames;
    }

    @SneakyThrows
    public void putObject(String filePath, String bucketName) {
        bucketName = StringUtils.isNotBlank(bucketName) ? bucketName : minioProperties.getBucketName();

        if (!this.bucketExists(bucketName)) {
            this.makeBucket(bucketName);
        }


        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            throw new IOException("File not found: " + filePath);
        }

        LocalDateTime timeCreated = LocalDateTime.now();
        String objectName = timeCreated.toString() + "_" + file.getName(); // Use underscore for better readability

        try (FileInputStream fis = new FileInputStream(file)) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(fis, file.length(), -1)
                            .build());
        }

        // String fileName = multipartFile.getOriginalFilename();

        // LocalDateTime timeCreated = LocalDateTime.now();

        // String objectName = timeCreated.toString()
        //         + fileName.substring(fileName.lastIndexOf("."));

        // minioClient.putObject(
        //         PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(
        //             multipartFile.getInputStream(), multipartFile.getSize(), -1)
        //                 .contentType(multipartFile.getContentType())
        //                 .build());

        // return Import.builder()
        //              .status(ImportStatus.SUCCESSFUL)
        //              .userName(username)
        //              .time(timeCreated)
        //              .objectName(objectName)
        //              .build();
    }

    @SneakyThrows
    public boolean removeObject(String bucketName, List<String> objectNames) {
        LOGGER.info("MinioUtil | removeObject is called");

        boolean flag = bucketExists(bucketName);
        LOGGER.info("MinioUtil | removeObject | flag : " + flag);

        if (flag) {
            List<DeleteObject> objects = new LinkedList<>();
            for (int i = 0; i < objectNames.size(); i++) {
                objects.add(new DeleteObject(objectNames.get(i)));
            }
            Iterable<Result<DeleteError>> results =
                    minioClient.removeObjects(
                            RemoveObjectsArgs.builder().bucket(bucketName).objects(objects).build());

            for (Result<DeleteError> result : results) {
                DeleteError error = result.get();

                LOGGER.info("MinioUtil | removeObject | error : " + error.objectName() + " " + error.message());

                return false;
            }
        }
        return true;
    }

    @SneakyThrows
    public StatObjectResponse statObject(String bucketName, String objectName) {

        StatObjectResponse stat =
                minioClient.statObject(
                        StatObjectArgs.builder().bucket(bucketName).object(objectName).build());

        LOGGER.info("MinioUtil | statObject | stat : " + stat.toString());

        return stat;
    }

    @SneakyThrows
    public InputStream getObject(String bucketName, String objectName) {
        bucketName = StringUtils.isNotBlank(bucketName) ? bucketName : minioProperties.getBucketName();


        StatObjectResponse statObject = statObject(bucketName, objectName);
        if (statObject != null && statObject.size() > 0) {
            InputStream stream =
                    minioClient.getObject(
                            GetObjectArgs.builder()
                                    .bucket(bucketName)
                                    .object(objectName)
                                    .build());

            return stream;
        }
        return null;
    }

    @SneakyThrows
    public String getObjectUrl(String bucketName, String objectName) {

        LOGGER.info("MinioUtil | getObjectUrl is called");
        boolean flag = bucketExists(bucketName);
        LOGGER.info("MinioUtil | getObjectUrl | flag : " + flag);

        String url = "";

        if (flag) {
            url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(2, TimeUnit.MINUTES)
                            .build());
            LOGGER.info("MinioUtil | getObjectUrl | url : " + url);
        }
        return url;
    }


}