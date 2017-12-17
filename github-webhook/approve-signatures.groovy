import org.jenkinsci.plugins.scriptsecurity.scripts.*;
ScriptApproval sa = ScriptApproval.get();

def signatures=new XmlSlurper().parseText('''
<signature>
    <string>method hudson.model.AbstractItem updateByXml javax.xml.transform.stream.StreamSource</string>
    <string>method hudson.model.ItemGroup getItem java.lang.String</string>
    <string>method hudson.plugins.git.GitSCM getBranches</string>
    <string>method hudson.plugins.git.GitSCM getRepositories</string>
    <string>method hudson.plugins.git.GitSCM getUserRemoteConfigs</string>
    <string>method hudson.plugins.git.GitSCMBackwardCompatibility getExtensions</string>
    <string>method hudson.scm.SCM getBrowser</string>
    <string>method java.io.BufferedReader readLine</string>
    <string>method java.lang.AutoCloseable close</string>
    <string>method java.lang.String getBytes java.nio.charset.Charset</string>
    <string>method jenkins.model.Jenkins createProject java.lang.Class java.lang.String</string>
    <string>method jenkins.model.ModifiableTopLevelItemGroup createProjectFromXML java.lang.String java.io.InputStream</string>
    <string>new java.io.BufferedReader java.io.Reader</string>
    <string>new java.io.ByteArrayInputStream byte[]</string>
    <string>new javax.xml.transform.stream.StreamSource java.io.InputStream</string>
    <string>staticField java.nio.charset.StandardCharsets UTF_8</string>
    <string>staticMethod jenkins.model.Jenkins getInstance</string>
</signature>''');

signatures.string.each {
  sa.approveSignature(it.text());
}
