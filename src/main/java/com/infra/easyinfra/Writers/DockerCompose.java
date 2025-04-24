package com.infra.easyinfra.Writers;

import com.infra.easyinfra.Contracts.InfraWriter;
import com.infra.easyinfra.Entity.InfraData;
import com.infra.easyinfra.Helpers.FileOperations;

public class DockerCompose implements InfraWriter {
    @Override
    public void write(InfraData infraData) {
        FileOperations
                .createOrOverwriteFile(
                        infraData.getProjectRootFolder().replace("/infra", ""),
                        "docker-compose.yml",
                        String.format(
                                """
                                        version: '3.8'
                                        
                                        services:
                                          app:
                                            image: %s:latest
                                            build:
                                              context: .
                                              dockerfile: Dockerfile
                                            ports:
                                              - "%s:%s"
                                            environment:
                                              - AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID}
                                              - AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY}
                                              - DB_ENDPOINT=${DB_ENDPOINT}
                                              - DB_USERNAME=${DB_USERNAME}
                                              - DB_PASSWORD=${DB_PASSWORD}
                                        """,
                                infraData.getContainerName(),
                                infraData.getApplicationPort(),
                                infraData.getApplicationPort()
                        )
                );
    }
}
