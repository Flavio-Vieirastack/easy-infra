package com.infra.easyinfra.Writers;

import com.infra.easyinfra.Contracts.InfraWriter;
import com.infra.easyinfra.Entity.InfraData;
import com.infra.easyinfra.Helpers.FileOperations;

public class TfMain implements InfraWriter {
    @Override
    public void write(InfraData infraData) {
        FileOperations
                .createOrOverwriteFile(
                        infraData.getProjectRootFolder(),
                        "main.tf",
                        String.format(
                                """
                                        terraform {
                                          required_providers {
                                            aws = {
                                              source  = "hashicorp/aws"
                                              version = "5.81.0"
                                            }
                                          }
                                        }
                                        
                                        provider "aws" {
                                          region = "%s"
                                        }
                                        
                                        resource "aws_vpc" "main" {
                                          cidr_block           = "10.0.0.0/16"
                                          enable_dns_support   = true
                                          enable_dns_hostnames = true
                                          tags = {
                                            Name = "main-vpc"
                                          }
                                        }
                                        
                                        resource "aws_subnet" "public_subnet" {
                                          vpc_id                  = aws_vpc.main.id
                                          cidr_block              = "10.0.1.0/24"
                                          availability_zone       = "%sa"
                                          map_public_ip_on_launch = true
                                          tags = {
                                            Name = "public-subnet"
                                          }
                                        }
                                        
                                        resource "aws_internet_gateway" "igw" {
                                          vpc_id = aws_vpc.main.id
                                          tags = {
                                            Name = "main-igw"
                                          }
                                        }
                                        
                                        resource "aws_route_table" "public_route_table" {
                                          vpc_id = aws_vpc.main.id
                                        
                                          route {
                                            cidr_block = "0.0.0.0/0"
                                            gateway_id = aws_internet_gateway.igw.id
                                          }
                                        
                                          tags = {
                                            Name = "public-route-table"
                                          }
                                        }
                                        
                                        resource "aws_route_table_association" "public_route_table_association" {
                                          subnet_id      = aws_subnet.public_subnet.id
                                          route_table_id = aws_route_table.public_route_table.id
                                        }
                                        
                                        resource "aws_ecr_repository" "client_app" {
                                          name = "%s"
                                        }
                                        
                                        resource "aws_iam_role" "ecs_instance_role" {
                                          name = "ecs_instance_role"
                                        
                                          assume_role_policy = jsonencode({
                                            Version = "2012-10-17"
                                            Statement = [
                                              {
                                                Action = "sts:AssumeRole"
                                                Effect = "Allow"
                                                Principal = {
                                                  Service = "ec2.amazonaws.com"
                                                }
                                              }
                                            ]
                                          })
                                        }
                                        
                                        resource "aws_iam_role_policy_attachment" "ecs_instance_connect_policy_attachment" {
                                          role       = aws_iam_role.ecs_instance_role.name
                                          policy_arn = "arn:aws:iam::aws:policy/EC2InstanceConnect"
                                        }
                                        
                                        resource "aws_iam_role_policy_attachment" "ecs_instance_policy_attachment" {
                                          role       = aws_iam_role.ecs_instance_role.name
                                          policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonEC2ContainerServiceforEC2Role"
                                        }
                                        
                                        resource "aws_iam_role_policy_attachment" "ecs_instance_ssm_policy_attachment" {
                                          role       = aws_iam_role.ecs_instance_role.name
                                          policy_arn = "arn:aws:iam::aws:policy/AmazonSSMManagedInstanceCore"
                                        }
                                        
                                        resource "aws_iam_instance_profile" "ecs_instance_profile" {
                                          name = "ecs_instance_profile"
                                          role = aws_iam_role.ecs_instance_role.name
                                        }
                                        
                                        resource "aws_ecs_task_definition" "client_app_task" {
                                          family       = "%s"
                                          requires_compatibilities = ["EC2"]
                                          network_mode = "bridge"
                                          cpu          = "256"
                                          memory       = "512"
                                          depends_on = [aws_db_instance.postgres]
                                        
                                          container_definitions = jsonencode([
                                            {
                                              name      = "%s"
                                              image = "${aws_ecr_repository.client_app.repository_url}:latest"  # Aqui vem o repositório do ECR
                                              essential = true
                                              portMappings = [
                                                {
                                                  containerPort = %s,
                                                  hostPort      = %s
                                                }
                                              ]
                                              environment = [
                                                {
                                                  name  = "DB_ENDPOINT"
                                                  value = aws_db_instance.postgres.endpoint
                                                },
                                                {
                                                  name  = "DB_USERNAME"
                                                  value = var.db_username
                                                },
                                                {
                                                  name  = "DB_PASSWORD"
                                                  value = var.db_password
                                                }
                                              ]
                                            }
                                          ])
                                        }
                                        
                                        resource "aws_ecs_service" "client_app_service" {
                                          name            = "%s"
                                          cluster         = aws_ecs_cluster.client_app_cluster.id
                                          task_definition = aws_ecs_task_definition.client_app_task.arn
                                          desired_count   = 1
                                          launch_type     = "EC2"
                                        
                                          depends_on = [
                                            aws_ecs_cluster.client_app_cluster,
                                            aws_ecs_task_definition.client_app_task,
                                            aws_instance.ecs_instance
                                          ]
                                        }
                                        
                                        resource "aws_ecs_cluster" "client_app_cluster" {
                                          name = "%s"
                                        }
                                        
                                        resource "aws_instance" "ecs_instance" {
                                          ami = data.aws_ssm_parameter.ecs_ami.value  # AMI do EC2, use uma AMI que seja compatível com o Free Tier
                                          instance_type = "t2.micro"                  # Tipo de instância compatível com o Free Tier
                                        
                                          iam_instance_profile        = aws_iam_instance_profile.ecs_instance_profile.name
                                          subnet_id                   = aws_subnet.public_subnet.id
                                          vpc_security_group_ids = [aws_security_group.ecs_instance_sg.id]
                                          associate_public_ip_address = true
                                          user_data                   = <<-EOF
                                                      #!/bin/bash
                                                      sudo yum update -y
                                                      sudo amazon-linux-extras install -y ecs
                                                      echo "export DB_ENDPOINT='${aws_db_instance.postgres.endpoint}'" >> /etc/environment
                                                      sudo yum install -y docker
                                                      sudo service docker start
                                                      sudo systemctl enable docker
                                                      sudo usermod -a -G docker ec2-user
                                                      sudo start ecs
                                                      echo ECS_CLUSTER=%s >> /etc/ecs/ecs.config
                                        
                                                      # Instalar e iniciar o SSM Agent
                                                      sudo yum install -y amazon-ssm-agent
                                                      sudo systemctl start amazon-ssm-agent
                                                      sudo systemctl enable amazon-ssm-agent
                                        
                                                      cat << 'EOF_SCRIPT' > /home/ec2-user/configurar-ssl.sh
                                        #!/bin/bash
                                        
                                        sudo amazon-linux-extras enable nginx1
                                        sudo yum clean metadata
                                        sudo yum install -y nginx
                                        sudo systemctl enable nginx
                                        sudo systemctl start nginx
                                        
                                        sudo amazon-linux-extras install epel -y
                                        sudo yum install -y epel-release
                                        sudo yum install -y certbot python2-certbot-nginx
                                        
                                        certbot certonly --nginx -d %s --agree-tos --non-interactive --email %s
                                        
                                        if [ -f /etc/letsencrypt/live/%s/fullchain.pem ]; then
                                            cat << 'EOF_NGINX' | sudo tee /etc/nginx/conf.d/api.conf > /dev/null
                                        server {
                                            listen 443 ssl;
                                            server_name %s;
                                        
                                            ssl_certificate /etc/letsencrypt/live/%s/fullchain.pem;
                                            ssl_certificate_key /etc/letsencrypt/live/%s/privkey.pem;
                                        
                                            location / {
                                                proxy_pass http://localhost:%s;
                                                proxy_set_header Host localhost;
                                                proxy_set_header X-Real-IP $remote_addr;
                                                proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
                                                proxy_set_header X-Forwarded-Proto $scheme;
                                            }
                                        }
                                        EOF_NGINX
                                        
                                            sudo systemctl restart nginx
                                        else
                                            echo "Certbot falhou. Certificado SSL não instalado."
                                        fi
                                        
                                        echo "0 0 * * * root certbot renew --quiet && systemctl reload nginx" | sudo tee -a /etc/crontab
                                        
                                        EOF_SCRIPT
                                        
                                                      sudo chmod +x /home/ec2-user/configurar-ssl.sh
                                        EOF
                                        
                                          tags = {
                                            Name = "ECSInstance"
                                          }
                                        }
                                        
                                        data "aws_ssm_parameter" "ecs_ami" {
                                          name = "/aws/service/ecs/optimized-ami/amazon-linux-2/recommended/image_id"
                                        }
                                        
                                        resource "aws_security_group" "ecs_instance_sg" {
                                          name        = "ecs-instance-sg"
                                          description = "SG da instancia EC2 que roda ECS"
                                          vpc_id      = aws_vpc.main.id
                                        
                                          ingress {
                                            from_port = %s
                                            to_port   = %s
                                            protocol  = "tcp"
                                            cidr_blocks = ["0.0.0.0/0"]
                                          }
                                        
                                          ingress {
                                            from_port = 443
                                            to_port   = 443
                                            protocol  = "tcp"
                                            cidr_blocks = ["0.0.0.0/0"]
                                          }
                                        
                                          ingress {
                                            from_port = 22
                                            to_port   = 22
                                            protocol  = "tcp"
                                            cidr_blocks = ["0.0.0.0/0"]
                                          }
                                        
                                          egress {
                                            from_port = 0
                                            to_port   = 0
                                            protocol  = "-1"
                                            cidr_blocks = ["0.0.0.0/0"]
                                          }
                                        }
                                        
                                        output "instance_public_ip" {
                                          value = aws_instance.ecs_instance.public_ip
                                        }
                                        """,
                                infraData.getAwsRegion(),
                                infraData.getAwsRegion(),
                                infraData.getContainerName() + "-repository",
                                infraData.getEcsTask(),
                                infraData.getContainerName(),
                                infraData.getApplicationPort(),
                                infraData.getApplicationPort(),
                                infraData.getEcsServiceName(),
                                infraData.getEcsClusterName(),
                                infraData.getEcsClusterName(),
                                infraData.getSubDomainUrl(),
                                infraData.getEcsTaskuserEmail(),
                                infraData.getSubDomainUrl(),
                                infraData.getSubDomainUrl(),
                                infraData.getSubDomainUrl(),
                                infraData.getSubDomainUrl(),
                                infraData.getApplicationPort(),
                                infraData.getApplicationPort(),
                                infraData.getApplicationPort()
                        )
                );
    }
}
