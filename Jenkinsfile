pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "mrunal616/e-commerce-fullstack-backend-server"
    }

    stages {
        stage('Checkout Source Code') {
            steps {
                git 'https://github.com/mrunalshaganti1/E-commerce-Server-Code.git'
            }
        }

        stage('Build and Test') {
            agent {
                docker {
                    image 'maven:3.8.5-openjdk-17'
                    args '--user root -v /var/run/docker.sock:/var/run/docker.sock'
                }
            }
            steps {
                script {
                    sh 'mvn clean install -DskipTests'
                    sh 'ls -la target'  // Ensure JAR file is created
                }
            }
        }

        stage('Build Docker Image on Host') {
            steps {
                script {
                    sh "DOCKER_HOST=unix:///var/run/docker.sock docker build -t ${DOCKER_IMAGE}:latest ."
                }
            }
        }

        stage('Push Docker Image to Docker Hub') {
            environment {
                registryCredential = 'dockerhublogin'
            }
            steps {
                script {
                    sh "DOCKER_HOST=unix:///var/run/docker.sock docker push ${DOCKER_IMAGE}:latest"
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    sh "kubectl set image deployment/backend-deployment backend=${DOCKER_IMAGE}:latest"
                }
            }
        }
    }
}
