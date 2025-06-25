package com.s3reportlambda.ConfinS3Client;

// import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
// import software.amazon.awssdk.auth.credentials.AwsCredentials;
// import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

public class S3Clientconfig {
   public static S3Client createS3Client() {
        //String accessKey = System.getenv("AWS_ACCESS_KEY_ID");
        //String secretKey = System.getenv("AWS_SECRET_ACCESS_KEY");

        //AwsCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        return S3Client.builder()
                .region(Region.US_EAST_1)
                //.credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }
}
