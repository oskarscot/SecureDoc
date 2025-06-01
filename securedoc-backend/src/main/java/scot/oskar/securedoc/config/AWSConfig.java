package scot.oskar.securedoc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AWSConfig {

  @Value("${app.aws.region}")
  private String awsRegion;

    @Value("${app.aws.access-key-id}")
    private String awsAccessKeyId;

    @Value("${app.aws.secret-access-key}")
    private String awsSecretAccessKey;

  @Bean
  public S3Client s3Client() {
    return S3Client.builder()
        .region(Region.of(awsRegion))
        //.credentialsProvider(EnvironmentVariableCredentialsProvider.create())
            .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(awsAccessKeyId, awsSecretAccessKey)))
        .build();
  }

}
