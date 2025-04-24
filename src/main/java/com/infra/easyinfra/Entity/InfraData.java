package com.infra.easyinfra.Entity;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InfraData {
    private static InfraData instance;

    private String projectRootFolder;
    private String awsRegion;
    private List<String> s3Buckets;
    private String ecsTask;
    private String containerName;
    private String ecsServiceName;
    private String ecsClusterName;
    private String applicationPort;
    private String subDomainUrl;
    private String userEmail;
    private String dbName;

    private InfraData() {}

    public static InfraData getInstance() {
        if (instance == null) {
            instance = new InfraData();
        }
        return instance;
    }

    public String getInfraFolder() {
        return projectRootFolder + "/infra";
    }
}

