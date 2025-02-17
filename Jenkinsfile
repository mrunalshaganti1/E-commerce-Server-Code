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
        
        stage('SonarQube Analysis'){
        	steps{
        		withSonarQubeEnv('SonarQube'){
        			sh "mvn clean verify sonar:sonar \
						  -Dsonar.projectKey=FullStackBackEndCodeCheck \
						  -Dsonar.host.url=http://host.docker.internal:9000 \
						  -Dsonar.login=sqp_c1e52e1922a9a249ed38f14489b785d56055d5a7"
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
        
        stage('Trivy Check'){
        	steps{
        		sh "trivy image --format table -o trivy-scan-report.txt ${DOCKER_IMAGE}:latest"
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
        withCredentials([file(credentialsId: 'kubernetes', variable: 'KUBECONFIG')]) {
            script {
                sh """
                    export KUBECONFIG=${KUBECONFIG}
                    
                    echo "🚀 Fixing Kubernetes paths..."
                    sed -i 's|C:\\\\Users\\\\Mruna\\\\.minikube|/root/.minikube|g' ${KUBECONFIG}
                    sed -i 's|\\\\|/|g' ${KUBECONFIG}  # Convert Windows paths to Linux

                    echo "🔍 Checking Kubernetes API..."
                    kubectl cluster-info

                    echo "🚀 Applying Kubernetes deployment and service..."
                    kubectl apply -f 'Kubernetes Files/mysql-deployment.yaml'
                    kubectl apply -f 'Kubernetes Files/backend-deployment.yaml'

                    echo "✅ Verifying deployment..."
                    kubectl rollout status deployment/backend-deployment
                """
            }
        }
    }
}


    }
}
