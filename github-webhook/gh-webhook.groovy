import hudson.model.*;
import groovy.json.JsonSlurper;

// get current thread / Executor
def thr = Thread.currentThread()
// get current build
def build = thr?.executable

def resolver = build.buildVariableResolver
def ghPayload = new JsonSlurper().parseText(resolver.resolve('payload'))
def ghEvent = resolver.resolve('X-GitHub-Event')
def ghSignature = resolver.resolve('X-Hub-Signature')

//println "param ${hardcoded_param} value : ${hardcoded_param_value}"

String GH_EVENT_PUSH='push';
String GH_EVENT_PR='pull_request';

if (GH_EVENT_PR.equals(ghEvent)){
 String ghEventAction=ghPayload.action
 String targetBarnch=ghPayload.base.ref
 String senderLogin = ghPayload.sender.login
 
 println "action: ${ghEventAction}"
}else{
  println "Unsuported event"
}
