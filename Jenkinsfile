pipeline {
    agent any

    environment {
    	DOCKER_HOST = "tcp://host.docker.internal:2375"
        DOCKER_IMAGE = "mrunal616/e-commerce-fullstack-backend-server"
    }

    stages {
        stage('Checkout Source Code') {
            steps {
                git 'https://github.com/mrunalshaganti1/E-commerce-Server-Code.git'
            }
        }

        stage('Build Docker Image on Host') {
            steps {
                script {
                    sh "export DOCKER_HOST=tcp://host.docker.internal:2375 && docker build -t ${DOCKER_IMAGE}:latest ."
                }
            }
        }

        stage('Push Docker Image to Docker Hub') {
            environment {
                registryCredential = 'dockerhublogin'
            }
            steps {
                script {
                    sh "export DOCKER_HOST=tcp://host.docker.internal:2375 && docker push ${DOCKER_IMAGE}:latest"
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    sh "export DOCKER_HOST=tcp://host.docker.internal:2375 && kubectl set image deployment/backend-deployment backend=${DOCKER_IMAGE}:latest"
                }
            }
        }
    }
}
