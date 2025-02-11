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
		    steps {
		        script {
		            sh '''
		            # Print current working directory
		            echo "Current directory: $(pwd)"
		
		            # Ensure the POM file exists
		            ls -la "$(pwd)"
		
		            # Use absolute path and wrap in quotes to handle spaces
		            WORKSPACE_DIR="$(pwd)"
		
		            docker run --rm \
		            -v "${WORKSPACE_DIR}:/workspace" \
		            -w "/workspace" \
		            maven:3.8.5-openjdk-17 \
		            bash -c "cd /workspace && mvn clean package -DskipTests"
		            '''
		        }
		    }
		}

        stage('Build Docker Image') {
            steps {
                script {
                    sh "docker build -t ${DOCKER_IMAGE}:latest ."
                }
            }
        }

        stage('Push Docker Image') {
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
