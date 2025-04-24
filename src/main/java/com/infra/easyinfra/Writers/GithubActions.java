package com.infra.easyinfra.Writers;

import com.infra.easyinfra.Contracts.InfraWriter;
import com.infra.easyinfra.Entity.InfraData;
import com.infra.easyinfra.Helpers.FileOperations;

public class GithubActions implements InfraWriter {
    @Override
    public void write(InfraData infraData) {
        FileOperations
                .createOrOverwriteFile(
                        infraData.getProjectRootFolder() + "/.github/workflows",
                        "deploy.yml",
                        String.format(
                                """
                                name: Terraform Deploy
                                
                                on:
                                  push:
                                    branches:
                                      - prod
                                  pull_request:
                                    branches:
                                      - prod
                                
                                jobs:
                                  terraform:
                                    name: Apply Terraform
                                    runs-on: ubuntu-latest
                                
                                    outputs:
                                      db_endpoint: ${{ steps.output.outputs.db_endpoint }}
                                
                                    env:
                                      TF_LOG: INFO
                                      AWS_ACCESS_KEY_ID: ${{ secrets.AWS_ACCESS_KEY_ID }}
                                      AWS_SECRET_ACCESS_KEY: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
                                      AWS_DEFAULT_REGION: us-east-1
                                      TF_VAR_db_username: ${{ secrets.DB_USERNAME }}
                                      TF_VAR_db_password: ${{ secrets.DB_PASSWORD }}
                                      DB_USERNAME: ${{ secrets.DB_USERNAME }}
                                      DB_PASSWORD: ${{ secrets.DB_PASSWORD }}
                                
                                    steps:
                                      - name: Checkout repository
                                        uses: actions/checkout@v4
                                
                                      - name: Set up Terraform
                                        uses: hashicorp/setup-terraform@v3
                                        with:
                                          terraform_version: 1.11.4
                                          terraform_wrapper: false
                                
                                      - name: Terraform Init
                                        run: |
                                          cd infra
                                          terraform init
                                
                                      - name: Terraform Format
                                        run: |
                                          cd infra
                                          terraform fmt
                                
                                      - name: Terraform Format Check
                                        run: |
                                          cd infra
                                          terraform fmt -check
                                
                                      - name: Terraform Validate
                                        run: |
                                          cd infra
                                          terraform validate
                                
                                      - name: Terraform Plan
                                        run: |
                                          cd infra
                                          terraform plan
                                
                                      - name: Terraform Apply
                                        if: github.ref == 'refs/heads/prod' && github.event_name == 'push'
                                        run: |
                                          cd infra
                                          terraform apply -auto-approve
                                
                                      - name: Get db_endpoint from Terraform output
                                        id: output
                                        run: |
                                          cd infra
                                          echo "db_endpoint=$(terraform output -raw db_endpoint)" >> $GITHUB_ENV
                                          echo "db_endpoint=${{ steps.output.outputs.db_endpoint }}"
                                          echo "db_endpoint=$(terraform output -raw db_endpoint)" >> $GITHUB_OUTPUT
                                
                                  docker:
                                    name: Build and Push Docker Image to ECR
                                    runs-on: ubuntu-latest
                                    needs: terraform
                                
                                    env:
                                      AWS_ACCESS_KEY_ID: ${{ secrets.AWS_ACCESS_KEY_ID }}
                                      AWS_SECRET_ACCESS_KEY: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
                                      AWS_DEFAULT_REGION: us-east-1
                                      DB_ENDPOINT: ${{ needs.terraform.outputs.db_endpoint }}
                                      DB_USERNAME: ${{ secrets.DB_USERNAME }}
                                      DB_PASSWORD: ${{ secrets.DB_PASSWORD }}
                                
                                    steps:
                                      - name: Checkout repository
                                        uses: actions/checkout@v4
                                
                                      - name: Set up Docker
                                        uses: docker/setup-buildx-action@v2
                                
                                      - name: Log in to Amazon ECR
                                        run: |
                                          aws ecr get-login-password --region ${{ secrets.AWS_DEFAULT_REGION }} | docker login --username AWS --password-stdin ${{ secrets.AWS_ACCOUNT_ID }}.dkr.ecr.${{ secrets.AWS_DEFAULT_REGION }}.amazonaws.com
                                
                                      - name: Install docker-compose
                                        run: |
                                          sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
                                          sudo chmod +x /usr/local/bin/docker-compose
                                          docker-compose version
                                
                                      - name: Print DB endpoint (debug)
                                        run: echo "DB endpoint is $DB_ENDPOINT"
                                
                                      - name: Build Docker Image with Docker Compose
                                        run: |
                                          docker-compose -f docker-compose.yml build
                                
                                      - name: Tag Docker image for ECR
                                        run: |
                                          docker tag %s:latest ${{ secrets.AWS_ACCOUNT_ID }}.dkr.ecr.${{ secrets.AWS_DEFAULT_REGION }}.amazonaws.com/%s-repository:latest
                                
                                      - name: Push Docker Image to ECR
                                        run: |
                                          docker push ${{ secrets.AWS_ACCOUNT_ID }}.dkr.ecr.${{ secrets.AWS_DEFAULT_REGION }}.amazonaws.com/%s-repository:latest
                                """,
                                infraData.getContainerName(),
                                infraData.getContainerName(),
                                infraData.getContainerName()
                        )
                );
    }
}
