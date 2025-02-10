pipeline {
    environment {
        dockerimagename = "mrunal616/e-commerce-fullstack-backend-server"
    }

    agent any

    stages {

        stage('Checkout Source Code') {
            steps {
                git 'https://github.com/mrunalshaganti1/E-commerce-Server-Code.git'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    dockerImage = docker.build(dockerimagename)
                }
            }
        }

        stage('Push Image to Docker Hub') {
            environment {
                registryCredential = 'dockerhublogin'  // Ensure this is added in Jenkins credentials
            }
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', registryCredential) {
                        dockerImage.push("latest")
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    kubernetesDeploy(
                        configs: ["Kubernetes Files/backend-deployment.yaml"], 
                        kubeconfigId: "kubernetes"  // Ensure this is stored in Jenkins credentials
                    )
                }
            }
        }
    }
}
