pipeline {
    agent any

    environment {
        DOCKER_HUB_CREDENTIALS = credentials('docker-hub-credentials-id')
        SSH_CREDENTIALS = credentials('ec2-user-credentials-id')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build on EC2') {
            steps {
                sshagent(['ec2-user-credentials-id']) {
                    sh """
                    ssh -o StrictHostKeyChecking=no ec2-user@c2-98-81-10-115.compute-1.amazonaws.com '
                    cd /home/ec2-user &&
                    git pull &&
                    docker build -t productos-pro .'
                    """
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                sshagent(['ec2-user-credentials-id']) {
                    sh """
                    ssh -o StrictHostKeyChecking=no ec2-user@c2-98-81-10-115.compute-1.amazonaws.com '
                    docker login -u ${DOCKER_HUB_CREDENTIALS_USR} -p ${DOCKER_HUB_CREDENTIALS_PSW} &&
                    docker tag productos-pro jcarbalto/productos-pro:latest &&
                    docker push jcarbalto/productos-pro:latest'
                    """
                }
            }
        }

        stage('Deploy on EC2') {
            steps {
                sshagent(['ec2-user-credentials-id']) {
                    sh """
                    ssh -o StrictHostKeyChecking=no ec2-user@c2-98-81-10-115.compute-1.amazonaws.com '
                    docker pull jcarbalto/productos-pro:latest &&
                    docker stop productos-pro || true &&
                    docker rm productos-pro || true &&
                    docker run -d -p 8080:8080 --name productos-pro jcarbalto/productos-pro:latest'
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
