package com.infra.easyinfra.Writers;

import com.infra.easyinfra.Contracts.InfraWriter;
import com.infra.easyinfra.Entity.InfraData;
import com.infra.easyinfra.Helpers.FileOperations;

public class TfBackend implements InfraWriter {
    @Override
    public void write(InfraData infraData) {
        FileOperations.createOrOverwriteFile(
                infraData.getInfraFolder(),
                "backend.tf",
                String.format(
                        """
                                terraform {
                                  backend "s3" {
                                    bucket  = "terraform-back-orca-facil"
                                    key     = "infra/terraform.tfstate"
                                    region  = "%s"
                                    encrypt = true
                                  }
                                }
                                """, infraData.getAwsRegion()
                )
        );
    }
}
