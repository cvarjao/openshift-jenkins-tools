## To create a local jenkins account:
1. try to authenticate with an invalid password
    ```
    curl -k https://github-webhook:ThisIsAInvalidPassword@jenkins.example.com/whoAmI/
    ```
1. Obtain the newly created account API token via `Manage Jenkins` > `Script Console`
     ```
     import jenkins.security.*
     //j.jenkins.setSecurityRealm(j.createDummySecurityRealm());        
     User u = User.get("github-webhook")  
     ApiTokenProperty t = u.getProperty(ApiTokenProperty.class)  
     def token = t.getApiTokenInsecure()
     //token.getClass()
     println "token is $token "
     ```
    Reference: https://gist.github.com/hayderimran7/dec6a655ba671fa5b3c3
   1. you can also reveal user's api token by setting a system property (`-Djenkins.security.ApiTokenProperty.showTokenToAdmins=true`) that enables to reveal any user api token via UI.
     Reference: https://wiki.jenkins.io/display/JENKINS/Features+controlled+by+system+properties
1. Check the access by using the correct token
     ```
     curl -k https://github-webhook:%USER_API_TOKEN%@jenkins.example.com/whoAmI/
    ```
1. Grant appropriate access to the account via `Manage Jenkins` > `Configure Global Security`
1. Use token to trigger builds
     ```
     curl -k -X POST https://github-webhook:%USER_API_TOKEN%@jenkins.example.com/job/github-webhook/buildWithParameters?token=%JOB_SECRET_TOKEN%
    ```
