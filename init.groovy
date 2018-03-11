import com.cloudbees.plugins.credentials.impl.*;
import com.cloudbees.plugins.credentials.*;
import com.cloudbees.plugins.credentials.domains.*;
import org.jenkinsci.plugins.plaincredentials.impl.*;
import hudson.util.Secret;
import jenkins.model.*
import hudson.model.*
import java.net.URL;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/* TODO:
- Create Jenkins Credetential "Secret Text" with id "github-access-token"
- Create Jenkins Credential "Username and Password" with id "github-account" where password is the same as "github-access-token"
- Setup GitHub Server using the "github-access-token" credential
  - uncheck "Manage Hooks"
*/

/* LOGGER:
org.jenkinsci.plugins.workflow.multibranch
org.jenkinsci.plugins.github_branch_source
jenkins.branch
hudson.plugins.git
org.jenkinsci.plugins.github

*/
println "Initializing from remote script"


/*
method hudson.plugins.git.UserRemoteConfig getUrl
method hudson.scm.SCM getKey
method org.jenkinsci.plugins.workflow.support.steps.build.RunWrapper getRawBuild
method hudson.model.Run getPreviousBuildInProgress

*/

Credentials c1 = (Credentials) new UsernamePasswordCredentialsImpl(
  CredentialsScope.GLOBAL,
  "github-account",
  "GitHub account",
  System.getenv()['GH_USERNAME'],
  System.getenv()['GH_PASSWORD']);

SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), c1);

Credentials c2 = (Credentials) new StringCredentialsImpl(
   CredentialsScope.GLOBAL,
  "github-access-token",
  "GitHub account (Access Token)",
  Secret.fromString(System.getenv()['GH_ACCESS_TOKEN']));

SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), c2);

Jenkins.getInstance().getDescriptor(org.jenkinsci.plugins.github.config.GitHubPluginConfig.class)
def ghCofigs = Jenkins.getInstance().getDescriptor(org.jenkinsci.plugins.github.config.GitHubPluginConfig.class).getConfigs();
def ghServerConfig = new org.jenkinsci.plugins.github.config.GitHubServerConfig('github-access-token');
ghServerConfig.setName('GitHub')
ghServerConfig.setApiUrl('https://api.github.com')
ghServerConfig.setManageHooks(false);
ghServerConfig.setClientCacheSize(21)
ghCofigs.clear();
ghCofigs.add(ghServerConfig);
Jenkins.getInstance().save()


String ghTemplateJobConfigXml = new URL('https://raw.githubusercontent.com/cvarjao/openshift-jenkins-tools/master/workflow-multibranch.template.xml').getText(StandardCharsets.UTF_8.name()).trim();

System.getenv()['GH_REPOSITORIES'].split(',').each { repo ->
  def (repo_owner, repo_name) = repo.tokenize( '/' )
  def ghJobConfigXml=ghTemplateJobConfigXml.replaceAll('\\Q#{REPO_OWNER}\\E', repo_owner).replaceAll('\\Q#{REPO_NAME}\\E', repo_name);
  println "Addind GitHub job for repository '${repo_owner}' / '${repo_name}'"
  InputStream ghPushJobConfigInputStream = new ByteArrayInputStream(ghJobConfigXml.getBytes(StandardCharsets.UTF_8));
  Jenkins.instance.createProjectFromXML("${repo_owner}-${repo_name}", ghPushJobConfigInputStream);  
}

def sa = org.jenkinsci.plugins.scriptsecurity.scripts.ScriptApproval.get();
[
  'method hudson.plugins.git.UserRemoteConfig getUrl',
  'method hudson.scm.SCM getKey',
  'method org.jenkinsci.plugins.workflow.support.steps.build.RunWrapper getRawBuild',
  'method hudson.model.Run getPreviousBuildInProgress'
].each {
  sa.approveSignature(it);
}
