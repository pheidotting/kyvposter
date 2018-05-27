pipeline {
    agent any
    stages {
        stage ('Initialize') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                '''
            }
        }

        stage ('Build') {
            steps {
                sh '''
                    mvn clean package
                '''
            }
        }


        stage ('Undeploy Testbak') {
            steps {
                slackSend (color: '#4245f4', message: "${env.JOB_NAME} Start Builden")
                sh '''
                    ssh jetty@192.168.91.230 rm -f /opt/jetty/webapps/kyvposter.war
                '''
            }
        }

        stage ('Deployment Testbak') {
            steps {
                slackSend (color: '#4245f4', message: "Deploy naar testbak :  '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
                sh '''
                    scp src/main/resources/tst2/kyv.app.properties jetty@192.168.91.230:/opt/jetty
                    scp src/main/resources/tst2/kyv.log4j.xml jetty@192.168.91.230:/opt/jetty
                    scp target/kyvposter.war jetty@192.168.91.230:/opt/jetty/webapps
                '''
            }
        }
    }
    post {
        success {
            slackSend (color: '#4245f4', message: "Afgerond : '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
        }
        failure {
            slackSend (color: '#FF0000', message: "FOUT : '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
        }
    }
}

def commitMessage() {
    sh 'git log --format=%B -n 1 HEAD > commitMessage'
    def commitMessage = readFile('commitMessage')
    sh 'rm commitMessage'
    commitMessage
}