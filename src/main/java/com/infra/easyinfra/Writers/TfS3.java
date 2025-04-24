package com.infra.easyinfra.Writers;

import com.infra.easyinfra.Contracts.InfraWriter;
import com.infra.easyinfra.Entity.InfraData;
import com.infra.easyinfra.Helpers.FileOperations;

public class TfS3 implements InfraWriter {
    @Override
    public void write(InfraData infraData) {
        if (infraData.getS3Buckets() != null && !infraData.getS3Buckets().isEmpty()) {
            StringBuilder buckets = new StringBuilder();
            for (String bucketName : infraData.getS3Buckets()) {
                buckets.append(String.format(
                        """
                                resource "aws_s3_bucket" "bucket" {
                                  bucket = "%s"
                                }
                                """,
                        bucketName
                ));
            }
            FileOperations
                    .createOrOverwriteFile(
                            infraData.getInfraFolder(),
                            "s3.tf",
                            buckets.toString()
                    );
        }
    }
}
