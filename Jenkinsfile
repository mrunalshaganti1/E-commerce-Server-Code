pipeline {
    agent {
        docker {
            image 'maven:3.8.5-openjdk-17'  // Use a Maven container
            args '-v /var/run/docker.sock:/var/run/docker.sock'  // Allows Docker commands
        }
    }

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
            steps {
                script {
                    sh 'mvn clean package -DskipTests'  // Build JAR file
                }
            }
        }

        stage('Build Docker Image on Host') {
            steps {
                script {
                    sh "docker build -t ${DOCKER_IMAGE}:latest ."
                }
            }
        }

        stage('Push Docker Image to Docker Hub') {
            environment {
                registryCredential = 'dockerhublogin'
            }
            steps {
                script {
                    sh "docker push ${DOCKER_IMAGE}:latest"
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
