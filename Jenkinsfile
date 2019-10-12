library 'kypseli@master'
def podYaml = libraryResource 'podtemplates/kubectl.yml'
pipeline {
  agent {
    kubernetes {
      label 'core-demo-oc-jcasc-update'
      defaultContainer 'jnlp'
      yaml podYaml
    }
  }
  options { 
    buildDiscarder(logRotator(numToKeepStr: '5'))
    preserveStashes(buildCount: 5)
  }
  stages {
    stage('Run Groovy Scripts') {
      agent { label 'master' }
      when { 
        branch 'master'
      }
      steps {
        echo "preparing Jenkins CLI"
        sh 'curl -O http://teams-ops-ops.core-demo.svc.cluster.local/teams-ops-ops/jnlpJars/jenkins-cli.jar'
        withCredentials([usernamePassword(credentialsId: 'cli-username-token', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
          sh """
            alias cli='java -jar jenkins-cli.jar -s \'http://cjoc/cjoc/\' -auth $USERNAME:$PASSWORD'
            cli reload-jcasc-configuration
            sleep 5
            cli reload-configuration
            sleep 5
            cli build config-jobs/reprovision-masters/ -f -v
          """
        }
      }
    }
  }
}