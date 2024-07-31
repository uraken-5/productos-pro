pipeline {
    agent any
    environment {
        DOCKER_HUB_CREDENTIALS = credentials('docker-hub-credentials-id')
        SSH_CREDENTIALS = credentials('ssh-credentials-id')
    }
    stages {
        stage('Checkout SCM') {
            steps {
                checkout scm
            }
        }
        stage('Build on EC2') {
            steps {
                script {
                    sshagent(['ssh-credentials-id']) {
                        sh '''
                        ssh -o StrictHostKeyChecking=no ec2-user@ec2-98-81-10-115.compute-1.amazonaws.com '
                        cd /home/ec2-user/productos-pro &&
                        git pull &&
                        docker build -t productos-pro .'
                        '''
                    }
                }
            }
        }
        stage('Push to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', 'docker-hub-credentials-id') {
                        sh 'docker tag productos-pro jcarbalto/productos-pro:latest'
                        sh 'docker push jcarbalto/productos-pro:latest'
                    }
                }
            }
        }
        stage('Deploy on EC2') {
            steps {
                script {
                    sshagent(['ssh-credentials-id']) {
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
      			cleanWs() // Now within a script block providing context
    		}
        }
    }
}
