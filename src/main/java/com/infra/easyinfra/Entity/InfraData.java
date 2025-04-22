package com.infra.easyinfra.Entity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infra.easyinfra.Enum.TempKeys;
import com.infra.easyinfra.Helpers.FileOperations;
import lombok.SneakyThrows;

import java.util.List;

public class InfraData {
    private String projectRootFolder;
    private String awsRegion;
    private List<String> s3Buckets;
    private String dbName;

    public String getProjectRootFolder() {
        return FileOperations.readFile(TempKeys.PROJECT_ROOT_FOLDER.getKey());
    }

    public String getAwsRegion() {
        return FileOperations.readFile(TempKeys.AWS_REGION.getKey());
    }

    @SneakyThrows
    public List<String> getS3Buckets() {
        ObjectMapper mapper = new ObjectMapper();
        String json = FileOperations.readFile(TempKeys.S3_BUCKETS.getKey());
        return mapper.readValue(json, new TypeReference<>() {});
    }

    public String getDbName() {
        return FileOperations.readFile(TempKeys.DB_NAME.getKey());
    }
}
