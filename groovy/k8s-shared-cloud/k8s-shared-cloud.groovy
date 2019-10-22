import hudson.model.*;

import com.cloudbees.hudson.plugins.folder.AbstractFolder

import com.cloudbees.opscenter.bluesteel.BlueSteelConstants

import jenkins.model.*;

import java.util.logging.Logger

Logger logger = Logger.getLogger("init_02_create-eval-job.groovy")

def j = Jenkins.instance

AbstractFolder teamsFolder = j.model.Jenkins.instanceOrNull.getItemByFullName(BlueSteelConstants.CJOC_TEAMS_FOLDER_NAME, AbstractFolder.class)
if (teamsFolder == null) {
    teamsFolder = j.createProject(Folder.class, BlueSteelConstants.CJOC_TEAMS_FOLDER_NAME);
}

def name = 'kubernetes shared cloud'
logger.info("creating $name job")
def job = teamsFolder.getItem(name)
if (job != null) {
  logger.info("job $name already existed so deleting")
  job.delete()
}
println "--> creating $name"

def configXml = """
<com.cloudbees.opscenter.clouds.kubernetes.KubernetesConfiguration plugin="operations-center-kubernetes-cloud@2.176.0.1">
  <actions/>
  <description></description>
  <snippets>
    <com.cloudbees.opscenter.clouds.kubernetes.KubernetesCloudConfigurationSnippet>
      <value>
        <string>INSERT_XML</string>
      </value>
    </com.cloudbees.opscenter.clouds.kubernetes.KubernetesCloudConfigurationSnippet>
  </snippets>
  <properties/>
</com.cloudbees.opscenter.clouds.kubernetes.KubernetesConfiguration>
"""
def p = teamsFolder.createProjectFromXML(name, new ByteArrayInputStream(configXml.getBytes("UTF-8")));