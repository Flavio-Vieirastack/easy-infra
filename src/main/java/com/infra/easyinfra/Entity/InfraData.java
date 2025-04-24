package com.infra.easyinfra.Entity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infra.easyinfra.Enum.TempKeys;
import com.infra.easyinfra.Helpers.FileOperations;
import lombok.SneakyThrows;

import java.util.List;

public class InfraData {

    public String getEcsTask() {
        return FileOperations.readFile(TempKeys.ECS_TASK.getKey());
    }

    public String getEcsTaskuserEmail() {
        return FileOperations.readFile(TempKeys.USER_EMAIL.getKey());
    }

    public String getSubDomainUrl() {
        return FileOperations.readFile(TempKeys.SUBDOMAIN_URL.getKey());
    }

    public String getApplicationPort() {
        return FileOperations.readFile(TempKeys.APPLICATION_PORT.getKey());
    }

    public String getEcsClusterName() {
        return FileOperations.readFile(TempKeys.ECS_CLUSTER_NAME.getKey());
    }

    public String getContainerName() {
        return FileOperations.readFile(TempKeys.CONTAINER_NAME.getKey());
    }

    public String getEcsServiceName() {
        return FileOperations.readFile(TempKeys.ECS_SERVICE_NAME.getKey());
    }

    public String getProjectRootFolder() {
        return FileOperations.readFile(TempKeys.PROJECT_ROOT_FOLDER.getKey()) + "/infra";
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
