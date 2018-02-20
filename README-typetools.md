This is a version of Commons IO that is annotated with type annotations for the Checker Framework.


To build a version of commons-io that contains type annotations
---------------------------------------------------------------

```
mvn -B -Dmaven.test.skip=true package
```

The `.jar` file is found at, for example, `target/commons-io-2.6.jar`.


To update to a newer version of the upstream library
----------------------------------------------------

In the upstream repository, find the commit corresponding to the release.
For version 2.6, that is 2ae025fe5c4a7d2046c53072b0898e37a079fe62
which was committed on Fri Oct 20 08:23:16 2017 +0200.

So, run: 

git pull https://github.com/apache/commons-io 2ae025fe5c4a7d2046c53072b0898e37a079fe62


To upload this version of Commons IO to Maven Central
-----------------------------------------------------


# Compile, and create commons-io-javadoc.jar
mvn -B -Dmaven.test.skip=true package javadoc:javadoc && (cd target/site/apidocs && jar -cf commons-io-javadoc.jar org)

## This does not seem to work for me:
# -Dhomedir=/projects/swlab1/checker-framework/hosting-info

mvn gpg:sign-and-deploy-file -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=sonatype-nexus-staging -DpomFile=docs/cfMavenCentral.xml -Dgpg.publicKeyring=/projects/swlab1/checker-framework/hosting-info/pubring.gpg -Dgpg.secretKeyring=/projects/swlab1/checker-framework/hosting-info/secring.gpg -Dgpg.keyname=ADF4D638 -Dgpg.passphrase="`cat /projects/swlab1/checker-framework/hosting-info/release-private.password`" -Dfile=target/commons-io-2.6.jar

mvn gpg:sign-and-deploy-file -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=sonatype-nexus-staging -DpomFile=docs/cfMavenCentral.xml -Dgpg.publicKeyring=/projects/swlab1/checker-framework/hosting-info/pubring.gpg -Dgpg.secretKeyring=/projects/swlab1/checker-framework/hosting-info/secring.gpg -Dgpg.keyname=ADF4D638 -Dgpg.passphrase="`cat /projects/swlab1/checker-framework/hosting-info/release-private.password`" -Dfile=target/commons-io-2.6-sources.jar -Dclassifier=sources

mvn gpg:sign-and-deploy-file -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=sonatype-nexus-staging -DpomFile=docs/cfMavenCentral.xml -Dgpg.publicKeyring=/projects/swlab1/checker-framework/hosting-info/pubring.gpg -Dgpg.secretKeyring=/projects/swlab1/checker-framework/hosting-info/secring.gpg -Dgpg.keyname=ADF4D638 -Dgpg.passphrase="`cat /projects/swlab1/checker-framework/hosting-info/release-private.password`" -Dfile=target/site/apidocs/commons-io-javadoc.jar -Dclassifier=javadoc
