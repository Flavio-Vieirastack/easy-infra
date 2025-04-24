package com.infra.easyinfra.Writers;

import com.infra.easyinfra.Contracts.InfraWriter;
import com.infra.easyinfra.Entity.InfraData;
import com.infra.easyinfra.Helpers.FileOperations;

public class TfVariables implements InfraWriter {
    @Override
    public void write(InfraData infraData) {
        FileOperations.createOrOverwriteFile(
                infraData.getInfraFolder(),
                "variables.tf",
                """
                        variable "db_username" {
                          type        = string
                          description = "RDS master username"
                        }
                        
                        variable "db_password" {
                          type        = string
                          description = "RDS master password"
                          sensitive   = true
                        }
                        """
        );
    }
}
