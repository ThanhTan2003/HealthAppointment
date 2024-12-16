package com.programmingtechie.HIS.minio.integration;

import java.util.Objects;
import java.util.UUID;

import jakarta.annotation.PostConstruct;

import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.programmingtechie.HIS.dto.response.FileUploadResponse;
import com.programmingtechie.HIS.minio.utils.ConverterUtils;

import io.minio.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MinioChannel {
    private static final String BUCKET = "resources";
    private final MinioClient minioClient;

    @PostConstruct
    private void init() {
        createBucket(BUCKET);
    }

    @SneakyThrows
    private void createBucket(final String name) {
        // Kiểm tra nếu bucket đã tồn tại
        final var found =
                minioClient.bucketExists(BucketExistsArgs.builder().bucket(name).build());
        if (!found) {
            // Tạo bucket nếu chưa tồn tại
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(name).build());

            // Thiết lập bucket là public bằng cách set policy
            final var policy =
                    """
						{
						"Version": "2012-10-17",
						"Statement": [
						{
							"Effect": "Allow",
							"Principal": "*",
							"Action": "s3:GetObject",
							"Resource": "arn:aws:s3:::%s/*"
							}
						]
						}
					"""
                            .formatted(name);
            minioClient.setBucketPolicy(
                    SetBucketPolicyArgs.builder().bucket(name).config(policy).build());
        } else {
            log.info("Bucket %s đã tồn tại.".formatted(name));
        }
    }

    @SneakyThrows
    public String upload(@NonNull final MultipartFile file) {

        log.info("Bucket: {}, file size: {}", BUCKET, file.getSize());
        final var fileName = file.getOriginalFilename();
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(BUCKET)
                    .object(fileName)
                    .contentType(
                            Objects.isNull(file.getContentType()) ? "image/png; image/jpg;" : file.getContentType())
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .build());
        } catch (Exception ex) {
            log.error("Error saving image \n {} ", ex.getMessage());
            throw new IllegalArgumentException("Unable to upload file", ex);
        }
        return minioClient.getPresignedObjectUrl(io.minio.GetPresignedObjectUrlArgs.builder()
                .method(io.minio.http.Method.GET)
                .bucket(BUCKET)
                .object(fileName)
                .build());
    }

    @SneakyThrows
    public FileUploadResponse uploadFileWithUUID(@NonNull final MultipartFile file) {
        // Lấy tên gốc của tệp
        String originalFileName = file.getOriginalFilename();

        // Tạo UUID cho tệp để đảm bảo tên duy nhất
        String uniqueFileName = UUID.randomUUID().toString() + "-" + originalFileName;

        // Tải tệp lên MinIO
        minioClient.putObject(PutObjectArgs.builder()
                .bucket("resources")
                .object(uniqueFileName) // Tên tệp đã thêm UUID
                .contentType(file.getContentType())
                .stream(file.getInputStream(), file.getSize(), -1)
                .build());

        return FileUploadResponse.builder()
                .fileName(uniqueFileName)
                .bucketName("resources")
                .build();
    }

    @SneakyThrows
    public String generateFileUrl(String fileName, String bucketName) {
        int expiresInSeconds = 1800; // 30 phút

        String fileUrl = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .method(io.minio.http.Method.GET) // Phương thức GET để tải tệp
                .bucket(bucketName) // Tên bucket
                .object(fileName) // Tên tệp đã được tải lên
                .expiry(expiresInSeconds) // Thời gian hết hạn
                .build());

        // Trả về URL truy cập tệp
        return fileUrl;
    }

    public byte[] download(String bucket, String name) {
        try (GetObjectResponse inputStream = minioClient.getObject(
                GetObjectArgs.builder().bucket(bucket).object(name).build())) {
            String contentLength = inputStream.headers().get(HttpHeaders.CONTENT_LENGTH);
            int size = StringUtils.isEmpty(contentLength) ? 0 : Integer.parseInt(contentLength);
            return ConverterUtils.readBytesFromInputStream(inputStream, size);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to download file", e);
        }
    }

    // Phương thức xóa tệp
    @SneakyThrows
    public void deleteFile(@NonNull final String fileName) {
        log.info("Deleting file: {} from bucket: {}", fileName, BUCKET);
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder().bucket(BUCKET).object(fileName).build());
            log.info("File {} đã được xóa thành công từ bucket {}", fileName, BUCKET);
        } catch (Exception ex) {
            log.error("Error deleting file: {}. Error: {}", fileName, ex.getMessage());
            throw new IllegalArgumentException("Unable to delete file", ex);
        }
    }

    // Phương thức xóa bucket
    @SneakyThrows
    public void deleteBucket(@NonNull final String bucketName) {
        try {
            Iterable<Result<Item>> objects = minioClient.listObjects(
                    ListObjectsArgs.builder().bucket(bucketName).build());
            for (Result<Item> result : objects) {
                Item item = result.get();
                deleteFile(item.objectName()); // Xóa từng đối tượng
            }
            minioClient.removeBucket(
                    RemoveBucketArgs.builder().bucket(bucketName).build());
            log.info("Bucket {} đã được xóa thành công.", bucketName);
        } catch (Exception ex) {
            log.error("Error deleting bucket: {}. Error: {}", bucketName, ex.getMessage());
            throw new IllegalArgumentException("Unable to delete bucket", ex);
        }
    }
}

// init(): Khi Spring Boot ứng dụng khởi động, nó sẽ kiểm tra và tạo một bucket MinIO nếu chưa tồn tại.
// createBucket(): Kiểm tra và tạo một bucket mới nếu chưa có, sau đó thiết lập chính sách truy cập public cho bucket.
// upload(): Tải lên tệp vào bucket MinIO và trả về URL có thể sử dụng tạm thời để truy cập tệp.
// download(): Tải xuống tệp từ MinIO và trả về dưới dạng byte array.
