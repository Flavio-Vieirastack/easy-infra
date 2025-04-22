package com.infra.easyinfra.Enum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TempKeys {
    PROJECT_ROOT_FOLDER("root-folder"),
    AWS_REGION("aws_region"),
    S3_BUCKETS("s3_buckets"),
    DB_NAME("db_name");
    private final String key;

}
