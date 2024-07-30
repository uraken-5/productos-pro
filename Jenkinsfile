pipeline {
    agent any

    environment {
        DOCKER_HUB_CREDENTIALS = credentials('docker-hub-credentials-id')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                script {
                    dockerImage = docker.build("productos-pro")
                }
            }
        }

        stage('Push') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', DOCKER_HUB_CREDENTIALS) {
                        dockerImage.push("latest")
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                sshagent(['ec2-user-credentials-id']) {
                    sh """
                    ssh -o StrictHostKeyChecking=no ec2-user@your-instance-public-dns 'docker pull my-dockerhub-username/my-spring-boot-app:latest && docker stop my-app || true && docker rm my-app || true && docker run -d -p 8080:8080 --name my-app my-dockerhub-username/my-spring-boot-app:latest'
                    """
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
