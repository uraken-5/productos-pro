pipeline {
    agent any
    tools {
        maven 'Maven 3.9.8' // Ajusta según tu configuración de Maven en Jenkins
    }
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
        stage('Build JAR and Docker Image') {
            steps {
                sh 'mvn clean package'
                sh 'docker build -t productos-pro .'
            }
        }
        stage('Push Docker Image') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', 'docker-hub-credentials-id') {
                        sh 'docker tag productos-pro jcarbalto/productos-pro:latest'
                        sh 'docker push jcarbalto/productos-pro:latest'
                    }
                }
            }
        }
        stage('Copy JAR to EC2 and Deploy') {
            steps {
                script {
                    sshagent(['new-ssh-key']) {
                        sh '''
                        scp -o StrictHostKeyChecking=no target/productos-pro-0.0.1-SNAPSHOT.jar ec2-user@ec2-54-196-118-142.compute-1.amazonaws.com:/home/ec2-user/
                        ssh -o StrictHostKeyChecking=no ec2-user@ec2-54-196-118-142.compute-1.amazonaws.com '
                        docker pull jcarbalto/productos-pro:latest &&
                        docker stop productos-pro || true &&
                        docker rm productos-pro || true &&
                        docker run -d -p 8080:8080 --name productos-pro jcarbalto/productos-pro:latest
                        '
                        '''
                    }
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
