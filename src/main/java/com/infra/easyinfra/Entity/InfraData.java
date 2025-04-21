package com.infra.easyinfra.Entity;

import com.infra.easyinfra.Constants.TempConstants;
import com.infra.easyinfra.Helpers.FileOperations;

public class InfraData {
    private String projectRootFolder;

    public String getProjectRootFolder() {
        return FileOperations.readFile(TempConstants.PROJECT_ROOT_FOLDER);
    }
}
