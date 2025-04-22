package com.infra.easyinfra.Entity;

import com.infra.easyinfra.Enum.TempKeys;
import com.infra.easyinfra.Helpers.FileOperations;

public class InfraData {
    private String projectRootFolder;
    private String awsRegion;

    public String getProjectRootFolder() {
        return FileOperations.readFile(TempKeys.PROJECT_ROOT_FOLDER.getKey());
    }

    public String getAwsRegion() {
        return FileOperations.readFile(TempKeys.AWS_REGION.getKey());
    }
}
