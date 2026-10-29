pipeline {
    agent { label 'zholdas-docker' }
    options {
        skipDefaultCheckout(true)
        disableConcurrentBuilds()
        timestamps()
        timeout(time: 20, unit: 'MINUTES')
    }
    triggers { pollSCM('H/2 * * * *') }
    environment {
        STUDENT_NAME = 'Zhannura Zholdas'
        STUDENT_GROUP = 'IT2-2306'
        STUDENT_ID = '37742'
        DOCKER_IMAGE = 'zholdas-zhannura-student-api:v1.1-zholdas-zhannura'
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
                echo "Student: ${env.STUDENT_NAME}; Group: ${env.STUDENT_GROUP}; Student ID: ${env.STUDENT_ID}"
                sh 'git rev-parse HEAD && java -version && docker version'
            }
        }
        stage('Build') {
            steps { sh './gradlew clean assemble --no-daemon --console=plain' }
        }
        stage('Test') {
            steps { sh './gradlew test --no-daemon --console=plain' }
            post { always { junit 'build/test-results/test/TEST-*.xml' } }
        }
        stage('Docker Build') {
            steps {
                sh '''docker build --label org.opencontainers.image.revision=$(git rev-parse HEAD) \
                    --label org.opencontainers.image.authors="Zhannura Zholdas" \
                    -t "$DOCKER_IMAGE" .'''
            }
        }
        stage('Verify') {
            steps {
                sh '''docker image inspect "$DOCKER_IMAGE" --format 'Image={{.Id}} Size={{.Size}} Created={{.Created}}'
test "$(git rev-parse HEAD)" = "$(docker image inspect --format '{{ index .Config.Labels "org.opencontainers.image.revision" }}' "$DOCKER_IMAGE")"
echo "Image verified for $STUDENT_NAME: $DOCKER_IMAGE"'''
            }
        }
    }
    post {
        success {
            archiveArtifacts artifacts: 'build/libs/student-api.jar', fingerprint: true
            echo "Verified CI build completed for ${env.STUDENT_NAME}. Deployment is a separate authorized operation."
        }
        failure { echo 'Build failed. Later normal stages are not executed.' }
    }
}
