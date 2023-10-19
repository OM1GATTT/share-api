package top.om1ga.share.content.config;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月17日 18:48
 * @description MinioUtil
 */

@Configuration
public class MinioConfig {

    @Value("${minio.endPoint}")
    private String endPoint;

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    private MinioClient instance;

    @PostConstruct
    public void init() {
        instance = MinioClient.builder()
                .endpoint(endPoint)
                .credentials(accessKey, secretKey)
                .build();
    }

    /**
     * 判断 bucket 是否存在
     *
     * @param bucketName 桶名称
     * @return 是否存在
     */
    private boolean bucketExists(String bucketName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return instance.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    /**
     * 创建桶
     *
     * @param bucketName 桶名称
     */
    public void makeBucket(String bucketName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        boolean flag = bucketExists(bucketName);

        if (!flag) {
            instance.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    /**
     * 上传文件
     *
     * @param bucketName 桶名称
     * @param objectName 文件名称
     * @param filePath 文件路径
     * @return
     */
    public ObjectWriteResponse uploadObject(String bucketName, String objectName, String filePath) throws Exception {
        return instance.uploadObject(UploadObjectArgs.builder().bucket(bucketName).object(objectName).filename(filePath).build());
    }

    public void removeObject(String bucketName, String objectName) throws Exception {
        instance.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }

    /**
     * 文件上传
     *
     * @param bucketName 存储桶名称
     * @param objectName 文件名
     * @param filePath   存储目录
     * @return 响应结果
     */
    public ObjectWriteResponse putObject(String bucketName, String objectName,InputStream inputStream) throws Exception {
        PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .stream(inputStream, -1, 10485760)
                .build();
        return instance.putObject(putObjectArgs);
    }

    public void removeBucket(String bucketName) throws Exception {
        if (bucketExists(bucketName)) {
            instance.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
        }
    }

    /**
     * 根据桶名称和图片名称获取图片预览url
     *
     * @param bucketName 桶名称
     * @param fileName 文件名称
     * @return 图片url
     */
    public String getPreviewFileUrl(String bucketName, String fileName) {
        try {
            return instance.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().method(Method.GET).expiry(7, TimeUnit.DAYS).bucket(bucketName).object(fileName).build());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
