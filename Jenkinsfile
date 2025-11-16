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
                    bat "./mvnw.cmd test"
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
                bat "docker compose build"
            }
        }

        stage('Deploy') {
            steps {
                bat "deploy.bat"
            }
        }
    }
}
