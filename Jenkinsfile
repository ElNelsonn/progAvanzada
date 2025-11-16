pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    credentialsId: 'github-credentials-2',
                    url: 'https://github.com/ElNelsonn/progAvanzada.git'
            }
        }

        stage('Run Unit Tests') {
            steps {
                dir('app') {
                    sh "chmod +x mvnw"
                    sh "./mvnw test"
                }
            }
            post {
                always {
                    junit 'app/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker compose build"
            }
        }

        stage('Deploy') {
            steps {
                sh "docker compose down"
                sh "docker compose up -d"
            }
        }
    }
}

