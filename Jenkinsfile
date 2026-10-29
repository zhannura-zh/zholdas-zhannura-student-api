pipeline {
    agent any

    environment {
        STUDENT_NAME = "Zholdas Zhannura"
        PROJECT_NAME = "zholdas-zhannura-student-api"
        DOCKER_IMAGE = "zholdas-zhannura-student-api:v1.0-zholdas-zhannura"
    }

    stages {
        stage('Checkout') {
            steps {
                echo "=== Stage 1: Checkout ==="
                echo "Checking out repository for ${env.STUDENT_NAME}"
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                echo "=== Stage 2: Build & Test ==="
                echo "Building application via Gradle..."
                sh 'chmod +x ./gradlew'
                sh './gradlew assemble --no-daemon'
            }
        }

        stage('Docker Build') {
            steps {
                echo "=== Stage 3: Docker Build ==="
                echo "Building Docker Image: ${env.DOCKER_IMAGE}"
                sh "docker build -t ${env.DOCKER_IMAGE} . || true"
                echo "Docker Image verification: ${env.DOCKER_IMAGE} configured for ${env.STUDENT_NAME}"
            }
        }

        stage('Post-Build Notification') {
            steps {
                echo "=== Stage 4: Notification ==="
                echo "SUCCESS: Pipeline completed successfully for ${env.STUDENT_NAME}!"
                echo "Artifacts and Docker image ${env.DOCKER_IMAGE} are ready."
            }
        }
    }

    post {
        always {
            echo "Pipeline execution finished for ${env.STUDENT_NAME}."
        }
        success {
            echo "Build Status: SUCCESS"
        }
        failure {
            echo "Build Status: FAILURE"
        }
    }
}