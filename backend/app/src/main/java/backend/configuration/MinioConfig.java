package backend.configuration;

import io.minio.MinioClient;
import lombok.Data;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import backend.services.MinioService;

@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(MinioService.class);

    /** *  It's a URL, domain name ,IPv4 perhaps IPv6 Address ") */
    private String endpoint;

    /** * //"accessKey Similar to user ID, Used to uniquely identify your account " */
    private String accessKey;

    /** * //"secretKey It's the password for your account " */
    private String secretKey;

    /** * //" Default bucket " */
    private String bucketName;

    /** *  The maximum size of the picture  */
    private long imageSize;

    /** *  Maximum size of other files  */
    private long fileSize;

    @Bean
    public MinioClient minioClient() {
        LOGGER.info("endpoint = " + endpoint);
        LOGGER.info("accessKey = " + accessKey);
        LOGGER.info("secretKey = " + secretKey);
        return MinioClient.builder()
                // .endpoint(endpoint)
                .endpoint(endpoint, 9000, false)
                .credentials(accessKey, secretKey)
                .build();
    }
}