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

