package com.infra.easyinfra.Writers;

import com.infra.easyinfra.Contracts.InfraWriter;
import com.infra.easyinfra.Entity.InfraData;
import com.infra.easyinfra.Helpers.FileOperations;

public class TfDb implements InfraWriter {
    @Override
    public void write(InfraData infraData) {
        FileOperations
                .createOrOverwriteFile(
                        infraData.getInfraFolder(),
                        "db.tf",
                        String.format(
                                """
                                        # RDS PostgreSQL (Free Tier)
                                        resource "aws_db_instance" "postgres" {
                                          identifier             = "postgres-db"
                                          engine                 = "postgres"
                                          engine_version         = "15.12"
                                          instance_class         = "db.t3.micro"
                                          allocated_storage      = 20
                                          storage_type           = "gp2"
                                          username               = var.db_username
                                          password               = var.db_password
                                        
                                          skip_final_snapshot    = true
                                          publicly_accessible    = false
                                          vpc_security_group_ids = [aws_security_group.db_sg.id]
                                          db_name                = "%s"
                                          db_subnet_group_name  = aws_db_subnet_group.db_subnet_group.name
                                          multi_az = false
                                        }
                                        
                                        
                                        resource "aws_security_group" "ecs_sg" {
                                          name        = "ecs-service-sg"
                                          description = "Allow ECS containers to access RDS"
                                          vpc_id      = aws_vpc.main.id
                                        }
                                        
                                        resource "aws_security_group" "db_sg" {
                                          name        = "postgresql-sg"
                                          description = "Allow PostgreSQL inbound traffic from ECS"
                                          vpc_id      = aws_vpc.main.id
                                        
                                          ingress {
                                            from_port       = 5432
                                            to_port         = 5432
                                            protocol        = "tcp"
                                            security_groups = [aws_security_group.ecs_instance_sg.id]
                                            description     = "Enable access to ECS"
                                          }
                                          egress {
                                            from_port   = 0
                                            to_port     = 0
                                            protocol    = "-1"
                                            cidr_blocks = ["0.0.0.0/0"]
                                          }
                                        }
                                        
                                        resource "aws_db_subnet_group" "db_subnet_group" {
                                          name       = "main-db-subnet-group"
                                          subnet_ids = [
                                            aws_subnet.public_subnet.id,
                                            aws_subnet.public_subnet_2.id
                                          ]
                                        
                                          tags = {
                                            Name = "Main DB Subnet Group"
                                          }
                                        }
                                        
                                        
                                        resource "aws_subnet" "public_subnet_2" {
                                          vpc_id                  = aws_vpc.main.id
                                          cidr_block              = "10.0.2.0/24"
                                          availability_zone       = "%sb"
                                          map_public_ip_on_launch = true
                                        
                                          tags = {
                                            Name = "public-subnet-2"
                                          }
                                        }
                                        
                                        
                                        output "db_endpoint" {
                                          value = aws_db_instance.postgres.endpoint
                                        }
                                        """,
                                infraData.getDbName(),
                                infraData.getAwsRegion()
                        )
                );
    }
}
