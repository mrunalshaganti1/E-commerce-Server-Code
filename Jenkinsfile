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
                    sh 'mvn clean package -DskipTests'
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
            sh """
                export KUBECONFIG=/root/.kube/config
                
                echo "🚀 Fixing Kubernetes paths..."
                sed -i 's|C:\\\\Users\\\\Mruna\\\\.minikube|/root/.minikube|g' /root/.kube/config
                sed -i 's|\\\\|/|g' /root/.kube/config  # Convert Windows backslashes to Linux forward slashes

				echo "🚀 Applying Kubernetes deployment and service..."
				kubectl apply -f 'Kubernetes Files/mysql-deployment.yaml'
				
                echo "🚀 Applying Kubernetes deployment and service..."
                kubectl apply -f 'Kubernetes Files/backend-deployment.yaml'

                echo "✅ Verifying deployment..."
                kubectl rollout status deployment/backend-deployment
            """
        }
    }
}


    }
}
