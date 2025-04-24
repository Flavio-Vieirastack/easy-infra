package com.infra.easyinfra.Writers;

import com.infra.easyinfra.Contracts.InfraWriter;
import com.infra.easyinfra.Entity.InfraData;
import com.infra.easyinfra.Helpers.FileOperations;

public class DockerFile implements InfraWriter {
    @Override
    public void write(InfraData infraData) {
        FileOperations.createOrOverwriteFile(
                infraData.getProjectRootFolder().replace("/infra", ""),
                "Dockerfile",
                String.format(
                        """
                        FROM eclipse-temurin:21-jdk-alpine as builder
                        
                        RUN apk add --no-cache maven git
                        
                        WORKDIR /build
                        
                        COPY . .
                        
                        RUN mvn clean package -DskipTests
                        
                        FROM eclipse-temurin:21-jdk-alpine
                        
                        WORKDIR /app
                        
                        COPY --from=builder /build/target/*.jar app.jar
                        
                        EXPOSE %s
                        
                        ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=70.0", "-jar", "app.jar"]
                        """,
                        infraData.getApplicationPort()
                )
        );
    }
}
