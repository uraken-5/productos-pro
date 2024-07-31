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
                sshagent(['ssh-credentials-id']) {
                    sh '''
                    ssh -o StrictHostKeyChecking=no ec2-user@ec2-98-81-10-115.compute-1.amazonaws.com '
                    if [ ! -d "/home/ec2-user/productos-pro" ]; then
                        git clone https://github.com/uraken-5/productos-pro.git /home/ec2-user/productos-pro;
                    else
                        cd /home/ec2-user/productos-pro &&
                        git pull;
                    fi &&
                    cd /home/ec2-user/productos-pro &&
                    docker build -t productos-pro .'
                    '''
                }
            }
        }
        stage('Push to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', 'docker-hub-credentials-id') {
                        sh 'docker tag productos-pro your-dockerhub-username/productos-pro:latest'
                        sh 'docker push your-dockerhub-username/productos-pro:latest'
                    }
                }
            }
        }
        stage('Deploy on EC2') {
            steps {
                sshagent(['ssh-credentials-id']) {
                    sh '''
                    ssh -o StrictHostKeyChecking=no ec2-user@ec2-98-81-10-115.compute-1.amazonaws.com '
                    docker stop productos-pro || true &&
                    docker rm productos-pro || true &&
                    docker pull your-dockerhub-username/productos-pro:latest &&
                    docker run -d -p 8080:8080 --name productos-pro your-dockerhub-username/productos-pro:latest'
                    '''
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