pipeline {
    agent {
        docker {
            image 'docker:latest'
            args '--user root -v /var/run/docker.sock:/var/run/docker.sock'
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
                sh "mvn clean package -DskipTests"
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${DOCKER_IMAGE}:latest ."
            }
        }

        stage('Push Docker Image') {
            steps {
                sh "docker push ${DOCKER_IMAGE}:latest"
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                sh "kubectl set image deployment/backend-deployment backend=${DOCKER_IMAGE}:latest"
            }
        }
    }
}
