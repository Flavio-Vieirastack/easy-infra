package com.infra.easyinfra.Writers;

import com.infra.easyinfra.Contracts.InfraWriter;
import com.infra.easyinfra.Entity.InfraData;
import com.infra.easyinfra.Helpers.FileOperations;

public class TfElasticIp implements InfraWriter {
    @Override
    public void write(InfraData infraData) {
        FileOperations
                .createOrOverwriteFile(
                        infraData.getInfraFolder(),
                        "elastic_ip.tf",
                        """
                                resource "aws_eip" "api_eip" {
                                  tags = {
                                    Name = "api-eip"
                                  }
                                }
                                
                                resource "aws_eip_association" "api_eip_assoc" {
                                  instance_id   = aws_instance.ecs_instance.id
                                  allocation_id = aws_eip.api_eip.id
                                }
                                
                                output "api_public_ip" {
                                  description = "Fixed IP of ec2 instance"
                                  value       = aws_eip.api_eip.public_ip
                                }
                                """
                );
    }
}
