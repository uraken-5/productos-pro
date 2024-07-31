pipeline {
    agent any
    environment {
        DOCKER_HUB_CREDENTIALS = credentials('docker-hub-credentials-id')
        SSH_CREDENTIALS = credentials('new-ssh-key')
    }
    stages {
        stage('Checkout SCM') {
            steps {
                checkout scm
            }
        }
        stage('Build JAR') {
            steps {
                sh 'mvn clean package'
            }
        }
        stage('Build and Push Docker Image') {
            steps {
                script {
                    sshagent(['new-ssh-key']) {
                        sh '''
                        ssh -o StrictHostKeyChecking=no ec2-user@ec2-98-81-10-115.compute-1.amazonaws.com '
                        cd /home/ec2-user/productos-pro &&
                        docker build -t productos-pro .
                        docker tag productos-pro jcarbalto/productos-pro:latest
                        docker push jcarbalto/productos-pro:latest'
                        '''
                    }
                }
            }
        }
        stage('Deploy to EC2') {
            steps {
                script {
                    sshagent(['new-ssh-key']) {
                        sh '''
                        ssh -o StrictHostKeyChecking=no ec2-user@ec2-98-81-10-115.compute-1.amazonaws.com '
                        docker stop productos-pro || true &&
                        docker rm productos-pro || true &&
                        docker pull jcarbalto/productos-pro:latest &&
                        docker run -d -p 8080:8080 --name productos-pro jcarbalto/productos-pro:latest'
                        '''
                    }
                }
            }
        }
    }
    post {
        always {
            script {
                cleanWs()
            }
        }
    }
}
