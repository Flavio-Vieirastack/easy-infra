package com.infra.easyinfra.Enum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TempKeys {
    PROJECT_ROOT_FOLDER("root-folder"),
    AWS_REGION("aws_region"),
    S3_BUCKETS("s3_buckets"),
    ECS_TASK("ecs_task"),
    CONTAINER_NAME("container_name"),
    ECS_SERVICE_NAME("ecs_service_name"),
    ECS_CLUSTER_NAME("ecs_cluster_name"),
    APPLICATION_PORT("application_port"),
    SUBDOMAIN_URL("subdomain_url"),
    USER_EMAIL("user_email"),
    DB_NAME("db_name");
    private final String key;

}
